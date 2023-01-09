package fr.funixgaming.twitch.api.chatbot_irc;

import fr.funixgaming.twitch.api.tools.TwitchThreadPool;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class IRCSocketClient {

    private final String username;

    private final Queue<String> messagesQueue = new LinkedList<>();
    private final Set<String> channelsConnected = new HashSet<>();

    private final TwitchThreadPool threadPool;
    private final Logger logger;
    private final String oAuthCode;
    private final String domain;
    private final int port;

    private volatile boolean isRunning = true;
    private volatile boolean twitchReady = false;

    protected volatile SSLSocket socket = null;
    private volatile BufferedReader reader = null;
    private volatile PrintWriter writer = null;

    protected IRCSocketClient(final String domain, final int port, final String username, final String oAuthCode) {
        this.threadPool = new TwitchThreadPool(6);
        this.username = username.toLowerCase();
        this.logger = Logger.getLogger("TwitchIRC");
        this.oAuthCode = oAuthCode;
        this.domain = domain;
        this.port = port;

        this.start();
        this.messageTask();
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeConnection));
    }

    private void start() {
        final Thread botThread = new Thread(() -> {
            while (this.isRunning) {
                this.twitchReady = false;

                try {
                    if (!initSocketConnection()) {
                        Thread.sleep(5000);
                        continue;
                    }

                    reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                    writer = new PrintWriter(this.socket.getOutputStream());

                    initIrcConnection();

                    while (this.isRunning && this.socket.isConnected() && !this.socket.isClosed()) {
                        worker();
                    }

                } catch (IOException | InterruptedException e) {
                    logger.log(Level.SEVERE, "Error while initializing socket. Exception: " + e.getMessage());
                } finally {
                    this.twitchReady = false;
                    cleanMemory();
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        botThread.setName("IRC-TwitchBot");
        botThread.start();
    }

    /**
     * Initialize socket connection.
     * @return true if connection is successful, false otherwise
     */
    private boolean initSocketConnection() {
        try {
            logger.log(Level.INFO, "Connecting to " + domain + ':' + port + "...");

            final SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            this.socket = (SSLSocket) factory.createSocket(domain, port);
        } catch (IOException e) {
            logger.log(Level.WARNING, "Could not connect to " + domain + ':' + port + ". Reason: " + e.getMessage());
            return false;
        }

        final Thread checkLogin = new Thread(() -> {
            try {
                Thread.sleep(10000);

                if (!this.twitchReady) {
                    logger.log(Level.WARNING, "Twitch not responding, retry login...");
                    this.socket.close();
                }
            } catch (InterruptedException | IOException e) {
                logger.log(Level.WARNING, "Error while waiting for Twitch response. Exception: " + e.getMessage());
            }
        });

        checkLogin.setName("IRC-TwitchBot CheckLogin");
        checkLogin.start();
        return true;
    }

    private void initIrcConnection() {
        this.writer.println("PASS oauth:" + this.oAuthCode);
        this.writer.println("NICK " + this.username);
        this.writer.println("CAP REQ :twitch.tv/tags");
        this.writer.println("CAP REQ :twitch.tv/commands");
        this.writer.println("CAP REQ :twitch.tv/membership");
        this.writer.flush();
    }

    private void worker() {
        try {

            final String message = reader.readLine();
            if (message == null) {
                return;
            }

            if (message.startsWith(":tmi.twitch.tv 001 " + this.username + " :Welcome, GLHF!")) {

                this.twitchReady = true;
                logger.log(Level.INFO, "Connected to " + domain + ':' + port + " !");

                for (final String channel : this.channelsConnected) {
                    this.joinChannel(channel);
                }

            } else if (!twitchReady) {
                logger.log(Level.WARNING, "IRC received message while in not ready state: " + message);
            } else {
                this.threadPool.newTask(() -> this.onSocketMessage(message));
            }

        } catch (IOException e) {
            if (!this.socket.isClosed()) {
                logger.log(Level.SEVERE, "Error while reading from socket. Exception: " + e.getMessage());
            }
        }
    }

    /**
     * Handles the message queue.
     */
    private void messageTask() {
        final Thread messageThread = new Thread(() -> {
            try {
                while (this.isRunning) {
                    if (this.messagesQueue.size() > 0 && this.twitchReady) {
                        final String message = this.messagesQueue.poll();

                        if (message != null) {
                            writer.println(message);
                        }
                        writer.flush();
                    }

                    Thread.sleep(150);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        messageThread.setName("IRC-TwitchBot Message-Thread");
        messageThread.start();
    }

    private void cleanMemory() {
        try {
            if (this.reader != null) {
                this.reader.close();
                this.reader = null;
            }

            if (this.writer != null) {
                this.writer.close();
                this.writer = null;
            }

            if (this.socket != null && !this.socket.isClosed()) {
                this.socket.close();
                this.socket = null;
            }
        } catch (IOException exception) {
            logger.log(Level.SEVERE, "Error while cleaning memory socket. Exception: " + exception.getMessage());
        }
    }

    private boolean isRunning() {
        return this.isRunning && this.socket != null && !this.socket.isClosed();
    }

    public boolean isConnected() {
        return this.isRunning() && this.twitchReady && this.socket.isConnected();
    }

    public void closeConnection() {
        this.isRunning = false;
        this.twitchReady = false;

        try {
            if (this.isConnected()) {
                this.logger.log(Level.INFO, "Closing Twitch IRC...");
                this.socket.close();
                this.logger.log(Level.INFO, "TwitchIRC closed.");
            }
        } catch (IOException ioException) {
            logger.log(Level.SEVERE, "Error while closing TwitchIRC. Exception: " + ioException.getMessage());
        }
    }

    protected void sendMessage(final String message) {
        this.messagesQueue.add(message);
    }

    protected void joinChannel(String channel) {
        channel = channel.toLowerCase();

        this.sendMessage("JOIN #" + channel + "\n");
        this.channelsConnected.add(channel);
        logger.log(Level.INFO, "Joined channel #" + channel);
    }

    protected void leaveChannel(String channel) {
        channel = channel.toLowerCase();

        this.sendMessage("PART #" + channel + "\n");
        this.channelsConnected.remove(channel);
        logger.log(Level.INFO, "Left channel #" + channel);
    }

    public Logger getLogger() {
        return this.logger;
    }

    protected abstract void onSocketMessage(final String message);

}

package fr.funixgaming.twitch.api.chatbot_irc;

import fr.funixgaming.twitch.api.exceptions.TwitchIRCException;
import fr.funixgaming.twitch.api.tools.TwitchThreadPool;
import lombok.Getter;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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
    @Getter
    private final Logger logger;
    private final String oAuthCode;
    private final String domain;
    private final int port;

    private volatile boolean isRunning = true;
    private volatile boolean twitchReady = false;
    private volatile long lastMessageTs = System.currentTimeMillis();
    protected volatile boolean needRestart = false;

    protected IRCSocketClient(final String domain, final int port, final String username, final String oAuthCode) {
        this.threadPool = new TwitchThreadPool(6);
        this.username = username.toLowerCase();
        this.logger = Logger.getLogger("TwitchIRC");
        this.oAuthCode = oAuthCode;
        this.domain = domain;
        this.port = port;

        this.start();
    }

    private void start() {
        final Thread botThread = new Thread(() -> {
            while (this.isRunning) {
                this.twitchReady = false;
                this.needRestart = false;

                try (final SSLSocket socket = this.initSocketConnection();
                     final BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     final PrintWriter writer = new PrintWriter(socket.getOutputStream())) {

                    writer.println("PASS oauth:" + this.oAuthCode);
                    writer.println("NICK " + this.username);
                    writer.println("CAP REQ :twitch.tv/tags");
                    writer.println("CAP REQ :twitch.tv/commands");
                    writer.println("CAP REQ :twitch.tv/membership");
                    writer.flush();

                    this.checkTimeoutWorker(socket);
                    while (this.isRunning && !needRestart && socket.isConnected() && !socket.isClosed()) {
                        this.worker(reader, writer);
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, String.format("Error start socket IRC. Exception: %s", e.getMessage()), e);

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException interruptedException) {
                        logger.log(Level.SEVERE, "Error while waiting for Twitch response. Exception: " + interruptedException.getMessage());
                    }
                }
            }
        });

        botThread.setName("IRC-TwitchBot");
        botThread.start();
    }

    /**
     * Initialize socket connection.
     */
    private SSLSocket initSocketConnection() throws TwitchIRCException {
        try {
            logger.log(Level.INFO, String.format("Connecting to %s:%d...", domain, port));

            final SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            final SSLSocket socket = (SSLSocket) factory.createSocket(domain, port);
            final Thread checkLogin = checkLoginThread(socket);

            checkLogin.start();
            return socket;
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Could not connect to %s:%d. Reason: %s", domain, port, e.getMessage()), e);
            throw new TwitchIRCException("Error login Twitch IRC");
        }
    }

    private Thread checkLoginThread(final SSLSocket socket) {
        final Thread checkLogin = new Thread(() -> {
            try {
                Thread.sleep(10000);

                if (!this.twitchReady) {
                    logger.log(Level.WARNING, "Twitch not responding, retry login...");
                    socket.close();
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Error while waiting for Twitch response. Exception: " + e.getMessage(), e);
            }
        });

        checkLogin.setName("IRC-TwitchBot CheckLogin");
        return checkLogin;
    }

    private void worker(final BufferedReader reader, final PrintWriter writer) throws TwitchIRCException {
        try {
            final String message = reader.readLine();
            if (message == null) {
                return;
            }

            this.lastMessageTs = System.currentTimeMillis();
            if (message.startsWith(":tmi.twitch.tv 001 " + this.username + " :Welcome, GLHF!")) {
                this.twitchReady = true;
                logger.log(Level.INFO, "Connected to " + domain + ':' + port + " !");
                this.messageTask(writer);

                for (final String channel : this.channelsConnected) {
                    this.joinChannel(channel);
                }
            } else if (!twitchReady) {
                logger.log(Level.WARNING, "IRC received message while in not ready state: " + message);
            } else {
                this.threadPool.newTask(() -> this.onSocketMessage(message));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while reading from socket. Exception: " + e.getMessage(), e);
            throw new TwitchIRCException("Error while reading from socket");
        }
    }

    private void checkTimeoutWorker(final Socket socket) {
        final Thread checkTimeout = new Thread(() -> {
            while (this.isRunning && !needRestart && socket.isConnected() && !socket.isClosed()) {
                try {
                    Thread.sleep(1000);

                    if (System.currentTimeMillis() - this.lastMessageTs > 330000) {
                        logger.log(Level.WARNING, "Twitch not responding, retry login...");
                        needRestart = true;
                        socket.close();
                    }
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Error while waiting for Twitch response. Exception: " + e.getMessage(), e);
                }
            }
        });

        checkTimeout.setName("IRC-TwitchBot CheckTimeout");
        checkTimeout.start();
    }

    /**
     * Handles the message queue.
     */
    private void messageTask(final PrintWriter writer) {
        final Thread messageThread = new Thread(() -> {
            try {
                while (this.twitchReady) {
                    String message = this.messagesQueue.poll();

                    while (message != null) {
                        writer.println(message);
                        message = this.messagesQueue.poll();
                    }
                    if (writer.checkError()) {
                        this.needRestart = true;
                        throw new TwitchIRCException("Error while sending message to Twitch IRC");
                    }

                    Thread.sleep(150);
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error while sending message to Twitch IRC. Exception: " + e.getMessage(), e);
            }
        });

        messageThread.setName("IRC-TwitchBot Message-Thread");
        messageThread.start();
    }

    public boolean isConnected() {
        return this.isRunning;
    }

    public void closeConnection() {
        this.isRunning = false;
        this.twitchReady = false;
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

    protected abstract void onSocketMessage(final String message);

}

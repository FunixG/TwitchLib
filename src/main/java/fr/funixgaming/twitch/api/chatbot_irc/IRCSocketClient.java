package fr.funixgaming.twitch.api.chatbot_irc;

import fr.funixgaming.twitch.api.auth.TwitchAuth;
import fr.funixgaming.twitch.api.exceptions.TwitchApiException;
import fr.funixgaming.twitch.api.tools.TwitchThreadPool;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class IRCSocketClient {

    private final String username;

    private final Queue<String> messagesQueue = new LinkedList<>();
    private final TwitchThreadPool threadPool;
    private final Logger logger;
    private final TwitchAuth auth;

    private volatile boolean isRunning = true;
    private volatile boolean twitchReady = false;

    private volatile SSLSocket socket;
    private volatile BufferedInputStream reader;
    private volatile PrintWriter writer;

    protected IRCSocketClient(final String domain, final int port, final String username, final TwitchAuth auth) {
        this.threadPool = new TwitchThreadPool(6);
        this.username = username.toLowerCase();
        this.logger = Logger.getLogger("TwitchIRC");
        this.auth = auth;

        this.start(domain, port);
        this.messageTask();
        Runtime.getRuntime().addShutdownHook(new Thread(this::closeConnection));
    }

    public boolean isConnected() {
        return this.isRunning() && this.twitchReady && this.socket.isConnected();
    }

    private boolean isRunning() {
        return this.isRunning && this.socket != null && !this.socket.isClosed();
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
            ioException.printStackTrace();
        }
    }

    protected void sendMessage(final String message) {
        this.messagesQueue.add(message);
    }

    protected abstract void onSocketMessage(final String message);

    private void start(final String domain, final int port) {
        final Thread botThread = new Thread(() -> {
            while (this.isRunning) {
                this.twitchReady = false;

                try {
                    logger.log(Level.INFO, "Connecting to " + domain + ':' + port + "...");

                    final SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                    this.socket = (SSLSocket) factory.createSocket(domain, port);
                } catch (IOException e) {
                    logger.log(Level.WARNING, "Could not connect to " + domain + ':' + port + ". Reason: " + e.getMessage());

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    continue;
                }

                final Thread checkLogin = new Thread(() -> {
                    try {
                        Thread.sleep(10000);
                        if (!this.twitchReady) {
                            logger.log(Level.WARNING, "Twitch not responding, retry login...");
                            this.socket.close();
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                });
                checkLogin.setName("CheckLogin Thread");
                checkLogin.start();


                try {
                    reader = new BufferedInputStream(this.socket.getInputStream());
                    writer = new PrintWriter(this.socket.getOutputStream());

                    if (!this.auth.isValid()) {
                        this.auth.refresh();
                    }
                    this.auth.isUsable();

                    this.writer.println("PASS oauth:" + this.auth.getAccessToken());
                    this.writer.println("NICK " + this.username);
                    this.writer.println("CAP REQ :twitch.tv/tags");
                    this.writer.println("CAP REQ :twitch.tv/commands");
                    this.writer.println("CAP REQ :twitch.tv/membership");
                    this.writer.flush();

                    while (this.socket.isConnected() && !this.socket.isClosed()) {
                        try {
                            final byte[] messageByte = new byte[10000];
                            int stream = this.reader.read(messageByte);

                            if (stream > 0) {
                                final String message = new String(messageByte, 0, stream);

                                if (message.contains(":tmi.twitch.tv 001 " + this.username + " :Welcome, GLHF!")) {
                                    this.twitchReady = true;
                                    logger.log(Level.INFO, "Connected to " + domain + ':' + port + " !");
                                } else if (!twitchReady) {
                                    logger.log(Level.INFO, message);
                                } else {
                                    this.threadPool.newTask(() -> this.onSocketMessage(message));
                                }
                            }
                        } catch (IOException e) {
                            if (!this.socket.isClosed())
                                e.printStackTrace();
                            this.twitchReady = false;
                        }
                    }

                } catch (IOException | TwitchApiException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (this.reader != null)
                            this.reader.close();
                        if (this.writer != null)
                            this.writer.close();
                        if (this.socket != null && !this.socket.isClosed()) {
                            this.socket.close();
                        }
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }

                    this.twitchReady = false;
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        botThread.setName("TwitchBot Thread");
        botThread.start();
    }

    private void messageTask() {
        final Thread messageThread = new Thread(() -> {
            try {
                while (this.isRunning) {
                    if (this.messagesQueue.size() > 0 && this.twitchReady) {
                        final String message = this.messagesQueue.poll();

                        if (message != null) {
                            writer.println(message);
                            writer.flush();
                        }
                    }

                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        messageThread.setName("IRC-Message-Thread-Twitch");
        messageThread.start();
    }

    public Logger getLogger() {
        return this.logger;
    }
}

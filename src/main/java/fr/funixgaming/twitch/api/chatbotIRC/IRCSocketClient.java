package fr.funixgaming.twitch.api.chatbotIRC;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class IRCSocketClient {

    private final String domain;
    private final int port;
    private final String username;
    private final String oauthToken;
    private final Queue<String> messagesQueue;

    private SSLSocket socket;
    private BufferedInputStream reader;
    private PrintWriter writer;
    private boolean isRunning;
    private boolean twitchReady;

    protected IRCSocketClient(final String domain, final int port, final String username, final String oauthToken) {
        this.messagesQueue = new PriorityQueue<>();
        this.socket = null;
        this.domain = domain;
        this.port = port;
        this.username = username.toLowerCase();
        this.oauthToken = oauthToken;
        this.isRunning = true;
        this.twitchReady = false;
    }

    public void start() {
        new Thread(() -> {
            while (this.isRunning) {
                try {
                    System.out.println("Connecting to " + this.domain + ':' + this.port + "...");
                    final SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                    this.socket = (SSLSocket) factory.createSocket(this.domain, this.port);
                } catch (IOException e) {
                    System.err.println("Could not connect to " + this.domain + ':' + this.port + ".\nReason: " + e.getMessage());
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    continue;
                }
                System.out.println("Connected to " + this.domain + ':' + this.port + " !");

                try {
                    reader = new BufferedInputStream(this.socket.getInputStream());
                    writer = new PrintWriter(this.socket.getOutputStream());

                    this.writer.println("PASS " + this.oauthToken);
                    this.writer.println("NICK " + this.username);
                    this.writer.println("CAP REQ :twitch.tv/tags");
                    this.writer.println("CAP REQ :twitch.tv/commands");
                    this.writer.println("CAP REQ :twitch.tv/membership");
                    this.writer.flush();

                    while (this.socket.isConnected()) {
                        try {
                            byte[] messageByte = new byte[10000];
                            int stream = this.reader.read(messageByte);
                            if (stream > 0) {
                                final String message = new String(messageByte, 0, stream);

                                if (message.contains(":tmi.twitch.tv 001 " + this.username + " :Welcome, GLHF!")) {
                                    this.twitchReady = true;
                                    System.out.println("Twitch IRC connected !");
                                    while (!this.messagesQueue.isEmpty())
                                        this.sendMessage(this.messagesQueue.remove());
                                } else {
                                    new Thread(() -> this.onSocketMessage(message)).start();
                                }
                            }
                        } catch (IOException e) {
                            if (!(e instanceof SocketException) && !e.getMessage().equals("Socket closed"))
                                e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
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
                }

                this.twitchReady = false;
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected void sendMessage(final String message) {
        new Thread(() -> {
            if (this.socket != null && this.socket.isConnected() && this.twitchReady) {
                writer.println(message);
                writer.flush();
            } else {
                this.messagesQueue.add(message);
            }
        }).start();
    }

    public boolean isRunning() {
        return this.isRunning && this.socket != null && !this.socket.isClosed() && this.socket.isConnected();
    }

    public void closeConnection() {
        this.isRunning = false;
        try {
            this.socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    protected abstract void onSocketMessage(final String message);

}

package fr.funixgaming.twitch.api.chatbot_irc;

import fr.funixgaming.twitch.api.tools.TwitchThreadPool;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.util.PriorityQueue;
import java.util.Queue;

public abstract class IRCSocketClient {

    private final String username;
    private final String oauthToken;

    private final Queue<String> messagesQueue = new PriorityQueue<>();
    private final TwitchThreadPool threadPool;

    private boolean isRunning = true;
    private boolean twitchReady = false;

    private SSLSocket socket;
    private BufferedInputStream reader;
    private PrintWriter writer;

    protected IRCSocketClient(final String domain, final int port, final String username, final String oauthToken) {
        this.threadPool = new TwitchThreadPool(6);
        this.username = username.toLowerCase();
        this.oauthToken = oauthToken;

        this.start(domain, port);
    }

    private void start(final String domain, final int port) {
        new Thread(() -> {
            while (this.isRunning) {
                try {
                    System.out.println("Connecting to " + domain + ':' + port + "...");

                    final SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                    this.socket = (SSLSocket) factory.createSocket(domain, port);

                } catch (IOException e) {
                    System.err.println("Could not connect to " + domain + ':' + port + ".\nReason: " + e.getMessage());

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                    continue;
                }

                final Thread checkLoggedThread = new Thread(() -> {
                    try {
                        Thread.sleep(10000);
                        if (!this.twitchReady) {
                            System.out.println("Twitch not responding, retry login...");
                            this.socket.close();
                        }
                    } catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                });
                checkLoggedThread.setName("CheckLoggedThread");
                checkLoggedThread.start();

                try {
                    reader = new BufferedInputStream(this.socket.getInputStream());
                    writer = new PrintWriter(this.socket.getOutputStream());

                    this.writer.println("PASS " + this.oauthToken);
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
                                    System.out.println("Connected to " + domain + ':' + port + " !");

                                    while (!this.messagesQueue.isEmpty()) {
                                        this.sendMessage(this.messagesQueue.remove());
                                    }
                                } else {
                                    this.threadPool.newTask(() -> this.onSocketMessage(message));
                                }

                            }
                        } catch (IOException e) {
                            if (!this.socket.isClosed())
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
        this.threadPool.newTask(() -> {
            if (this.socket != null && this.socket.isConnected() && this.twitchReady) {
                writer.println(message);
                writer.flush();
            } else {
                this.messagesQueue.add(message);
            }
        });
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

package com.app.debate1;

import com.app.debate1.console.ConsoleInput;
import com.app.debate1.pubsub.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Peer {
    private Logger logger = LoggerFactory.getLogger(Peer.class);
    private static final String IP = "127.0.0.1";
    private static final int PORT1 = 6060; // server
    private static final int PORT2 = 7070; // citeste + scrie
    private Socket socket;
    private BufferedReader from;
    private PrintWriter to;
    private ServerSocket serverChannel = null;
    private volatile String token;
    private Publisher publisher;
    private final Pattern messagePattern = Pattern.compile("(\"[^\"]*\"|'[^']*')");



    public Peer(String token, Publisher publisher)
    {
        this.token = token;
        this.publisher = publisher;
    }

    private void openApp(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        from = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        to = new PrintWriter(socket.getOutputStream(), true);
    }

    private String send(String content) throws IOException {
        to.println(content);
        return from.readLine();
    }

    private void closeApp() throws IOException {
        to.close();
        from.close();
        socket.close();
    }

    private void passToken() {
        try {
            String response = this.send(token);
            logger.info(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        token = null;
    }

    public void start() {
        new Thread(() -> {
            boolean connected = false;

            while (!connected) {
                try {
                    openApp(IP, PORT2);
                    connected = true;
                    logger.info("Connected!");
                } catch (IOException e) {
                    connected = false;
                    logger.info("Cannot connect yet!");
                }
            }
            while (true) {
                if (token != null) { // are token
                    logger.info("Command>");
                    ConsoleInput con = new ConsoleInput(
                            1,
                            30,
                            TimeUnit.SECONDS
                    );
                    String userInput = null;
                    try {
                        userInput = con.readLine();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // if userInput is null then expire...
                    if (userInput == null) {
                        // Timeout
                        passToken();

                    } else if (userInput.equals("exit")) {
                        break;
                    } else {
                        if(!userInput.contains(":")) {
                            logger.warn("Invalid command format!");
                            continue;
                        }
                        String[] splits = userInput.split(":");
                        if(splits.length != 2) {
                            logger.warn("Invalid command format!");
                            continue;
                        }
                        String[] topics = splits[0].split("\\+");
                        boolean topicNotFound = false;
                        for(int i = 0; i < topics.length; i++) {
                            if(!topics[i].equals("cooking") && !topics[i].equals("sport")) {
                                topicNotFound = true;
                                break;
                            }
                        }

                        if(topicNotFound) {
                            logger.warn("Incorrect topic/s inserted!");
                            continue;
                        }
                        Matcher matcher = messagePattern.matcher(splits[1]);
                        // FORMAT:
                        // topic1 + topic2 + topic3: ['aaa','bbb','ccc']
                        // topic1 + topic2: ['aaa', 'bbb']
                        // topic1 : ['aaa']
                        List<String> messages = new ArrayList<String>();
                        String res = null;
                        int index = 0;
                        while (matcher.find(index) && (res = matcher.group()) != null) {
                            messages.add(res);
                            index = matcher.end();
                        }

                        if (topics.length != messages.size()) {
                            logger.warn("Number of topics is not equal to number of messages!");
                        } else {
                            int size = messages.size();
                            for (int i = 0; i < size; i++) {
                                String message = messages.get(i);
                                String topic = topics[i];

                                switch (topic) {
                                    case "cooking":
                                        publisher.publishCookingMessage(message);
                                        break;
                                    case "sport":
                                        publisher.publishSportMessage(message);
                                        break;
                                    default:
                                        logger.info("Invalid topic " + topic);
                                }
                            }
                            passToken();
                        }
                    }

                }
            }
            try {
                this.closeApp();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        // Server
        try {
            serverChannel = new ServerSocket(PORT1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket socketForClient = null;

        try {
            socketForClient = serverChannel.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Socket finalSocketForClient = socketForClient;
        BufferedReader fromClient;
        PrintWriter toClient;
        try {
            fromClient = new BufferedReader(new InputStreamReader(finalSocketForClient.getInputStream()));
            toClient = new PrintWriter(finalSocketForClient.getOutputStream(), true);

            String message;
            while ((message = fromClient.readLine()) != null) {
                if ("abc".equals(message)) {
                    token = message;
                    toClient.println("token passed");
                } else {
                    toClient.println("Invalid token!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

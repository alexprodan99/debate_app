package com.app.debate0.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class ConsoleInputReadTask implements Callable<String> {
    private Logger logger = LoggerFactory.getLogger(ConsoleInput.class);

    public String call() throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
        String input;
        do {
            try {
                // wait until we have data to complete a readLine()
                while (!br.ready()) {
                    Thread.sleep(200);
                }
                input = br.readLine();
            } catch (InterruptedException e) {
                logger.info("ConsoleInputReadTask() cancelled");
                return null;
            }
        } while ("exit".equals(input));
        return input;
    }
}

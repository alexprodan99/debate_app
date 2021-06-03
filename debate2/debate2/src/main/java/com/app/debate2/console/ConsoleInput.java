package com.app.debate2.console;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class ConsoleInput {
    private Logger logger = LoggerFactory.getLogger(ConsoleInput.class);
    private final int tries;
    private final int timeout;
    private final TimeUnit unit;

    public ConsoleInput(int tries, int timeout, TimeUnit unit) {
        this.tries = tries;
        this.timeout = timeout;
        this.unit = unit;
    }

    public String readLine() throws InterruptedException {
        ExecutorService ex = Executors.newSingleThreadExecutor();
        String input = null;
        try {
            // start working
            for (int i = 0; i < tries; i++) {
                Future<String> result = ex.submit(
                        new ConsoleInputReadTask());
                try {
                    input = result.get(timeout, unit);
                    break;
                } catch (ExecutionException e) {
                    e.getCause().printStackTrace();
                } catch (TimeoutException e) {
                    logger.info("Cancelling reading task");
                    result.cancel(true);
                    logger.info("\nThread cancelled. input is null");
                }
            }
        } finally {
            ex.shutdownNow();
        }
        return input;
    }
}

package fr.funixgaming.twitch.api.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TwitchThreadPool {

    private final ExecutorService executor;

    /**
     * Initialize the thread poll with n max threads
     * @param numberOfThreads maximum threads executing
     */
    public TwitchThreadPool(final int numberOfThreads) {
        this.executor = Executors.newFixedThreadPool(numberOfThreads);
    }

    /**
     * Add a new task to the queue.
     * If a thread is available the task is executed and his Future is returned
     * @param task Runnable instance to execute
     * @return The future response of the Runnable sended
     */
    public Future<?> newTask(final Runnable task) {
        return executor.submit(task);
    }
}

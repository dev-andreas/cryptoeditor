package com.andreas.main.temp;

import java.nio.file.Path;

import com.andreas.main.FileUtils;

/**
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public class TempHandler implements Runnable {

    public static final float CLEANINGS_PER_SECONDS = 1f;

    private boolean running, tempFileExists, isOrdered;
    private long cleaningTime;

    private Path tempPath;

    /**
     * This is the first constructor of the class.
     * @param tempPath The path of your temp file.
     */
    public TempHandler(Path tempPath) {
        this.tempPath = tempPath;
        tempFileExists = true;
        isOrdered = true;
    }

    /**
     * This is the second constructor of the class.
     * @param tempPath The path of your temp file.
     * @param waitUntilOrdered Set <code>true</code> if you want to order the deletion manually.
     */
    public TempHandler(Path tempPath, boolean waitUntilOrdered) {
        this.tempPath = tempPath;
        tempFileExists = true;
        isOrdered = !waitUntilOrdered;
    }

    /**
     * Thread run method.
     */
    @Override
    public void run() {
        cleaningTime = (long) (1 / CLEANINGS_PER_SECONDS * 1000);

        running = true;
        while (running) {
            try {
                Thread.sleep(cleaningTime);
            } catch (InterruptedException e) { }

            if (!isOrdered) continue;

            if (tempFileExists)
                clean();
            else 
                stop();
        }
    }

    /**
     * This method deletes the temp file.
     */
    private void clean() {

        if (FileUtils.deleteFile(tempPath.toString())) {
            tempFileExists = false;
            System.out.println(tempPath.getFileName().toString() + " deleted.");
        }
    }

    /**
     * This method stops the thred.
     */
    public void stop() {
        running = false;
    }

    /**
     * Ths method orders deletion of this file.
     */
    public void orderDelete() {
        isOrdered = true;
        clean();
    }
}

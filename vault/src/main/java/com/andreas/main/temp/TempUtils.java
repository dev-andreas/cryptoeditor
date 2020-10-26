package com.andreas.main.temp;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.andreas.main.FileUtils;

/**
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public class TempUtils {
   
    /**
     * This method should be called inside the application start method.
     */
    public static void init() {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                if (Files.exists(Paths.get("data/temp/")))
                    FileUtils.deleteDirectory("data/temp/");
            }
        });
    }

    /**
     * This method creates a temp file.
     * @param data The data as <code>byte[]</code> that should be inside the temp file.
     * @return Returns the path of the created temp file.
     */
    public static TempHandler createTempFile(byte[] data, boolean waitUntilOrdered) {

        if (!Files.exists(Paths.get("data/temp/")))
            FileUtils.createDirectories("data/temp/");

        String name = System.nanoTime() + ".tmp";
        FileUtils.createBinaryFile("data/temp/" + name, data);

        TempHandler tempHandler = new TempHandler(Paths.get("data/temp/" + name), waitUntilOrdered);

        Thread tempHandlerThread = new Thread(tempHandler);
        tempHandlerThread.setDaemon(true);
        tempHandlerThread.start();

        return tempHandler;
    }
}

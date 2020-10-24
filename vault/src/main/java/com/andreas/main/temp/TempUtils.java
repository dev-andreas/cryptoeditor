package com.andreas.main.temp;

import java.nio.file.Files;
import java.nio.file.Path;
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
    public static Path createTempFile(byte[] data) {

        if (!Files.exists(Paths.get("data/temp/")))
            FileUtils.createDirectories("data/temp/");

        String name = System.nanoTime() + ".tmp";
        FileUtils.createBinaryFile("data/temp/" + name, data);

        Thread tempHandlerThread = new Thread(new TempHandler(Paths.get("data/temp/" + name)));
        tempHandlerThread.setDaemon(true);
        tempHandlerThread.start();

        return Paths.get("data/temp/" + name);
    }
}

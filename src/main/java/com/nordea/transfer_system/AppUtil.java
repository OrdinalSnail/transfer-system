package com.nordea.transfer_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class AppUtil {

    private static final Logger LOG = LoggerFactory.getLogger(AppUtil.class);

    private AppUtil() {
    }

    /**
     * Method for getting resource
     *
     * @param path path to resource file
     * @return resource as InputStream
     */
    public static InputStream getResourceAsStream(String path) {
        return AppUtil.class.getClassLoader().getResourceAsStream(path);
    }


    /**
     * Method for getting file as InputStream
     *
     * @param path path to file
     * @return file as InputStream
     */
    public static InputStream getFileAsStream(String path) {
        if (path == null) {
            return null;
        }
        try {
            File initialFile = new File(path);
            InputStream targetStream = new FileInputStream(initialFile);
            return targetStream;
        } catch (FileNotFoundException e) {
            LOG.error("File didn't find", e);
        }
        return null;
    }

}

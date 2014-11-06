package com.linkage.community.schedule.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility class used to copy files
 */
public class FileUtils {

    /**
     * Copy a file from a source to a destination
     * 
     * @param sourceFile
     *            the file to copy from .
     * @param destFile
     *            the file to copy to.
     * @throws IOException
     *             if the copying fails
     */
    public static void copyFile(String sourceFile, String destFile)
            throws IOException {
        copyFile(new File(sourceFile), new File(destFile), true);
    }

    /**
     * Convienence method to copy a file from a source to a destination
     * 
     * @param sourceFile
     *            the file to copy from. Must not be <code>null</code>.
     * @param destFile
     *            the file to copy to. Must not be <code>null</code>.
     * @param overwrite
     *            Whether or not the destination file should be overwritten if
     *            it already exists.
     * @throws IOException
     *             if the copying fails
     */
    public static void copyFile(File sourceFile, File destFile,
            boolean overwrite) throws IOException {

        if (overwrite && destFile.exists() && destFile.isFile()) {
            destFile.delete();
        }

        // ensure that parent dir of dest file exists!
        File parent = getParentFile(destFile);
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(sourceFile);
            out = new FileOutputStream(destFile);

            byte[] buffer = new byte[8 * 1024];
            int count = 0;
            do {
                out.write(buffer, 0, count);
                count = in.read(buffer, 0, buffer.length);
            } while (count != -1);
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
    }

    /**
     * Creates a new file with specialized name and inputstream
     * 
     * @param fileName
     *            specialized file name
     * @param in
     *            An inputstream
     * @return a newly created file
     * @throws IOException
     *             if an I/O error occurs
     */
    public static File createFile(String fileName, InputStream in)
            throws IOException {
        File file = new File(fileName);
        FileOutputStream out = new FileOutputStream(file);
        try {
            byte[] buffer = new byte[8 * 1024];
            int count = 0;
            while (count != -1) {
                out.write(buffer, 0, count);
                count = in.read(buffer, 0, buffer.length);
            }
        } finally {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        }
        return null;
    }

    /**
     * Emulation of File.getParentFile for JDK 1.1
     * 
     * 
     * @param f
     *            the file whose parent is required.
     * @return the given file's parent, or null if the file does not have a
     *         parent.
     * @since 1.10
     */
    public static File getParentFile(File f) {
        if (f != null) {
            String p = f.getParent();
            if (p != null) { return new File(p); }
        }
        return null;
    }
}
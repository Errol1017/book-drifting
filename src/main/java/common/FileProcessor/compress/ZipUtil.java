package common.FileProcessor.compress;

import project.resource.properties.ServerProperties;

import java.io.*;
import java.util.List;
import java.util.Random;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.zip.CRC32;

/**
 * Created by Errol on 17/6/5.
 */
public class ZipUtil {

    private static String fileBathPath = ServerProperties.getInstance().getFileBasePath();
    private static final String path = "zip/";

    static final int BUFFER = 8192;

    public static String compress(List<String> filenames, String zipName) {
        zipName = getZipName(zipName);
        try {
            ZipOutputStream zipOutputStream = getZipOutputStream(zipName);
            for (String filename: filenames) {
                compress(new File(fileBathPath + filename), zipOutputStream, "");
            }
            zipOutputStream.close();
            return zipName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String compress(String filename, String zipName) {
        zipName = getZipName(zipName);
        try {
            ZipOutputStream zipOutputStream = getZipOutputStream(zipName);
            compress(new File(fileBathPath + filename), zipOutputStream, "");
            zipOutputStream.close();
            return zipName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getZipName(String zipName) {
        if (zipName.equals("")) {
            zipName = System.currentTimeMillis() + "_" + new Random().nextInt(100);
        }
        zipName += ".zip";
        return path + zipName;
    }

    private static ZipOutputStream getZipOutputStream(String zipName) {
        try {
            File zipFile = new File(fileBathPath + zipName);
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream checkedOutputStream = new CheckedOutputStream(fileOutputStream, new CRC32());
            ZipOutputStream zipOutputStream = new ZipOutputStream(checkedOutputStream);
            return zipOutputStream;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void compress(File file, ZipOutputStream zipOutputStream, String path) {
        if (file.isDirectory()) {
            compressDirectory(file, zipOutputStream, path);
        } else {
            compressFile(file, zipOutputStream, path);
        }
    }

    private static void compressDirectory(File file, ZipOutputStream zipOutputStream, String path) {
        if (!file.exists()) {
            return;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            compress(files[i], zipOutputStream, path+file.getName()+"/");
        }
    }

    private static void compressFile(File file, ZipOutputStream zipOutputStream, String path) {
        if (!file.exists())
            return;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            ZipEntry entry = new ZipEntry(path + file.getName());
            zipOutputStream.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bufferedInputStream.read(data, 0, BUFFER)) != -1) {
                zipOutputStream.write(data, 0, count);
            }
            bufferedInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

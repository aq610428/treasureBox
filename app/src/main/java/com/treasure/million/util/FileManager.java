package com.treasure.million.util;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private static String PATH = AppInfo.getCompanyName() + File.separator
            + AppInfo.getFolderName() + File.separator + AppInfo.getUserId();// 基本路径;

    private static String LOG_PATH = AppInfo.getCompanyName() + File.separator + AppInfo.getFolderName();

    /**
     * 获取删除文件夹
     *
     * @return
     */
    public static String getDelPath() {
        if (hasSDCard()) {
            return exists(getRootFilePath() + PATH + "/");
        } else {
            return getRootFilePath() + PATH;
        }
    }

    /**
     * 获取上传的log信息文件路径
     *
     * @return 文件路径名称
     */
    public static String getLogPath() {
        return getRootFilePath() + LOG_PATH;
    }

    public static boolean isExistLogPath() {
        File file = new File(getLogPath());
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 刷新路径
     */
    public static void refreshPath() {
        PATH = AppInfo.getCompanyName() + File.separator
                + AppInfo.getFolderName() + File.separator + AppInfo.getUserId();// 基本路径;
    }

    /**
     * 判断文件夹是否存在，返回文件路径
     *
     * @param path
     * @return
     */
    private static String exists(String path) {
        if (!new File(path).exists()) {
            new File(path).mkdirs();
            return path;
        } else {
            return path;
        }
    }

    public static String getFilePath() {
        if (hasSDCard()) {
            return exists(Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/" + PATH + File.separator);
        } else {
            return null;
        }
    }

    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        if (!status.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    public static String getRootFilePath() {
        if (hasSDCard()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/";// filePath:/sdcard/
        } else {
            return Environment.getDataDirectory().getAbsolutePath() + "/data/"; // filePath:
            // /data/data/
        }
    }



    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void RecursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 创建多级目录
     *
     * @param filePath
     */
    public static void mkdirs(String filePath) {
        if (!isBlank(filePath)) {
            File file = new File(filePath);
            file.mkdirs();
        }
    }

    /**
     * 获取目录下所有文件
     *
     * @param filePath
     */
    public static List<String> getAllFile(String filePath, String fileType) {
        File path = new File(filePath);
        List<String> lists = new ArrayList<String>();
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            for (File file : files) {
                if (file.isFile() && file.getName().contains(fileType)) {
                    lists.add(file.getName());
                }
            }
        }
        return lists;
    }

    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    public static File screenShot(Bitmap bitmap) {
        try {
            String imagePath = FileManager.getFilePath() + "share.png";
            File file = new File(imagePath);
            FileOutputStream os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
            return file;
        } catch (Exception e) {
            LogUtils.e("screenshot:error=", e.getMessage());
        }
        return null;
    }

}

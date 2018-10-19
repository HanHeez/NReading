package com.gtv.hanhee.novelreading.Utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;

public class FileUtils {

    private static final String TAG = FileUtils.class.getSimpleName();

    /**
     * Tạo thư mục cache gốc
     *
     * @return
     */
    public static String createRootPath(Context context) {
        String cacheRootPath = "";
        if (isSdCardAvailable()) {
            // /sdcard/Android/data/<application package>/cache
            cacheRootPath = context.getExternalCacheDir().getPath();
        } else {
            // /data/data/<application package>/cache
            cacheRootPath = context.getCacheDir().getPath();
        }
        return cacheRootPath;
    }

    public static boolean isSdCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Tạo thư mục đệ quy
     *
     * @param dirPath
     * @return ""
     */
    public static String createDir(String dirPath) {
        try {
            File file = new File(dirPath);
            if (file.getParentFile().exists()) {
                LogUtils.i("----- Tạo thư mục" + file.getAbsolutePath());
                file.mkdir();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                LogUtils.i("----- Tạo thư mục" + file.getAbsolutePath());
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dirPath;
    }

    /**
     * Tạo File
     *
     * @param file
     * @return ""
     */
    public static String createFile(File file) {
        try {
            if (file.getParentFile().exists()) {
                LogUtils.i("----- Tạo file" + file.getAbsolutePath());
                file.createNewFile();
                return file.getAbsolutePath();
            } else {
                createDir(file.getParentFile().getAbsolutePath());
                file.createNewFile();
                LogUtils.i("----- Tạo file" + file.getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getImageCachePath(String path) {
        return createDir(path);
    }

    /**
     * Nhận thư mục cache hình ảnh trong bộ nhớ
     *
     * @return Không tạo được trả về " "
     */
    public static String getImageCachePath(Context context) {
        String path = createDir(createRootPath(context) + File.separator + "img" + File.separator);
        return path;
    }

    /**
     * Nhận thư mục chace hình ảnh crop
     *
     * @return Không tạo được trả về " "
     */
    public static String getImageCropCachePath(Context context) {
        String path = createDir(createRootPath(context) + File.separator + "imgCrop" + File.separator);
        return path;
    }

    /**
     * Viết nội dung vào File
     *
     * @param filePath eg:/mnt/sdcard/demo.txt
     * @param content  Nội dung
     */
    public static void writeFile(String filePath, String content, boolean isAppend) {
        try {
            FileOutputStream fout = new FileOutputStream(filePath, isAppend);
            byte[] bytes = content.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String filePathAndName, String fileContent) {
        try {
            OutputStream outstream = new FileOutputStream(filePathAndName);
            OutputStreamWriter out = new OutputStreamWriter(outstream);
            out.write(fileContent);
            out.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Mở File theo nội dung
     *
     * @param context
     * @param fileName
     * @return
     */
    public static InputStream openAssetFile(Context context, String fileName) {
        AssetManager am = context.getAssets();
        InputStream is = null;
        try {
            is = am.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    /**
     * nhận nội dung của File trong Raw
     *
     * @param context
     * @param resId
     * @return Nội dung file
     */
    public static String getFileFromRaw(Context context, int resId) {
        if (context == null) {
            return null;
        }

        StringBuilder s = new StringBuilder();
        try {
            InputStreamReader in = new InputStreamReader(context.getResources().openRawResource(resId));
            BufferedReader br = new BufferedReader(in);
            String line;
            while ((line = br.readLine()) != null) {
                s.append(line);
            }
            return s.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Sao chép File
     *
     * @param src  File nguồn
     * @param desc File đích
     */
    public static void fileChannelCopy(File src, File desc) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        try {
            fi = new FileInputStream(src);
            fo = new FileOutputStream(desc);
            FileChannel in = fi.getChannel();// lấy kênh File in tương ứng
            FileChannel out = fo.getChannel();//Lấy kênh File out tương ứng
            in.transferTo(0, in.size(), out);//Kết nối hai kênh, đọc từng kênh File in sau đó ghi vào kênh File out
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fo != null) fo.close();
                if (fi != null) fi.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Chuyển đổi kích thước File
     *
     * @param fileLen đơn vị byte
     * @return
     */
    public static String formatFileSizeToString(long fileLen) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileLen < 1024) {
            fileSizeString = df.format((double) fileLen) + "B";
        } else if (fileLen < 1048576) {
            fileSizeString = df.format((double) fileLen / 1024) + "K";
        } else if (fileLen < 1073741824) {
            fileSizeString = df.format((double) fileLen / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileLen / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * Xóa File chỉ định
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean deleteFile(File file) throws IOException {
        return deleteFileOrDirectory(file);
    }

    /**
     * Xóa các File được chỉ định, nếu là một thư mục thì đệ quy cách xóa
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean deleteFileOrDirectory(File file) throws IOException {
        try {
            if (file != null && file.isFile()) {
                return file.delete();
            }
            if (file != null && file.isDirectory()) {
                File[] childFiles = file.listFiles();
                // Xóa thư mục trống
                if (childFiles == null || childFiles.length == 0) {
                    return file.delete();
                }
                // Xóa đệ quy các thư mục con trong thư mục
                for (int i = 0; i < childFiles.length; i++) {
                    deleteFileOrDirectory(childFiles[i]);
                }
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /***
     * Tải File mở rộng
     *
     * @param filename tên file
     * @return
     */
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * Nhận nội dung File Output
     *
     * @param path
     * @return
     */
    public static String getFileOutputString(String path) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(path), 8192);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append("\n").append(line);
            }
            bufferedReader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
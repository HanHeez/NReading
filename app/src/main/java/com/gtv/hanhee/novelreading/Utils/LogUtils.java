package com.gtv.hanhee.novelreading.Utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.gtv.hanhee.novelreading.ReaderApplication;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LogUtils {
    // Định dạng đầu ra Format của Log
    private final static SimpleDateFormat LOG_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // Định dang mở rộng của file theo Date
    private final static SimpleDateFormat FILE_SUFFIX = new SimpleDateFormat("yyyy-MM-dd");
    private static Boolean LOG_SWITCH = true; // Log chuyển đổi hay không
    private static Boolean LOG_TO_FILE = false; // Log chuyển sang file hay ko
    private static String LOG_TAG = "TAG"; // Tag LOG
    private static char LOG_TYPE = 'v';// v là Log tất cả dữ liệu, w thì chỉ Log cảnh báo
    private static int LOG_SAVE_DAYS = 7;// Số ngày tối đa lưu trữ trong thẻ SD
    private static String LOG_FILE_PATH; // Đường dẫn lưu tệp Log
    private static String LOG_FILE_NAME;// tên tệp Log

    public static void init(Context context) { // Khởi tạo trong Application
        LOG_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + ReaderApplication.getsInstance().getPackageName();
        LOG_FILE_NAME = "Log";
    }

    /**************************** Warn *********************************/
    public static void w(Object msg) {
        w(LOG_TAG, msg);
    }

    public static void w(String tag, Object msg) {
        w(tag, msg, null);
    }

    public static void w(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'w');
    }

    /*************************** Error ********************************/
    public static void e(Object msg) {
        e(LOG_TAG, msg);
    }

    public static void e(String tag, Object msg) {
        e(tag, msg, null);
    }

    public static void e(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'e');
    }

    /*************************** Debug ********************************/
    public static void d(Object msg) {
        d(LOG_TAG, msg);
    }

    public static void d(String tag, Object msg) {// 调试信息
        d(tag, msg, null);
    }

    public static void d(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'd');
    }

    /**************************** Info *********************************/
    public static void i(Object msg) {
        i(LOG_TAG, msg);
    }

    public static void i(String tag, Object msg) {
        i(tag, msg, null);
    }

    public static void i(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'i');
    }

    /************************** Verbose ********************************/
    public static void v(Object msg) {
        v(LOG_TAG, msg);
    }

    public static void v(String tag, Object msg) {
        v(tag, msg, null);
    }

    public static void v(String tag, Object msg, Throwable tr) {
        log(tag, msg.toString(), tr, 'v');
    }

    /**
     * Tag, thông điệp, cấp độ
     *
     * @param tag
     * @param msg
     * @param level
     */
    private static void log(String tag, String msg, Throwable tr, char level) {
        if (LOG_SWITCH) {
            if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) { // Log đầu ra
                Log.e(tag, msg, tr);
            } else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.w(tag, msg, tr);
            } else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.d(tag, msg, tr);
            } else if ('i' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.i(tag, msg, tr);
            } else {
                Log.v(tag, msg, tr);
            }
            if (LOG_TO_FILE)
                log2File(String.valueOf(level), tag, msg + tr == null ? "" : "\n" + Log.getStackTraceString(tr));
        }
    }

    /**
     * Mở tệp Log và ghi vào Log
     *
     * @return
     **/
    private synchronized static void log2File(String mylogtype, String tag, String text) {
        Date nowtime = new Date();
        String date = FILE_SUFFIX.format(nowtime);
        String dateLogContent = LOG_FORMAT.format(nowtime) + ":" + mylogtype + ":" + tag + ":" + text; // Định đạng đầu ra Log
        File destDir = new File(LOG_FILE_PATH);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File file = new File(LOG_FILE_PATH, LOG_FILE_NAME + date);
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(dateLogContent);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa file Log chỉ định
     */
    public static void delFile() {// Xóa tệp Log
        String needDelFiel = FILE_SUFFIX.format(getDateBefore());
        File file = new File(LOG_FILE_PATH, needDelFiel + LOG_FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * Lấy ngày ghi nhớ Log
     *
     * @return
     */
    private static Date getDateBefore() {
        Date nowtime = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(nowtime);
        now.set(Calendar.DATE, now.get(Calendar.DATE) - LOG_SAVE_DAYS);
        return now.getTime();
    }
}

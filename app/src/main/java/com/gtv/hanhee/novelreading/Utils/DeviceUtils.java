package com.gtv.hanhee.novelreading.Utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.DisplayMetrics;

public class DeviceUtils {

    private static final String TAG = DeviceUtils.class.getSimpleName();
    private static final String Mobi_ISP = "45201";// Mobifone
    private static final String Vina_ISP = "45202";// Vinaphone
    private static final String SFone_ISP = "45203";// S-Fone/Telecom
    private static final String Viettel_ISP = "45204";// Viettel Mobile
    private static final String Vietna_ISP = "45205";// VietnaMobile
    private static final String Viettel2_ISP = "45206";// Viettel Mobile (formerly EVNTelecom)
    private static final String Gmobile_ISP = "45207"; // Gmobile
    private static final String Viettel3_ISP = "45208"; // Viettel Mobile (formerly EVNTelecom)

    /**
     * Nhận phiên bản hệ thống (SDK) của thiết bị
     */
    public static int getDeviceSDK() {
        int sdk = Build.VERSION.SDK_INT;
        return sdk;
    }

    /**
     * Lấy tên kiểu thiết bị
     */
    public static String getDeviceName() {
        String model = Build.MODEL;
        return model;
    }

    public static String getIMSI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            String IMSI = telephonyManager.getSubscriberId();
            return IMSI;
        }
        return "";
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            String IMEI = telephonyManager.getDeviceId();
            return IMEI;
        }
        return "";
    }

    /**
     * Nhận nhà cung cấp mạng di động
     *
     * @param context
     * @return
     */
    public static String getPhoneISP(Context context) {
        if (context == null) {
            return "";
        }
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String teleCompany = "";
        String np = manager.getNetworkOperator();

        if (np != null) {
            if (np.equals(Viettel_ISP) || np.equals(Viettel2_ISP) || np.equals(Viettel3_ISP)) {
                teleCompany = "Viettel";
            } else if (np.startsWith(Mobi_ISP)) {
                teleCompany = "Mobifone";
            } else if (np.startsWith(Vina_ISP)) {
                teleCompany = "Vinaphone";
            } else if (np.startsWith(SFone_ISP)) {
                teleCompany = "S-Fone Telecom";
            } else if (np.startsWith(Gmobile_ISP)) {
                teleCompany = "Gmobile";
            } else if (np.startsWith(Vietna_ISP)) {
                teleCompany = "Vietna Mobile";
            }
        }
        return teleCompany;
    }

    /**
     * Nhận thông tin màn hình
     *
     * @param context
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm;
    }

    /**
     * Nhận / in thông số trên màn hình
     *
     * @param context
     * @return
     */
    public static DisplayMetrics printDisplayInfo(Context context) {
        DisplayMetrics dm = getDisplayMetrics(context);
        StringBuilder sb = new StringBuilder();
        sb.append("\ndensity         :").append(dm.density);
        sb.append("\ndensityDpi      :").append(dm.densityDpi);
        sb.append("\nheightPixels    :").append(dm.heightPixels);
        sb.append("\nwidthPixels     :").append(dm.widthPixels);
        sb.append("\nscaledDensity   :").append(dm.scaledDensity);
        sb.append("\nxdpi            :").append(dm.xdpi);
        sb.append("\nydpi            :").append(dm.ydpi);
        LogUtils.i(TAG, sb.toString());
        return dm;
    }

    /**
     * Nhận kích thước bộ nhớ hiện có của hệ thống
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static String getAvailMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return Formatter.formatFileSize(context, mi.availMem);// Formar hóa kích thước bộ nhớ thu được
    }


    /**
     * Nhận địa chỉ Mac
     * Cần cấu hình: android.permission.ACCESS_WIFI_STATE
     */
    public static String getMacAddress(Context context) {
        //Địa chỉ wifi mac
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        LogUtils.i(TAG, " MAC：" + mac);
        return mac;
    }

    /**
     * Nhận thời gian khởi động
     */
    public static String getBootTimeString() {
        long ut = SystemClock.elapsedRealtime() / 1000;
        int h = (int) ((ut / 3600));
        int m = (int) ((ut / 60) % 60);
        LogUtils.i(TAG, h + ":" + m);
        return h + ":" + m;
    }
}

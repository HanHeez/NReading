package com.gtv.hanhee.novelreading.Utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;

public class ScreenUtils {


    public static EScreenDensity getDisply(Context context) {
        EScreenDensity eScreenDensity;
        //Khởi tạo độ phân giải màn hình
        DisplayMetrics dm = context.getApplicationContext().getResources().getDisplayMetrics();
        int densityDpi = dm.densityDpi;

        if (densityDpi <= 160) {
            eScreenDensity = EScreenDensity.MDPI;
        } else if (densityDpi <= 240) {
            eScreenDensity = EScreenDensity.HDPI;
        } else if (densityDpi < 400) {
            eScreenDensity = EScreenDensity.XHDPI;
        } else {
            eScreenDensity = EScreenDensity.XXHDPI;
        }
        return eScreenDensity;
    }

    /**
     * Nhận chiều rộng của màn hình
     *
     * @return
     */
    public static int getScreenWidth() {
        return AppUtils.getAppContext().getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * Nhận chiều cao của màn hình
     *
     * @return
     */
    public static int getScreenHeight() {
        return AppUtils.getAppContext().getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * Chuyển dp sang px
     *
     * @param dp
     * @return
     */
    public static float dpToPx(float dp) {
        return dp * AppUtils.getAppContext().getResources().getDisplayMetrics().density;
    }

    public static int dpToPxInt(float dp) {
        return (int) (dpToPx(dp) + 0.5f);
    }

    /**
     * Chuyển px sang dp
     *
     * @param px
     * @return
     */
    public static float pxToDp(float px) {
        return px / AppUtils.getAppContext().getResources().getDisplayMetrics().density;
    }

    public static int pxToDpInt(float px) {
        return (int) (pxToDp(px) + 0.5f);
    }

    /**
     * chuyển giá trị px sang giá trị sp
     *
     * @param pxValue
     * @return
     */
    public static float pxToSp(float pxValue) {
        return pxValue / AppUtils.getAppContext().getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * chuyển sp sang px
     *
     * @param spValue
     * @return
     */
    public static float spToPx(float spValue) {
        return spValue * AppUtils.getAppContext().getResources().getDisplayMetrics().scaledDensity;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            return actionBarHeight;
        }
        return 0;
    }

    /**
     * Màn hình có đang hiển thị theo chiều ngang hay ko
     *
     * @param context context
     * @return boolean
     */
    public static final boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Màn hình có đang hiển thị theo chiều dọc hay ko
     *
     * @param context context
     * @return boolean
     */
    public static final boolean isPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /**
     * Điều chỉnh độ trong suốt của Background 1.0f,0.5f là làm tối
     *
     * @param from    from>=0&&from<=1.0f
     * @param to      to>=0&&to<=1.0f
     * @param context activity hiện tại
     */
    public static void dimBackground(final float from, final float to, AppCompatActivity context) {
        final Window window = context.getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(
                new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        WindowManager.LayoutParams params = window.getAttributes();
                        params.alpha = (Float) animation.getAnimatedValue();
                        window.setAttributes(params);
                    }
                });
        valueAnimator.start();
    }

    /**
     * Nhận chế độ độ sáng của màn hình hiện tại
     *
     * @param mContext
     * @return SCREEN_BRIGHTNESS_MODE_AUTOMATIC : Tự động điều chỉnh độ sáng
     * *       SCREEN_BRIGHTNESS_MODE_MANUAL : độ sáng thủ công
     */
    public static int getScreenMode(Context mContext) {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {

        }
        return screenMode;
    }

    /**
     * Nhận giá trị độ sáng màn hình hiện tại
     *
     * @param mContext
     * @return 0~100
     */
    public static float getScreenBrightness(Context mContext) {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenBrightness / 255.0F * 100;
    }

    /**
     * Đặt chế độ độ sáng cho màn hình
     *
     * @param paramInt SCREEN_BRIGHTNESS_MODE_AUTOMATIC : auto
     *                 SCREEN_BRIGHTNESS_MODE_MANUAL : thủ công
     * @param mContext
     */
    public static void setScreenMode(int paramInt, Context mContext) {
        try {
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Đặt giá trị độ sáng cho màn hinh hiện tại
     *
     * @param paramInt 0~100
     * @param mContext
     */
    public static void saveScreenBrightness(int paramInt, Context mContext) {
        if (paramInt <= 5) {
            paramInt = 5;
        }
        try {
            float f = paramInt / 100.0F * 255;
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Đặt độ sáng của Activity
     *
     * @param paramInt
     * @param mActivity
     */
    public static void setScreenBrightness(int paramInt, AppCompatActivity mActivity) {
        if (paramInt <= 5) {
            paramInt = 5;
        }
        Window localWindow = mActivity.getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 100.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
    }

    private int getStatusBarHeight() {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = AppUtils.getAppContext().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    public enum EScreenDensity {
        XXHDPI,    //Phân giải cực cao      1080×1920
        XHDPI,    //Phân giải siêu cao      720×1280
        HDPI,    //Độ phân giải cao         480×800
        MDPI,    //Độ phân giải trung bình  320×480
    }
}

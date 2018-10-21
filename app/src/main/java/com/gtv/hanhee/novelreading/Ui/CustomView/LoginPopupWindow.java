package com.gtv.hanhee.novelreading.Ui.CustomView;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.gtv.hanhee.novelreading.R;

public class LoginPopupWindow extends PopupWindow implements View.OnTouchListener {

    LoginTypeListener listener;
    private View mContentView;
    private Activity mActivity;
    private ImageView login;
    private ImageView gg;
    private ImageView fb;


    public LoginPopupWindow(Activity activity) {
        mActivity = activity;
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        mContentView = LayoutInflater.from(activity).inflate(R.layout.layout_login_popup_window, null);
        setContentView(mContentView);

        login = (ImageView) mContentView.findViewById(R.id.ivLogin);
        gg = (ImageView) mContentView.findViewById(R.id.ivGoogle);
        fb = (ImageView) mContentView.findViewById(R.id.ivFacebook);

        login.setOnTouchListener(this);
        gg.setOnTouchListener(this);
        fb.setOnTouchListener(this);

        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        setAnimationStyle(R.style.LoginPopup);

        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lighton();
            }
        });
    }

    private void scale(View v, boolean isDown) {
        if (v.getId() == login.getId() || v.getId() == gg.getId() || v.getId() == fb.getId()) {
            if (isDown) {
                Animation testAnim = AnimationUtils.loadAnimation(mActivity, R.anim.scale_down);
                v.startAnimation(testAnim);
            } else {
                Animation testAnim = AnimationUtils.loadAnimation(mActivity, R.anim.scale_up);
                v.startAnimation(testAnim);
            }
        }
        if (!isDown && listener != null) {
            switch (v.getId()) {
                case R.id.ivLogin:
                    listener.onLogin(login, "Login");
                    break;
            }

            login.postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismiss();
                }
            }, 500);

        }
    }

    private void lighton() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mActivity.getWindow().setAttributes(lp);
    }

    private void lightoff() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 0.3f;
        mActivity.getWindow().setAttributes(lp);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        lightoff();
        super.showAsDropDown(anchor, xoff, yoff);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        lightoff();
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                scale(v, true);
                break;
            case MotionEvent.ACTION_UP:
                scale(v, false);
                break;
        }
        return false;
    }

    public void setLoginTypeListener(LoginTypeListener listener) {
        this.listener = listener;
    }

    public interface LoginTypeListener {

        void onLogin(ImageView view, String type);
    }

}

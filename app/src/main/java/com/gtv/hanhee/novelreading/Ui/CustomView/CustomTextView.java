package com.gtv.hanhee.novelreading.Ui.CustomView;/*
Copyright 2012 Aphid Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
 
   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;

public class CustomTextView extends android.support.v7.widget.AppCompatTextView {

    private String txt;

    public CustomTextView(Context context) {
        this(context, null);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTextColor(Color.BLACK);
        setGravity(Gravity.LEFT);
    }

    public String getTxt() {
        return txt;
    }

    public void setNumber(String txt) {
        this.txt = txt;
        setText(String.valueOf(txt));
    }

    @Override
    public String toString() {
        return "NumberTextView: " + txt;
    }
}

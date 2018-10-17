package com.gtv.hanhee.novelreading.Component;


import com.gtv.hanhee.novelreading.Ui.Activity.MainActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface MainActivityComponent {
    MainActivity inject(MainActivity mainActivity);
}

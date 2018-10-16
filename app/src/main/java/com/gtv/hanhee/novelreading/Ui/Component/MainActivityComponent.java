package com.gtv.hanhee.novelreading.Ui.Component;

import com.gtv.hanhee.novelreading.AppComponent;
import com.gtv.hanhee.novelreading.Module.MainActivityModule;
import com.gtv.hanhee.novelreading.Ui.Activity.MainActivity;

import dagger.Component;

@Component(modules = MainActivityModule.class, dependencies = AppComponent.class)
public interface MainActivityComponent {
    MainActivity inject(MainActivity mainActivity);
}

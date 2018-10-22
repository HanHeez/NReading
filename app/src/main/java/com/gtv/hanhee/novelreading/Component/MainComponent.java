package com.gtv.hanhee.novelreading.Component;

import com.gtv.hanhee.novelreading.Ui.Activity.MainActivity;
import com.gtv.hanhee.novelreading.Ui.Activity.SettingActivity;
import com.gtv.hanhee.novelreading.Ui.Activity.WifiBookActivity;
import com.gtv.hanhee.novelreading.Ui.Fragment.RecommendFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface MainComponent {
    MainActivity inject(MainActivity activity);

    RecommendFragment inject(RecommendFragment fragment);

    SettingActivity inject(SettingActivity activity);
    WifiBookActivity inject(WifiBookActivity activity);
}

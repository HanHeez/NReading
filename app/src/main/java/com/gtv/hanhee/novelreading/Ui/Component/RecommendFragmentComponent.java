package com.gtv.hanhee.novelreading.Ui.Component;

import com.gtv.hanhee.novelreading.AppComponent;
import com.gtv.hanhee.novelreading.Fragment.RecommendFragment;
import com.gtv.hanhee.novelreading.Module.RecommendFragmentModule;

import dagger.Component;

@Component(modules = RecommendFragmentModule.class, dependencies = AppComponent.class)
public interface RecommendFragmentComponent {
    RecommendFragment inject(RecommendFragment recommendFragment);
}

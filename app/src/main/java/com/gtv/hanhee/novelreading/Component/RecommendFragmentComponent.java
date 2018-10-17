package com.gtv.hanhee.novelreading.Component;

import com.gtv.hanhee.novelreading.Ui.Fragment.RecommendFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface RecommendFragmentComponent {
    RecommendFragment inject(RecommendFragment recommendFragment);
}

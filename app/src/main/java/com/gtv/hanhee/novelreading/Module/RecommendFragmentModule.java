package com.gtv.hanhee.novelreading.Module;

import com.gtv.hanhee.novelreading.Fragment.RecommendFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class RecommendFragmentModule {

    private RecommendFragment recommendFragment;

    public RecommendFragmentModule(RecommendFragment recommendFragment) {
        this.recommendFragment = recommendFragment;
    }

    @Provides
    RecommendFragment provideRecommendFragment() {
        return recommendFragment;
    }
}

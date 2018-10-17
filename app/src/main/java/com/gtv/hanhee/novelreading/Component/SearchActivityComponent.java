package com.gtv.hanhee.novelreading.Component;

import com.gtv.hanhee.novelreading.Ui.Activity.SearchActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface SearchActivityComponent {
    SearchActivity inject(SearchActivity searchActivity);
}

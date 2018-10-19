package com.gtv.hanhee.novelreading.Component;

import com.gtv.hanhee.novelreading.Ui.Activity.BookDetailActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface BookDetailActivityComponent {
    BookDetailActivity inject(BookDetailActivity bookDetailActivity);
}
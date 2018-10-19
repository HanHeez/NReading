package com.gtv.hanhee.novelreading.Component;

import com.gtv.hanhee.novelreading.Ui.Activity.BookReadActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface BookReadActivityComponent {
    BookReadActivity inject(BookReadActivity bookReadActivity);
}

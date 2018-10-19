package com.gtv.hanhee.novelreading.Component;

import com.gtv.hanhee.novelreading.Ui.Activity.BooksByTagActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface BooksByTagActivityComponent {
    BooksByTagActivity inject(BooksByTagActivity tagBookListActivity);
}

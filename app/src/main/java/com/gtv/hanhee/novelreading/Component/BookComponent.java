package com.gtv.hanhee.novelreading.Component;

import com.gtv.hanhee.novelreading.Ui.Activity.BookDetailActivity;
import com.gtv.hanhee.novelreading.Ui.Activity.BooksByTagActivity;
import com.gtv.hanhee.novelreading.Ui.Activity.ReadActivity;
import com.gtv.hanhee.novelreading.Ui.Activity.SearchActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface BookComponent {
    BookDetailActivity inject(BookDetailActivity activity);

    ReadActivity inject(ReadActivity activity);

//    BookSourceActivity inject(BookSourceActivity activity);

    BooksByTagActivity inject(BooksByTagActivity activity);

    SearchActivity inject(SearchActivity activity);

//    SearchByAuthorActivity inject(SearchByAuthorActivity activity);
//
//    BookDetailReviewFragment inject(BookDetailReviewFragment fragment);
//
//    BookDetailDiscussionFragment inject(BookDetailDiscussionFragment fragment);
}

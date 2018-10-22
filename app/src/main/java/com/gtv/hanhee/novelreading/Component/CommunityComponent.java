package com.gtv.hanhee.novelreading.Component;

import com.gtv.hanhee.novelreading.Ui.Activity.BookDiscussionDetailActivity;
import com.gtv.hanhee.novelreading.Ui.Fragment.BookDiscussionFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface CommunityComponent {

    BookDiscussionFragment inject(BookDiscussionFragment fragment);

    BookDiscussionDetailActivity inject(BookDiscussionDetailActivity activity);

//    BookReviewFragment inject(BookReviewFragment fragment);
//
//    BookReviewDetailActivity inject(BookReviewDetailActivity activity);
//
//    BookHelpFragment inject(BookHelpFragment fragment);
//
//    BookHelpDetailActivity inject(BookHelpDetailActivity activity);
//
//    GirlBookDiscussionFragment inject(GirlBookDiscussionFragment fragment);
}


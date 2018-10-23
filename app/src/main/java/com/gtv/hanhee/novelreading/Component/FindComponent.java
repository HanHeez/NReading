package com.gtv.hanhee.novelreading.Component;

import com.gtv.hanhee.novelreading.Ui.Activity.SubjectBookListDetailActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface FindComponent {

    /** 分类 **/
//    TopCategoryListActivity inject(TopCategoryListActivity activity);
//
//    SubCategoryListActivity inject(SubCategoryListActivity activity);
//
//    SubCategoryFragment inject(SubCategoryFragment fragment);
//
//    /** 排行 **/
//    TopRankActivity inject(TopRankActivity activity);
//
//    SubRankActivity inject(SubRankActivity activity);
//
//    SubOtherHomeRankActivity inject(SubOtherHomeRankActivity activity);
//
//    SubRankFragment inject(SubRankFragment fragment);
//
//    /** 主题书单 **/
//    SubjectBookListActivity inject(SubjectBookListActivity subjectBookListActivity);
//
//    SubjectFragment inject(SubjectFragment subjectFragment);

    SubjectBookListDetailActivity inject(SubjectBookListDetailActivity categoryListActivity);
}

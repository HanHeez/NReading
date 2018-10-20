package com.gtv.hanhee.novelreading.Api;

import com.gtv.hanhee.novelreading.Model.AutoComplete;
import com.gtv.hanhee.novelreading.Model.BookDetail;
import com.gtv.hanhee.novelreading.Model.BookListDetail;
import com.gtv.hanhee.novelreading.Model.BookListTags;
import com.gtv.hanhee.novelreading.Model.BookLists;
import com.gtv.hanhee.novelreading.Model.BookRead;
import com.gtv.hanhee.novelreading.Model.BookSource;
import com.gtv.hanhee.novelreading.Model.BookToc;
import com.gtv.hanhee.novelreading.Model.BooksByCats;
import com.gtv.hanhee.novelreading.Model.BooksByTag;
import com.gtv.hanhee.novelreading.Model.CategoryList;
import com.gtv.hanhee.novelreading.Model.CategoryListLv2;
import com.gtv.hanhee.novelreading.Model.ChapterRead;
import com.gtv.hanhee.novelreading.Model.HotReview;
import com.gtv.hanhee.novelreading.Model.HotWord;
import com.gtv.hanhee.novelreading.Model.PostCount;
import com.gtv.hanhee.novelreading.Model.RankingList;
import com.gtv.hanhee.novelreading.Model.Rankings;
import com.gtv.hanhee.novelreading.Model.Recommend;
import com.gtv.hanhee.novelreading.Model.RecommendBookList;
import com.gtv.hanhee.novelreading.Model.SearchDetail;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ReaderApiService {


    @GET("/book/recommend")
    Observable<Recommend> getRecomend(@Query("gender") String gender);

    @GET("/atoc")
    Observable<List<BookSource>> getBookSource(@Query("view") String view, @Query("book") String book);

    @GET("/mix-toc/{bookId}")
    Observable<BookRead> getBookRead(@Path("bookId") String bookId);

    @GET("/mix-atoc/{bookId}")
    Observable<BookToc> getBookToc(@Path("bookId") String bookId, @Query("view") String view);

    @GET("/btoc/{bookId}")
    Observable<BookToc> getBookBToc(@Path("bookId") String bookId, @Query("view") String view);

    @GET("http://chapter2.zhuishushenqi.com/chapter/{url}")
    Observable<ChapterRead> getChapterRead(@Path("url") String url);

    @GET("/post/post-count-by-book")
    Observable<PostCount> postCountByBook(@Query("bookId") String bookId);

    @GET("/post/total-count")
    Observable<PostCount> postTotalCount(@Query("books") String bookId);

    @GET("/book/hot-word")
    Observable<HotWord> getHotWord();

    @GET("/book/auto-complete")
    Observable<AutoComplete> autoComplete(@Query("query") String query);

    @GET("/book/fuzzy-search")
    Observable<SearchDetail> searchBooks(@Query("query") String query);

    @GET("/post/review/best-by-book")
    Observable<HotReview> getHotReview(@Query("book") String book);

    @GET("/book-list/{bookId}/recommend")
    Observable<RecommendBookList> getRecommendBookList(@Path("bookId") String bookId, @Query("limit") String limit);

    @GET("/book/{bookId}")
    Observable<BookDetail> getBookDetail(@Path("bookId") String bookId);

    @GET("/book/by-tags")
    Observable<BooksByTag> getBooksByTag(@Query("tags") String tags, @Query("start") String start, @Query("limit") String limit);

    /**
     * 获取所有排行榜
     * @return
     */
    @GET("/ranking/gender")
    Observable<RankingList> getRanking();

    /**
     * 获取单一排行榜
     * 周榜：rankingId->_id
     * 月榜：rankingId->monthRank
     * 总榜：rankingId->totalRank
     * @return
     */
    @GET("/ranking/{rankingId}")
    Observable<Rankings> getRanking(@Path("rankingId") String rankingId);

    /**
     * 获取主题书单列表
     * 本周最热：duration=last-seven-days&sort=collectorCount
     * 最新发布：duration=all&sort=created
     * 最多收藏：duration=all&sort=collectorCount
     * @param tag   都市、古代、架空、重生、玄幻、网游
     * @param gender   male、female
     * @param limit 20
     * @return
     */
    @GET("/book-list")
    Observable<BookLists> getBookLists(@Query("duration") String duration, @Query("sort") String sort, @Query("start") String start, @Query("limit") String limit,@Query("tag") String tag,@Query("gender") String gender);

    /**
     * 获取主题书单标签列表
     * @return
     */
    @GET("/book-list/tagType")
    Observable<BookListTags> getBookListTags();

    /**
     * 获取书单详情
     * @return
     */
    @GET("/book-list/{bookListId}")
    Observable<BookListDetail> getBookListDetail(@Path("bookListId") String bookListId);

    /**
     * 获取分类
     * @return
     */
    @GET("/cats/lv2/statistics")
    Observable<CategoryList> getCategoryList();

    /**
     * 获取二级分类
     * @return
     */
    @GET("/cats/lv2")
    Observable<CategoryListLv2> getCategoryListLv2();

    /**
     * 按分类获取书籍列表
     * @param gender male、female
     * @param type hot(热门)、new(新书)、reputation(好评)、over(完结)
     * @param major 玄幻
     * @param minor 东方玄幻、异界大陆、异界争霸、远古神话
     * @param limit 50
     * @return
     */
    @GET("/book/by-categories")
    Observable<BooksByCats> getBooksByCats(@Query("gender") String gender, @Query("type") String type, @Query("major") String major, @Query("minor") String minor, @Query("start") String start, @Query("limit") String limit);

}
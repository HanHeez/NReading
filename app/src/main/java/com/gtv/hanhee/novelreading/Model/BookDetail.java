package com.gtv.hanhee.novelreading.Model;

import java.io.Serializable;
import java.util.List;

public class BookDetail implements Serializable {


    /**
     * _id : 525253d094336b3155000dd8
     * author : w风雪
     * banned : 0
     * cover : /agent/http://image.cmfu.com/books/2797907/2797907.jpg
     * creater : iPhone 5 (GSM+CDMA)
     * dramaPoint : null
     * followerCount : 35
     * gradeCount : 0
     * isSerial : true
     * lastChapter : 请安装【追书神器】，本应用已停用
     * latelyFollower : 2385
     * longIntro : 您当前所使用的软件已改名为【追书神器】。
     * 请搜索“追书神器”下载安装最新版【追书神器】。
     * 无广告；不闪退；章节更新自动通知。
     * postCount : 111
     * serializeWordCount : 4614
     * site : zhuishuvip
     * tags : ["热血","洪荒封神","洪荒","架空","修炼","仙侠"]
     * title : 洪荒造化
     * tocs : ["525253d194336b3155000dd9","525253e6a4e35219120006a6","525253e6a4e35219120006a7","525253e6a4e35219120006a8","525253e6a4e35219120006a9","525253e6a4e35219120006ab","52c65225c1988af227000251","52c68d2cf91d99312b000d92","52c690bc0f3d8bda2b000873","532cf9dd39493253790020f4"]
     * totalPoint : null
     * type : wxxx
     * updated : 2016-04-03T13:48:05.907Z
     * writingPoint : null
     * hasNotice : false
     * tagStuck : 0
     * chaptersCount : 1294
     * tocCount : 15
     * tocUpdated : 2016-03-25T21:03:42.962Z
     * retentionRatio : 20.56
     * hasCmread : true
     * thirdFlagsUpdated : 2014-09-01T06:01:24.259Z
     * categories : ["洪荒封神","仙侠"]
     * wordCount : 5947980
     * cat : 仙侠
     * gender : ["male"]
     * majorCate : 仙侠
     * minorCate : 洪荒封神
     * reviewCount : 9
     * monthFollower : {"11":2170}
     * totalFollower : 2170
     * cpOnly : false
     * hasCp : true
     * _le : false
     */

    public String _id;
    public String author;
    public String authorTrans;
    public int banned;
    public String cover;
    public String creater;
    public Object dramaPoint;
    public int followerCount;
    public int gradeCount;
    public boolean isSerial;
    public String lastChapter;
    public String lastChapterTrans;
    public int latelyFollower;
    public String longIntro;
    public String longIntroTrans;
    public int postCount; // 社区帖子数
    public int serializeWordCount;
    public String site;
    public String title;
    public String titleTrans;
    public Object totalPoint;
    public String type;
    public String updated;
    public Object writingPoint;
    public boolean hasNotice;
    public int tagStuck;
    public int chaptersCount;
    public int tocCount;
    public String tocUpdated;
    public double retentionRatio;
    public boolean hasCmread;
    public String thirdFlagsUpdated;
    public int wordCount;
    public String cat;
    public String catTrans;

    public String getCatTrans() {
        return catTrans;
    }

    public void setCatTrans(String catTrans) {
        this.catTrans = catTrans;
    }

    public String majorCate;
    public String majorCateTrans;
    public String minorCate;
    public String minorCateTrans;
    public int reviewCount;
    public int totalFollower;
    public boolean cpOnly;
    public boolean hasCp;
    public boolean _le;
    public List<String> tags;
    public List<String> tocs;
    public List<String> categories;
    public List<String> gender;

    public static class combineCategories {
        String category;
        String categoryTrans;

        public combineCategories(String category, String categoryTrans) {
            this.category = category;
            this.categoryTrans = categoryTrans;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getCategoryTrans() {
            return categoryTrans;
        }

        public void setCategoryTrans(String categoryTrans) {
            this.categoryTrans = categoryTrans;
        }
    }

    public static class combineTags {
        String tag;
        String tagTrans;

        public combineTags(String tag, String tagTrans) {
            this.tag = tag;
            this.tagTrans = tagTrans;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getTagTrans() {
            return tagTrans;
        }

        public void setTagTrans(String tagTrans) {
            this.tagTrans = tagTrans;
        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorTrans() {
        return authorTrans;
    }

    public void setAuthorTrans(String authorTrans) {
        this.authorTrans = authorTrans;
    }

    public int getBanned() {
        return banned;
    }

    public void setBanned(int banned) {
        this.banned = banned;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Object getDramaPoint() {
        return dramaPoint;
    }

    public void setDramaPoint(Object dramaPoint) {
        this.dramaPoint = dramaPoint;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public int getGradeCount() {
        return gradeCount;
    }

    public void setGradeCount(int gradeCount) {
        this.gradeCount = gradeCount;
    }

    public boolean isSerial() {
        return isSerial;
    }

    public void setSerial(boolean serial) {
        isSerial = serial;
    }

    public String getLastChapter() {
        return lastChapter;
    }

    public void setLastChapter(String lastChapter) {
        this.lastChapter = lastChapter;
    }

    public String getLastChapterTrans() {
        return lastChapterTrans;
    }

    public void setLastChapterTrans(String lastChapterTrans) {
        this.lastChapterTrans = lastChapterTrans;
    }

    public int getLatelyFollower() {
        return latelyFollower;
    }

    public void setLatelyFollower(int latelyFollower) {
        this.latelyFollower = latelyFollower;
    }

    public String getLongIntro() {
        return longIntro;
    }

    public void setLongIntro(String longIntro) {
        this.longIntro = longIntro;
    }

    public String getLongIntroTrans() {
        return longIntroTrans;
    }

    public void setLongIntroTrans(String longIntroTrans) {
        this.longIntroTrans = longIntroTrans;
    }

    public int getPostCount() {
        return postCount;
    }

    public void setPostCount(int postCount) {
        this.postCount = postCount;
    }

    public int getSerializeWordCount() {
        return serializeWordCount;
    }

    public void setSerializeWordCount(int serializeWordCount) {
        this.serializeWordCount = serializeWordCount;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleTrans() {
        return titleTrans;
    }

    public void setTitleTrans(String titleTrans) {
        this.titleTrans = titleTrans;
    }

    public Object getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Object totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public Object getWritingPoint() {
        return writingPoint;
    }

    public void setWritingPoint(Object writingPoint) {
        this.writingPoint = writingPoint;
    }

    public boolean isHasNotice() {
        return hasNotice;
    }

    public void setHasNotice(boolean hasNotice) {
        this.hasNotice = hasNotice;
    }

    public int getTagStuck() {
        return tagStuck;
    }

    public void setTagStuck(int tagStuck) {
        this.tagStuck = tagStuck;
    }

    public int getChaptersCount() {
        return chaptersCount;
    }

    public void setChaptersCount(int chaptersCount) {
        this.chaptersCount = chaptersCount;
    }

    public int getTocCount() {
        return tocCount;
    }

    public void setTocCount(int tocCount) {
        this.tocCount = tocCount;
    }

    public String getTocUpdated() {
        return tocUpdated;
    }

    public void setTocUpdated(String tocUpdated) {
        this.tocUpdated = tocUpdated;
    }

    public double getRetentionRatio() {
        return retentionRatio;
    }

    public void setRetentionRatio(double retentionRatio) {
        this.retentionRatio = retentionRatio;
    }

    public boolean isHasCmread() {
        return hasCmread;
    }

    public void setHasCmread(boolean hasCmread) {
        this.hasCmread = hasCmread;
    }

    public String getThirdFlagsUpdated() {
        return thirdFlagsUpdated;
    }

    public void setThirdFlagsUpdated(String thirdFlagsUpdated) {
        this.thirdFlagsUpdated = thirdFlagsUpdated;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getMajorCate() {
        return majorCate;
    }

    public void setMajorCate(String majorCate) {
        this.majorCate = majorCate;
    }

    public String getMajorCateTrans() {
        return majorCateTrans;
    }

    public void setMajorCateTrans(String majorCateTrans) {
        this.majorCateTrans = majorCateTrans;
    }

    public String getMinorCate() {
        return minorCate;
    }

    public void setMinorCate(String minorCate) {
        this.minorCate = minorCate;
    }

    public String getMinorCateTrans() {
        return minorCateTrans;
    }

    public void setMinorCateTrans(String minorCateTrans) {
        this.minorCateTrans = minorCateTrans;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public int getTotalFollower() {
        return totalFollower;
    }

    public void setTotalFollower(int totalFollower) {
        this.totalFollower = totalFollower;
    }

    public boolean isCpOnly() {
        return cpOnly;
    }

    public void setCpOnly(boolean cpOnly) {
        this.cpOnly = cpOnly;
    }

    public boolean isHasCp() {
        return hasCp;
    }

    public void setHasCp(boolean hasCp) {
        this.hasCp = hasCp;
    }

    public boolean is_le() {
        return _le;
    }

    public void set_le(boolean _le) {
        this._le = _le;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }


    public List<String> getTocs() {
        return tocs;
    }

    public void setTocs(List<String> tocs) {
        this.tocs = tocs;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }



    public List<String> getGender() {
        return gender;
    }

    public void setGender(List<String> gender) {
        this.gender = gender;
    }
}

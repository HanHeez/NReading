package com.gtv.hanhee.novelreading.Utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.gtv.hanhee.novelreading.Model.ChapterRead;

import org.apache.commons.collections4.map.LRUMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class BookPageFactory {

    private static LRUMap<String, ArrayList<String>> cache = new LRUMap<>(10);
    private int mWidth;
    private int mHeight;
    private int mMarginWidth = 10; //
    private int mMarginHeight = 10; //
    private float mVisibleHeight; // chiều cao nội dung
    private float mVisibleWidth; // chiều cao nội dung
    private Paint mPaint;
    private int mFontSize = 16; //dp
    private float mFontSizePx;
    private int mTextColor = Color.LTGRAY;
    private int mLineCount = 0; // số hàng trên mỗi trang
    private int mLineWordCount = 0; // số từ trên mỗi dòng
    private String bookId;
    private String basePath = FileUtils.createRootPath(AppUtils.getAppContext()) + "/book/";

    //id sách + chiều cao của dòng
    public BookPageFactory(String bookId, int lineHeight) {
        this.bookId = bookId;
        mWidth = ScreenUtils.getScreenWidth();
        mHeight = ScreenUtils.getScreenHeight();

        mVisibleWidth = mWidth - ScreenUtils.dpToPx(mMarginWidth) * 2;
        mVisibleHeight = mHeight - ScreenUtils.dpToPx(mMarginHeight) * 2 - ScreenUtils.getStatusBarHeight(AppUtils.getAppContext());

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(mFontSize);
        mPaint.setColor(mTextColor);

        mFontSizePx = ScreenUtils.dpToPx(mFontSize);
        mLineWordCount = (int) (mVisibleWidth / mFontSizePx);
        mLineCount = (int) (mVisibleHeight / lineHeight); // Số dòng có thể được hiển thị

        LogUtils.e("mLineCount = " + mLineCount);
        LogUtils.e("mFontSizePx = " + mFontSizePx);
        LogUtils.e("mLineWordCount = " + mLineWordCount);
        LogUtils.e("mVisibleWidth = " + mVisibleWidth);
        LogUtils.e("mVisibleHeight = " + mVisibleHeight);

    }

    public File getBookFile(int chapter) {
        File file = new File(basePath + bookId + "/" + chapter + ".txt");
        if (!file.exists())
            FileUtils.createFile(file);
        return file;
    }

    /**
     * Ghi chép dữ liệu của chương vào File
     *
     * @param chapter
     * @param chapterNo
     */
    public void append(final ChapterRead.Chapter chapter, int chapterNo) {
        File file = getBookFile(chapterNo);
        FileUtils.writeFile(file.getAbsolutePath(), chapter.title + "\n" + chapter.body, true);
    }

    /**
     * Có lưu phân trang vào Cache hay ko
     *
     * @param chapter
     * @return
     */
    public boolean hasCache(int chapter) {
        ArrayList<String> chapterCache = cache.get(bookId + "-" + chapter);
        return chapterCache != null && chapterCache.size() > 0;
    }

    /**
     * Đọc bài viết và xử lý trang. Tăng khóa chuỗi để tránh phân trang bài viết cùng một lúc
     *
     * @param chapter
     * @return
     */
    public synchronized ArrayList<String> readPage(int chapter) {
        //lấy dữ liệu split trong cache bằng cách gửi Id của truyện - chapter
        ArrayList<String> split = cache.get(bookId + "-" + chapter);
        if (split != null && split.size() > 0) {
            LogUtils.d(bookId + "-" + chapter + ": from cache");
            return split;
        }
        String temp = readTxt(chapter);
        try {
            split = split(temp, mLineWordCount * 2, "GBK");
            cache.put(bookId + "-" + chapter, split);
            LogUtils.d(bookId + "-" + chapter + ": add cache");
            return split;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Đọc nội dung Collection
     */
    public String readTxt(int chapter) {
        String temp = "";

        BufferedReader bufferedReader = null;
        File txtFile = getBookFile(chapter);
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(txtFile), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Lỗi mã hóa
            LogUtils.e("UnsupportedEncodingException:" + e.toString());
            return null;
        } catch (FileNotFoundException e) {
            // TODO Lỗi ko tìm thấy
            LogUtils.e("FileNotFoundException:" + e.toString());
            return null;
        }

        String line = "";
        try {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                temp += line + "\n";
            }
        } catch (IOException e) {
            LogUtils.e("IOException:" + e.toString());
            return null;
        }
        return temp;
    }


    /**
     * Xử lý phân trang
     *
     * @param text
     * @param length
     * @param encoding
     * @return
     * @throws UnsupportedEncodingException
     */


    public ArrayList<String> split(String text, int length, String encoding) throws UnsupportedEncodingException {
        ArrayList<String> texts = new ArrayList();
        String temp = "    ";
        String c;
        int lines = 0;
        int pos = 2;
        int startInd = 0;
        for (int i = 0; text != null && i < text.length(); ) {
            byte[] b = String.valueOf(text.charAt(i)).getBytes(encoding);
            pos += b.length;
            if (pos >= length) {
                int endInd;
                if (pos == length) {
                    endInd = ++i;
                } else {
                    endInd = i;
                }
                temp += text.substring(startInd, endInd); // add vào 1 dòng
                lines++;
                if (lines >= mLineCount) { // vượt quá 1 trang
                    texts.add(temp); // add vào
                    temp = "";
                    lines = 0;
                }
                pos = 0;
                startInd = i;
            } else {
                c = new String(b, encoding);
                if (c.equals("\n")) {
                    temp += text.substring(startInd, i + 1);
                    lines++;
                    if (lines >= mLineCount) {
                        texts.add(temp);
                        temp = "";
                        lines = 0;
                    }
                    temp += "    ";
                    pos = 2;
                    startInd = i + 1;
                }
                i++;
            }
        }
        if (startInd < text.length()) {
            temp += text.substring(startInd);
            lines++;
        }
        if (!TextUtils.isEmpty(temp))
            texts.add(temp);
        return texts;
    }
}

package com.gtv.hanhee.novelreading.Model;


public class DownloadProgress {

    public String bookId;

    public int progress;

    public DownloadProgress(String bookId, int progress) {
        this.bookId = bookId;
        this.progress = progress;
    }
}

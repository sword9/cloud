package com.hochan.sqlite.data;

/**
 * Created by Administrator on 2016/7/18.
 */
public class ThreadInfo {
    private int id;
    private String fileName;
    private long start;
    private long end;
    private int upload; //值为1的时候上传，为0的时候下载
    private long finish;

    public ThreadInfo() {
    }

    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public ThreadInfo(int id, String fileName, long start, long end, int upload, long finish) {
        this.end = end;
        this.fileName = fileName;
        this.upload = upload;
        this.finish = finish;
        this.id = id;
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFinish() {
        return finish;
    }

    public void setFinish(long finish) {
        this.finish = finish;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }
}

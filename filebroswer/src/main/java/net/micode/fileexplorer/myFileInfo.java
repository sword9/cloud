package net.micode.fileexplorer;

/**
 * Created by Administrator on 2016/7/24.
 */

import java.io.Serializable;

/**
 * 用于下载
 * Created by Administrator on 2016/7/12.
 */
public class myFileInfo implements Serializable {
    private int id;
    private boolean isDir;
    private String path; //若为云端文件，则为云端文件2路径，否则为sd卡中的文件路径
    private String fileName;
    private long length;
    private long finish;

    public myFileInfo() {

    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public myFileInfo(String path, String fileName, long finish, int id, boolean isDir, long length) {
        this.path = path;
        this.fileName = fileName;
        this.finish = finish;
        this.id = id;
        this.isDir = isDir;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
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

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FileInfo{");
        sb.append("id=").append(id);
        sb.append(", isDir='").append(isDir);
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append(", length=").append(length);
        sb.append(", finish=").append(finish);
        sb.append('}');
        return sb.toString();
    }
}

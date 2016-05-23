package com.hochan.multi_file_selector.data;


/**
 * Created by Administrator on 2016/5/20.
 */
public class NoneMediaFile extends File{

    public final static int TYPE_TXT = 10;
    public final static int TYPE_PDF = 11;
    public final static int TYPE_DOCX = 12;
    public final static int TYPE_XLS = 13;

    public NoneMediaFile(int mType, String mName, String mPath, String mDataAdded, long mSzie) {
        super(mType, mName, mPath, mDataAdded, mSzie);
    }
}

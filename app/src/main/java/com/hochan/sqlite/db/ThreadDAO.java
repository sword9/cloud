package com.hochan.sqlite.db;

import com.hochan.sqlite.data.ThreadInfo;

import java.util.List;

/**
 * Created by Administrator on 2016/7/18.
 * 数据访问接口
 */
public interface ThreadDAO {
    /**
     * 往数据库插入线程信息
     */

    void insertThread(ThreadInfo threadInfo);

    /**
     * 往数据库删除线程信息
     */
    void deleteThread(String filename, int upload);

    /**
     * 更新线程信息
     * finished为完成进度
     */
    void updateThread(String filename, int thread_id, long finished, int upload);

    /**
     * 查询文件的线程信息
     */
    List<ThreadInfo> getThread(String filename, int upload);

    /**
     * 判断线程是否存在
     */
    boolean isExists(String filename, int thread_id);
}

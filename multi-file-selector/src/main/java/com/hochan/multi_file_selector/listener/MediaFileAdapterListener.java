package com.hochan.multi_file_selector.listener;

import java.io.File;

/**
 * Created by Administrator on 2016/5/17.
 */
public interface MediaFileAdapterListener {

    public void fileSelected(int selectedCount);

    public void recordFolder(File file);
}

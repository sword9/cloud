package com.hochan.multi_file_selector;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.hochan.multi_file_selector.data.File;

public class MultiFileSelectorActivity extends AppCompatActivity {

    public final static String TYPE_SELECT = "type_select";

    private int mSelectType = 0;

    private Toolbar mToolbar;
    private Button btnFolders;
    private MultiFileSelectorFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_file_selector);

        mSelectType = getIntent().getIntExtra(TYPE_SELECT, 0);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        btnFolders = (Button) findViewById(R.id.btn_folders);
        Bundle bundle = new Bundle();
        switch (mSelectType){
            case File.TYPE_IMAGE:
                mToolbar.setTitle("选择图片");
                bundle.putInt(TYPE_SELECT, File.TYPE_IMAGE);
                break;
            case File.TYPE_AUDIO:
                mToolbar.setTitle("选择音乐");
                bundle.putInt(TYPE_SELECT, File.TYPE_AUDIO);
                break;
            case File.TYPE_VIDEO:
                mToolbar.setTitle("选择视频");
                bundle.putInt(TYPE_SELECT, File.TYPE_VIDEO);
                break;
            case File.TYPE_MEDIANONE:
                mToolbar.setTitle("选择文档");
                bundle.putInt(TYPE_SELECT, File.TYPE_MEDIANONE);
                break;
            case File.TYPE_ALL:
                mToolbar.setTitle("选择文件");
                bundle.putInt(TYPE_SELECT, File.TYPE_ALL);
                btnFolders.setVisibility(View.GONE);
                break;
        }

        mFragment = (MultiFileSelectorFragment) Fragment.instantiate(this,
                MultiFileSelectorFragment.class.getName(),
                bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.rl_content,
                mFragment).commit();


        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFolders.setText("所有"+ File.TYPE_NAME.get(mSelectType));
        btnFolders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragment.showFolderPopupWindow(mToolbar);
            }
        });
    }

    public void setFolderName(String folder){
        btnFolders.setText(folder);
    }
}

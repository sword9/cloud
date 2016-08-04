package com.hochan.sqlite;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.hochan.multi_file_selector.MultiFileSelectorActivity;
import com.hochan.multi_file_selector.MultiFileSelectorFragment;
import com.hochan.multi_file_selector.data.File;
import com.hochan.sqlite.fragment.FileFragment;
import com.hochan.sqlite.service.UpLoadService;
import com.hochan.sqlite.tools.SQLHttpClient;
import com.hochan.sqlite.tools.ScreenTools;
import com.hochan.sqlite.tools.Tool;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class FileActivity extends AppCompatActivity {

    private Button btnUpload;
    private ListPopupWindow mListPopupWindow;
    private RecyclerView recyDownload;
    private ArrayList<String> mUploadFileType;
    private ArrayAdapter<String> mFileTypeAdater;

    private FileFragment mFileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_backarrow));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyDownload = (RecyclerView) findViewById(R.id.recy_download);

        btnUpload = (Button) findViewById(R.id.btn_upload_files);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListPopupWindow == null) {
                    System.out.println("ListPopupWindow is null!");
                    createPopupWindow();
                }

                if(mListPopupWindow.isShowing()) {
                    System.out.println("隐藏窗口");
                    mListPopupWindow.dismiss();
                }
                else {
                    System.out.println("显示窗口");
                    mListPopupWindow.show();
                }

            }
        });

        mFileFragment = (FileFragment) getSupportFragmentManager()
                .findFragmentById(R.id.rl_container);
        if(mFileFragment == null){
            mFileFragment = FileFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.rl_container, mFileFragment).commit();
        }
    }

    private void createPopupWindow(){
        System.out.println("创建弹出窗口");
        int width  = ScreenTools.getScreenWidth(this);


        mListPopupWindow = new ListPopupWindow(this);
        mListPopupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorBackground)));
        mListPopupWindow.setContentWidth(width);
        mListPopupWindow.setWidth(width);
        mListPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mListPopupWindow.setAnchorView(btnUpload);
        mListPopupWindow.setModal(true);
        mUploadFileType = new ArrayList<>();
        mUploadFileType.add("图片");
        mUploadFileType.add("音频");
        mUploadFileType.add("视频");
        mUploadFileType.add("文本");
        mUploadFileType.add("全部");
        mFileTypeAdater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mUploadFileType);
        mListPopupWindow.setAdapter(mFileTypeAdater);
        mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FileActivity.this, MultiFileSelectorActivity.class);
                mListPopupWindow.dismiss();
                switch (position){
                    case 0:
                        intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, File.TYPE_IMAGE);
                        startActivityForResult(intent, File.TYPE_IMAGE);
                        break;
                    case 1:
                        intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, File.TYPE_AUDIO);
                        startActivityForResult(intent, File.TYPE_AUDIO);
                        break;
                    case 2:
                        intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, File.TYPE_VIDEO);
                        startActivityForResult(intent, File.TYPE_VIDEO);
                        break;
                    case 3:
                        intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, File.TYPE_MEDIANONE);
                        startActivityForResult(intent, File.TYPE_MEDIANONE);
                        break;
                    case 4:
                        intent.putExtra(MultiFileSelectorActivity.TYPE_SELECT, File.TYPE_ALL);
                        startActivityForResult(intent, File.TYPE_ALL);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK)
        {
            ArrayList<String> stringList = new ArrayList<String>();
            stringList = data.getStringArrayListExtra(MultiFileSelectorFragment.EXTRA_RESULT);
            //耗时操作：启动上传service
            Intent intent = new Intent(this, UpLoadService.class);
            intent.putStringArrayListExtra(MultiFileSelectorFragment.EXTRA_RESULT,stringList);
            Toast.makeText(this,"准备启动上传",Toast.LENGTH_SHORT).show();
            this.startService(intent);
            Toast.makeText(this,"已启动上传",Toast.LENGTH_SHORT).show();
        }
    }
}

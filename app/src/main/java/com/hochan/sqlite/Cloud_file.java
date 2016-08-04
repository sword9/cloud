package com.hochan.sqlite;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hochan.sqlite.adapter.fileExploreAdapter;
import com.hochan.sqlite.data.FileInfo;
import com.hochan.sqlite.tools.SQLHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class Cloud_file extends AppCompatActivity {

    private Context mContext;
    private RecyclerView recycler;
    private List<FileInfo> dataset;
    private fileExploreAdapter mAdapter;
    private String localFile;  //当前想要获取下一步的文件名
    Button test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.fileexplore);
        mContext = this;

        *//*recycler = (RecyclerView) findViewById(R.id.file_recyclerview);

        dataset = createdata(mContext, "/");

        recycler.setLayoutManager((new LinearLayoutManager(mContext)));
        mAdapter = new fileExploreAdapter(dataset,mContext);
        recycler.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((new fileExploreAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data) {
                createdata(mContext, data);
            }
        }));*/


        //test
        setContentView(R.layout.test);
        test = (Button) findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataset = createdata(getApplication(),"/");
            }
        });
    }
    //更新所显示的云端文件
    public List<FileInfo> createdata(Context ctx, String fileName) {
        List<FileInfo> data = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject();
            /*jsonObject.put("userId",MainActivity.localWorker.getmID());
            jsonObject.put("path",localFile);*/
            //test
            jsonObject.put("userId","123456");
            jsonObject.put("path",fileName);

            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            SQLHttpClient.post(getApplicationContext(), SQLHttpClient.BASE_URL+"/listAll", stringEntity, RequestParams.APPLICATION_JSON,
                    new JsonHttpResponseHandler(){

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);

                            try {
                               /* JSONObject jsonObject1 = response.getJSONObject(0);*/

                                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                                Log.d("response0",response.getString("directory"));
                                Log.d("response1",response.getString("file"));
                                Log.d("response",response.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Toast.makeText(getApplicationContext(),"获取文件失败",Toast.LENGTH_SHORT).show();
                        }

                    });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return data;
    }

}

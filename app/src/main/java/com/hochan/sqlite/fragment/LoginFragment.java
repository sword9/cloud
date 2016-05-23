package com.hochan.sqlite.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hochan.sqlite.R;
import com.hochan.sqlite.data.Worker;
import com.hochan.sqlite.sql.DataHelper;
import com.hochan.sqlite.tools.MyApplication;
import com.hochan.sqlite.tools.SQLHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.ContentType;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by Administrator on 2016/5/5.
 */
public class LoginFragment extends DialogFragment implements View.OnClickListener{

    private LinearLayout llLogin, llProgress;
    private View mView;
    private Context mContext;
    private Button btnLogin;
    private EditText edName, edPassword, edUrl;
    private String mTag;
    private TextView tvProcess, tvTitle;

    public static final String TAG_LOGIN = "login";
    public static final String TAG_SYNC = "sync";

    private static final String TYPE = "type";

    public static LoginFragment newInstance(String type){
        LoginFragment loginFragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TYPE, type);
        loginFragment.setArguments(bundle);
        return loginFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.login_layout, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//注意此处
        getDialog().getWindow().setLayout((int) (MyApplication.mWidthOfDialog), WindowManager.LayoutParams.WRAP_CONTENT);//这2行,和上面的一样,注意顺序就行;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView = getView();
        mContext = getActivity();
        llLogin = (LinearLayout) mView.findViewById(R.id.ll_login);
        llProgress = (LinearLayout) mView.findViewById(R.id.ll_progress);
        mTag = getArguments().getString(TYPE);
        tvProcess = (TextView) mView.findViewById(R.id.tv_prosess);
        tvTitle = (TextView) mView.findViewById(R.id.tv_title);
        if(mTag.equals(TAG_SYNC)) {
            llLogin.setVisibility(View.GONE);
            llProgress.setVisibility(View.VISIBLE);
            tvTitle.setText("同步所有数据");
            tvProcess.setText("正在同步...");
            setCancelable(false);
            doSync();
        }else if(mTag.equals(TAG_LOGIN)){
            tvTitle.setText("获取个人基本数据");
            llLogin.setVisibility(View.VISIBLE);
            llProgress.setVisibility(View.GONE);
            btnLogin = (Button) mView.findViewById(R.id.btn_login);
            btnLogin.setOnClickListener(this);
            edName = (EditText) mView.findViewById(R.id.ed_name);
            edPassword = (EditText) mView.findViewById(R.id.ed_password);
            edUrl = (EditText) mView.findViewById(R.id.ed_url);
        }
    }

    //增量同步数据库
    private void doSync() {
        DataHelper tmpDataHelper = new DataHelper(mContext);
        Map<String, String> timeStamps = new HashMap<>();
        timeStamps = tmpDataHelper.getTimeStamp();
        ObjectMapper mapper = new ObjectMapper();
        String str = "";
        try {
            str = mapper.writeValueAsString(timeStamps);
            System.out.println(str);
            StringEntity stringEntity = new StringEntity(str, ContentType.APPLICATION_JSON);
            SQLHttpClient.post(mContext, SQLHttpClient.BASE_URL, stringEntity, RequestParams.APPLICATION_JSON,
                    new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                        }
                    });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if(TextUtils.isEmpty(edName.getText())){
            Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(edPassword.getText())){
            Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(edUrl.getText())){
            Toast.makeText(mContext, "请输入Url", Toast.LENGTH_SHORT).show();
            return;
        }
        setCancelable(false);
        llLogin.setVisibility(View.GONE);
        llProgress.setVisibility(View.VISIBLE);
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", edName.getText().toString());
            jsonObject.put("password", edPassword.getText().toString());
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            SQLHttpClient.post(mContext, edUrl.getText().toString(), stringEntity, RequestParams.APPLICATION_JSON,
                    new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                            super.onFailure(statusCode, headers, responseString, throwable);
                            String str = SQLHttpClient.handleFailure(statusCode, headers, responseString, throwable);
                            Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}

package com.hochan.sqlite;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hochan.sqlite.data.Worker;
import com.hochan.sqlite.sql.DataHelper;

import java.util.Date;

public class AddActivity extends AppCompatActivity {

    private EditText edName, edPhoneNumber, edTowerNumber, edWorkState;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(null);
        setSupportActionBar(toolbar);

        edName = (EditText) findViewById(R.id.ed_name);
        edPhoneNumber = (EditText) findViewById(R.id.ed_phone_number);
        edTowerNumber = (EditText) findViewById(R.id.ed_tower_number);
        edWorkState = (EditText) findViewById(R.id.ed_work_state);

        btnAdd = (Button) findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(edName.getText())){
                    Toast.makeText(getApplicationContext(), "请输入姓名", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(edPhoneNumber.getText())){
                    Toast.makeText(getApplicationContext(), "请输入电话", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(edTowerNumber.getText())){
                    Toast.makeText(getApplicationContext(), "请输入电塔编号", Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(edWorkState.getText())){
                    Toast.makeText(getApplicationContext(), "请输入工作状况", Toast.LENGTH_LONG).show();
                    return;
                }
                DataHelper dataHelper = new DataHelper(getApplicationContext());
                Worker worker = new Worker();
                worker.setmName(edName.getText().toString());
                worker.setmPhoneNumber(edPhoneNumber.getText().toString());
                worker.setmTowerNumber(edTowerNumber.getText().toString());
                worker.setmWorkState(edWorkState.getText().toString());
                Long result = dataHelper.addData(worker);
                if(result != -1){
                    Toast.makeText(getApplicationContext(), "添加成功："+String.valueOf(result), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

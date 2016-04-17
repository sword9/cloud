package com.hochan.sqlite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.hochan.sqlite.data.Worker;
import com.hochan.sqlite.fragment.EditDialogFragment;
import com.hochan.sqlite.fragment.SearchDialogFragment;
import com.hochan.sqlite.fragment.WorkersListFragment;
import com.hochan.sqlite.sql.DataHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnSearch, btnAdd, btnClear;
    private WorkersListFragment mWorkersListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSearch = (Button) findViewById(R.id.btn_search);
        btnAdd = (Button) findViewById(R.id.btn_add);
        btnClear = (Button) findViewById(R.id.btn_clear);
        btnSearch.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        mWorkersListFragment = (WorkersListFragment) getSupportFragmentManager().findFragmentByTag(WorkersListFragment.TAG);
        if(mWorkersListFragment == null) {
            mWorkersListFragment = WorkersListFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.rl_workers_list, mWorkersListFragment, WorkersListFragment.TAG).commit();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.btn_search:
                //intent.setClass(this, SearchActivity.class);
                SearchDialogFragment searchDialog = SearchDialogFragment.newInstance();
                searchDialog.setSearchListener(new SearchDialogFragment.OnSearchDialogListener() {
                    @Override
                    public void searchDialog(String key, int type) {
                        mWorkersListFragment.search(key, type);
                    }

                    @Override
                    public void showAll() {
                        mWorkersListFragment.showAll();
                    }
                });
                searchDialog.show(getSupportFragmentManager(), "search");
                return;
            case R.id.btn_add:
                //intent.setClass(this, AddActivity.class);
                EditDialogFragment addDialog = EditDialogFragment.newInstance(EditDialogFragment.ADD_DIALOG);
                addDialog.setOnDialogListener(new EditDialogFragment.OnDialogListener() {
                    @Override
                    public void clicked(int index, Worker worker) {
                        DataHelper tmpDataHelper = new DataHelper(getApplicationContext());
                        Long result = tmpDataHelper.addData(worker);
                        worker.setmID(String.valueOf(result));
                        Toast.makeText(getApplicationContext(), "添加成功：" + result, Toast.LENGTH_SHORT).show();
                        mWorkersListFragment.add(worker);
                    }
                });
                addDialog.show(getSupportFragmentManager(), "add");
                return;
            case R.id.btn_clear:
                DataHelper dataHelper = new DataHelper(getApplicationContext());
                dataHelper.clearTable(getApplicationContext());
                mWorkersListFragment.clear();
                Toast.makeText(getApplicationContext(), "已清空表的记录", Toast.LENGTH_LONG).show();
                return;
        }
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

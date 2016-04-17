package com.hochan.sqlite;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hochan.sqlite.adapter.ShowAdapter;
import com.hochan.sqlite.data.Worker;
import com.hochan.sqlite.sql.DataHelper;

import java.util.List;

public class ShowActivity extends AppCompatActivity {

    private RecyclerView recyShow;
    private ShowAdapter mShowAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyShow = (RecyclerView) findViewById(R.id.recy_show);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyShow.setLayoutManager(layoutManager);
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        List<Worker> workers = dataHelper.getWorkersInfo();
        mShowAdapter = new ShowAdapter(getBaseContext(), workers);
        recyShow.setAdapter(mShowAdapter);
    }

}

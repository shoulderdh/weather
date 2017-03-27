package com.study.dh.myapplication.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.study.dh.myapplication.R;

/**
 * Created by dh on 2017/3/21.
 */

public class BaseActivity  extends AppCompatActivity {

//    private LinearLayout rootLayout;
//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
//        // 这句很关键，注意是调用父类的方法
//        super.setContentView(R.layout.activity_base);
//        // 经测试在代码里直接声明透明状态栏更有效
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
//            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
//        }
//        initToolbar();
//    }
//
//    private void initToolbar() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        if (toolbar != null) {
//            setSupportActionBar(toolbar);
//        }
//    }
//
//    @Override
//    public void setContentView(int layoutId) {
//        setContentView(View.inflate(this, layoutId, null));
//    }
//
//    @Override
//    public void setContentView(View view) {
//        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
//        if (rootLayout == null) return;
//        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        initToolbar();
//    }
    }


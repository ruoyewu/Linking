package com.wuruoye.linking2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.wuruoye.linking2.base.BaseActivity;
import com.wuruoye.linking2.model.LinkingCache;
import com.wuruoye.linking2.widget.ShineTextView;

/**
 * Created by wuruoye on 2017/5/14.
 * this file is to do
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    private static final String[][] NUM_S = new String[][]{
            {"5","6","7","8","9","10"},
            {"5","6","7","8","9","10","11","12","13","14","15","16","17"}
    };
    private static final String[] TITLE_S = new String[]{
            "选择横排数量",
            "选择纵排数量"
    };

    private LinkingCache linkingCache;

    private TextView stvX;
    private TextView stvY;

    @Override
    protected int getContentView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData(Bundle bundle) {
        linkingCache = new LinkingCache(getApplicationContext());
    }

    @Override
    protected void initView() {
        stvX = (TextView) findViewById(R.id.stv_setting_x);
        stvY = (TextView) findViewById(R.id.stv_setting_y);

        stvX.setText(String.valueOf(linkingCache.getXNum()));
        stvY.setText(String.valueOf(linkingCache.getYNum()));

        stvX.setOnClickListener(this);
        stvY.setOnClickListener(this);

    }

    private void showSelect(final int flag, final TextView view){
        new AlertDialog.Builder(this)
                .setTitle(TITLE_S[flag])
                .setItems(NUM_S[flag], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String num = NUM_S[flag][which];
                        int n = Integer.parseInt(num);
                        if (flag == 0){
                            int x = linkingCache.getYNum() * n;
                            if (x % 2 == 1){
                                showError(view,flag);
                            }else {
                                linkingCache.saveXNum(n);
                                view.setText(num);
                            }
                        }else {
                            int x = linkingCache.getXNum() * n;
                            if (x % 2 == 1){
                                showError(view,flag);
                            }else {
                                linkingCache.saveYNum(n);
                                view.setText(num);
                            }
                        }
                    }
                })
                .show();
    }

    private void showError(final TextView view, final int flag){
        new AlertDialog.Builder(this)
                .setTitle("所选数不能满足item为偶数，请重新选择")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showSelect(flag,view);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stv_setting_x:{
                showSelect(0, (TextView) v);
            }break;
            case R.id.stv_setting_y:{
                showSelect(1, (TextView) v);
            }break;
        }
    }
}

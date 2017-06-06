package com.wuruoye.linking2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wuruoye.linking2.base.BaseActivity;
import com.wuruoye.linking2.model.LinkingCache;
import com.wuruoye.linking2.widget.LinkingView;

/**
 * Created by wuruoye on 2017/5/14.
 * this file is to do
 */

public class LinkActivity extends BaseActivity implements LinkingView.OnLinkGameListener, View.OnClickListener{
    private LinkingCache linkingCache;
    private int timePast;
    private int timeLeft;
    private boolean isStep;
    private int stepNow = 0;

    private FrameLayout flGame;
    private TextView tvTimeLeft;
    private ImageView ivRefresh;
    private LinkingView linkingView;
    private AlertDialog.Builder alertDialog;

    @Override
    protected int getContentView() {
        return R.layout.activity_link;
    }

    @Override
    protected void initData(Bundle bundle) {
        isStep = bundle.getBoolean("isStep");
        linkingCache = new LinkingCache(getApplicationContext());
        timePast = 0;
        timeLeft = 60;
    }

    @Override
    protected void initView() {
        flGame = (FrameLayout) findViewById(R.id.fl_link_game);
        tvTimeLeft = (TextView) findViewById(R.id.tv_time_left);
        ivRefresh = (ImageView) findViewById(R.id.iv_refresh);

        ivRefresh.setOnClickListener(this);

        if (!isStep) {
            linkingView = new LinkingView(this,this,linkingCache.getXNum(),
                    linkingCache.getYNum(),11);
        }else {
            linkingView = new LinkingView(this,this,linkingCache.getXNum(),
                    linkingCache.getYNum(),stepArray[stepNow]);
        }
        flGame.addView(linkingView);

        timeGo();
    }

    private void timeGo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (timeLeft == 0){
                    showGameOver();
                }
                while (timeLeft > 0){
                    try {
                        Thread.sleep(1000);
                        timeLeft --;
                        timePast ++;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvTimeLeft.setText(String.valueOf(timeLeft));
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void showGameOver() {
        new AlertDialog.Builder(this)
                .setTitle("游戏结束！")
                .setCancelable(false)
                .setPositiveButton("重新开始！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timePast = 0;
                        timeLeft = 60;
                        linkingView.resetLink(linkingCache.getXNum(),linkingCache.getYNum(),11);
                    }
                })
                .setNegativeButton("退出游戏！", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

    private void refreshLink(){
        linkingView.refresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_refresh:{
                refreshLink();
            }break;
        }
    }

    @Override
    public void onGameOver() {
        if (!isStep) {
            boolean isOver = false;
            if (timePast < linkingCache.getBestTime() || linkingCache.getBestTime() == LinkingCache.TIME_BEST_DEFAULT){
                isOver = true;
                linkingCache.saveBestTime(timePast);
            }

            if (alertDialog == null){
                alertDialog = new AlertDialog.Builder(this)
                        .setTitle("游戏结束，是否重新开始?")
                        .setCancelable(false)
                        .setPositiveButton("重新开始!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                linkingView.resetLink(linkingCache.getXNum(),linkingCache.getYNum(),11);
                                timePast = 0;
                                timeLeft = 60;
                            }
                        })
                        .setNegativeButton("退出!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });
            }
            if (isOver){
                alertDialog.setTitle("恭喜你破纪录了！此次通关 " + timePast + " 秒！");
            }
            alertDialog.show();
        }else {
            linkingView.resetLink(linkingCache.getXNum(),linkingCache.getYNum(),stepArray[stepNow++]);
        }
    }

    @Override
    public void onLinked() {
        timeLeft += 5;
        tvTimeLeft.setText(String.valueOf(timeLeft));
    }

    private static final int[] stepArray = new int[]{
            4,5,6,7,8,9,10,11
    };

}

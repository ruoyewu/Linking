package com.wuruoye.linking2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.wuruoye.linking2.base.BaseActivity;
import com.wuruoye.linking2.model.LinkingCache;
import com.wuruoye.linking2.widget.BackgroundView;
import com.wuruoye.linking2.widget.ShineTextView;

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private FrameLayout flBack;
    private ShineTextView stvStart;
    private ShineTextView stvStep;
    private ShineTextView stvSetting;


    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData(Bundle bundle) {
    }

    @Override
    protected void initView() {
        flBack = (FrameLayout) findViewById(R.id.fl_background);
        stvStart = (ShineTextView) findViewById(R.id.stv_start);
        stvStep = (ShineTextView) findViewById(R.id.stv_step);
        stvSetting = (ShineTextView) findViewById(R.id.stv_setting);

        flBack.addView(new BackgroundView(this));

        stvStart.setOnClickListener(this);
        stvStep.setOnClickListener(this);
        stvSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.stv_start:{
                Intent intent = new Intent(this,LinkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isStep",false);
                intent.putExtras(bundle);
                startActivity(intent);
            }break;
            case R.id.stv_step:{
//                Toast.makeText(this, "关卡模式", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,LinkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isStep",true);
                intent.putExtras(bundle);
                startActivity(intent);
            }break;
            case R.id.stv_setting:{
                startActivity(new Intent(this,SettingActivity.class));
            }break;
        }
    }
}

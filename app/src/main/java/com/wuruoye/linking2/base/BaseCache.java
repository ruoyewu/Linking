package com.wuruoye.linking2.base;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wuruoye on 2017/5/13.
 * this file is to do
 */

public class BaseCache {
    private static final String spName = "linking";
    protected static SharedPreferences mSP;

    public BaseCache(Context context){
        if (mSP == null){
            synchronized (this){
                if (mSP == null){
                    mSP = context.getSharedPreferences(spName,Context.MODE_PRIVATE);
                }
            }
        }
    }
}

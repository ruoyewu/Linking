package com.wuruoye.linking2.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.wuruoye.linking2.base.BaseCache;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wuruoye on 2017/5/13.
 * this file is to do
 */

public class LinkingCache extends BaseCache {
    private static final int X_DEFAULT = 8;
    private static final int Y_DEFAULT = 14;
    public static final int TIME_BEST_DEFAULT = -1;

    private static final String NUM = "num";
    private static final String X_NUM = "x_num";
    private static final String Y_NUM = "y_num";
    private static final String TIME_BEST = "time_best";

    public LinkingCache(Context context) {
        super(context);
    }

    public void saveNum(int[][] num){
        String json = new Gson().toJson(num);
        mSP.edit().putString(NUM,json).apply();
    }
    public int[][] getNum(){
        String json = mSP.getString(NUM,"");
        try {
            JSONArray array = new JSONArray(json);

            int[][] num = new int[array.length()][];
            for (int i = 0; i < array.length(); i ++){
                JSONArray jsonArray = array.getJSONArray(i);
                int[] n = new int[jsonArray.length()];
                for (int j = 0; j < jsonArray.length(); j ++){
                    n[j] = jsonArray.getInt(j);
                }
                num[i] = n;
            }
            return num;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new int[1][1];
    }

    public int getXNum(){
        return mSP.getInt(X_NUM,X_DEFAULT);
    }
    public int getYNum(){
        return mSP.getInt(Y_NUM,Y_DEFAULT);
    }
    public void saveXNum(int x){
        mSP.edit().putInt(X_NUM,x).apply();
    }
    public void saveYNum(int y){
        mSP.edit().putInt(Y_NUM,y).apply();
    }
    public int getBestTime(){return mSP.getInt(TIME_BEST,TIME_BEST_DEFAULT);}
    public void saveBestTime(int time){mSP.edit().putInt(TIME_BEST,time).apply();}

}

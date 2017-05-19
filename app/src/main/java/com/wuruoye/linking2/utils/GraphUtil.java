package com.wuruoye.linking2.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuruoye on 2017/5/5.
 * this file is from do
 */

public class GraphUtil {
    public static final int UP = 1;
    public static final int DOWN = -1;
    public static final int LEFT = 2;
    public static final int RIGHT = -2;

    private static List<List<Integer>> mPath = new ArrayList<>();
    private static int[][] num;
    private static int from;
    private static int[] end;
    private static int width;
    private static int height;

    public static List<Integer> getPath(int[][] num, int[] start, int[] end){
        mPath.clear();
        int s = num[start[0]][start[1]];
        if (num[end[0]][end[1]] != s){
            return null;
        }
        GraphUtil.num = num;
        GraphUtil.end = end;
        GraphUtil.from = s;
        int x = start[0];
        int y = start[1];
        width = num[0].length;
        height = num.length;
        DFS_2(new ArrayList<Integer>(),x,y,0,0);

        int min = -1;
        for (int i = 0; i < mPath.size(); i ++){
            if (min == -1 || mPath.get(i).size() < mPath.get(min).size()){
                min = i;
            }
        }
        if (min != -1) {
            return mPath.get(min);
        }else
            return null;
    }

    private static void DFS_2(List<Integer> path, int x, int y, int od, int step){
        go(UP,path,x,y-1,od,step);
        go(DOWN,path,x,y+1,od,step);
        go(LEFT,path,x-1,y,od,step);
        go(RIGHT,path,x+1,y,od,step);
    }

    private static void go(int direct,List<Integer> path,int x,int y,int od, int step){
        List<Integer> p = new ArrayList<>();
        p.addAll(path);
        if (direct != od){
            step ++;
        }
        if (x >= 0 && x < height && y >= 0 && y < width && direct != -od){
            if (num[x][y] == from && x == end[0] && y == end[1]){
                p.add(direct);
                if (step < 4) {
                    mPath.add(p);
                }
            }else if (num[x][y] == 0){
                p.add(direct);
                if (direct == od){
                    DFS_2(p,x,y,direct,step);
                }else {
                    if (step < 4) {
                        DFS_2(p,x,y,direct,step);
                    }
                }
            }
        }
    }

}

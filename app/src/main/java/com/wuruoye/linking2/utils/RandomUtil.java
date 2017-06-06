package com.wuruoye.linking2.utils;


/**
 * Created by wuruoye on 2017/5/3.
 * this file is to do
 */

public class RandomUtil {
    public static int[][] getNum(int x, int y, int max){
        int min = 1;
        int num[][] = new int[x][y];
        for (int i = 1; i < x - 1; i ++){
            for (int j = 1; j < y - 1; j ++){
                num[i][j] = RandomUtil.getRandom(min,max);
            }
        }
        int[] count = new int[max];
        for (int[] a : num){
            for (int b : a){
                count[b] ++;
            }
        }
        for (int i = 1; i < max; i ++){
            if (count[i] % 2 == 1){
                for (int j = i+1; j < max; j ++){
                    if (count[j] % 2 == 1){
                        int[] start = getPosition(i,num);
                        if (start != null) {
                            num[start[0]][start[1]] = j;
                            count[i] --;
                            count[j] ++;
                        }
                        break;
                    }
                }
            }
        }
        return num;
    }

    public static int[][] getNum(int[][] num, int max){
        int min = 1;
        for (int i = 0; i < num.length; i++) {
            for (int j = 0; j < num[i].length; j++) {
                if (num[i][j] != 0){
                    num[i][j] = getRandom(min,max);
                }
            }
        }
        return num;
    }

    private static int[] getPosition(int n, int[][] num){
        for (int i = 0; i < num.length; i ++){
            for (int j = 0; j < num[i].length; j ++){
                if (num[i][j] == n){
                    int[] position = new int[2];
                    position[0] = i;
                    position[1] = j;
                    return position;
                }
            }
        }
        return null;
    }

    private static int getRandom(int low, int height){
        return (int) (Math.random() * (height - low) + low);
    }
}

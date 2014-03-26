package com.seu.hitrip.util;

/**
 * Created by yqf on 3/26/14.
 */
public class MathTools {
    public static int getRandomBetween(int a, int b){
        return (int)Math.round(a + Math.random() * (b - a));
    }
}

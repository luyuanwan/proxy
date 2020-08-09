package com.xiang.proxy;

import lombok.Data;

@Data
public class Context {

    private static String homepage = "https://centerdev2.starride.cn";

    public static String getHomePage(){
        return homepage;
    }
}

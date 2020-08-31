package com.xiang.proxy;

import lombok.Data;

@Data
public class Context {

    private static String homepage = "https://center.starride.cn";

    public static String getHomePage(){
        return homepage;
    }
}

package com.xiang.proxy.modify;

import com.xiang.proxy.Context;
import lombok.Data;

import java.util.function.Function;

@Data
public class ModifyItem {

    private String url;
    private String param;
    @Deprecated
    private Function<String,String> modify;//params
    private Function<String,String> resultModify;
    private String domain;


    public ModifyItem(){
        this.domain = Context.getHomePage();
    }
}

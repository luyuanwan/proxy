package com.xiang.proxy.bo;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpVersion;
import lombok.Data;

@Data
public class BusinessBo {

    String uri;

    String content;

    HttpVersion version;

    String method;

    public String methodName(){
        return method;
    }

    public String content(){
        return content;
    }

    public HttpVersion protocolVersion(){
        return version;
    }
}

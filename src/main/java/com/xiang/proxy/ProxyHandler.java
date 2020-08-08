package com.xiang.proxy;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AsciiString;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.*;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class ProxyHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {

        if(httpObject instanceof HttpRequest){
            HttpRequest h = (HttpRequest)httpObject;
            String method = h.getMethod().name();

            String uri = h.getUri();
            if(uri.endsWith("ico")){
                ctx.writeAndFlush("");
                return ;
            }

            AsciiString type = TEXT_HTML;
            if(uri.contains("css")){
                type = TEXT_CSS;
            }

            if(uri.endsWith("png")){
                type = AsciiString.cached("image/webp,image/apng,image/*,*/*;q=0.8");
            }


            System.out.println(method + " " + uri);
            if(method.equals("GET")){
                //String newUrl = "http://news.sina.com.cn";
                String newUrl = "http://centertest2.starride.cn";
                String content = HttpUtils.get(newUrl + h.getUri()).getResult();

                FullHttpResponse response = new DefaultFullHttpResponse(h.protocolVersion(), OK,
                        Unpooled.wrappedBuffer(content.getBytes()));
                response.headers()
                        .set(CONTENT_TYPE, type)
                        .setInt(CONTENT_LENGTH, response.content().readableBytes());

                ChannelFuture f = ctx.write(response);
            }
        }
    }
}

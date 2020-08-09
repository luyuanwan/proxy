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
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_CSS;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_HTML;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class ProxyStringHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject httpObject) throws Exception {

        String newUrl = "https://centertest2.starride.cn";

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
            String content = "";
            if(method.equals("GET")){
                content = HttpUtils.get(newUrl + h.getUri()).getResult();
            }else if(method.equals("POST")){
//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                NameValuePair a = new BasicNameValuePair("phone","12345678900");
//                NameValuePair b = new BasicNameValuePair("pwd","39b5177e82858ecc5661a2077b58edc3");
//                NameValuePair c = new BasicNameValuePair("verifyCode","111");
//
//
//
//                params.add(a);
//                params.add(b);
//                params.add(c);

                        String params = "{\"phone\":\"12345678900\",\"pwd\":\"ceeb83dd7ef2d96a2bd9d0aebef9d3c7\",\"verifyCode\":\"1111\"}";
                content = HttpUtils.postJsonBody(newUrl + h.getUri(),params).getResult();
            }

            System.out.println("content : " + content);

            FullHttpResponse response = new DefaultFullHttpResponse(h.protocolVersion(), OK,
                    Unpooled.wrappedBuffer(content.getBytes()));
            response.headers()
                    .set(CONTENT_TYPE, type)
                    .setInt(CONTENT_LENGTH, response.content().readableBytes());

            ChannelFuture f = ctx.write(response);
        }
    }
}

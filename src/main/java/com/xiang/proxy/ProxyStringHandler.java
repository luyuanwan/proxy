package com.xiang.proxy;

import com.alibaba.fastjson.JSON;
import com.xiang.proxy.modify.LiushuiItem;
import com.xiang.proxy.modify.ModifyItem;
import com.xiang.proxy.modify.MenutItem;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import io.netty.util.CharsetUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_CSS;
import static io.netty.handler.codec.http.HttpHeaderValues.TEXT_HTML;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

public class ProxyStringHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

   static String cookie;


   static Map<String,ModifyItem> modifyItemMap;
   static {
       modifyItemMap = new HashMap<>();
       //modifyItemMap.put(OrderInfo.modify().getUrl(), OrderInfo.modify());
       //收入流水
       modifyItemMap.put(LiushuiItem.modify().getUrl(),LiushuiItem.modify());
       //订单菜单
       modifyItemMap.put(MenutItem.orderModify().getUrl(), MenutItem.orderModify());
       //财务菜单
       modifyItemMap.put(MenutItem.caiwuModify().getUrl(),MenutItem.caiwuModify());
   }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {


        String uri = request.uri();
        //无论输入什么，都能登录
       if( "/auth-server/login/login".equalsIgnoreCase(uri)){
           login(ctx, request);
           return ;
       }
       //一旦发现没有登录了，且不是主目录，则自动获取cookie
        if(cookie == null && !uri.equalsIgnoreCase("/") && !uri.endsWith("png")){
            HttpUtils.HttpResponse  responseFromUtils = HttpUtils.postJsonBody(Context.getHomePage() + "/auth-server/login/login", "{\"phone\":\"12345678900\",\"pwd\":\"ceeb83dd7ef2d96a2bd9d0aebef9d3c7\",\"verifyCode\":\"1111\"}",
                    null);

            String c = getCookie(responseFromUtils.getResult(), "/auth-server/login/login");
            if (c != null) {
                cookie = c;
            }
        }

        ByteBuf content = request.content();
        String requestContent = content.toString(CharsetUtil.UTF_8);


        String method = request.getMethod().name();



        if(uri.equals("/login")){
            uri = "/";
        }



        if (uri.endsWith("ico")) {
            ctx.writeAndFlush("");
            return;
        }

        AsciiString type = TEXT_HTML;
        if (uri.contains("css")) {
            type = TEXT_CSS;
        }

        boolean isImage = false;
        if (uri.endsWith("png") || uri.endsWith("jpg")) {
            isImage = true;
            type = AsciiString.cached("image/png");  //AsciiString.cached("image/webp,image/apng,image/*,*/*;q=0.8");
        }

        if(uri.endsWith("jpg")){
            isImage = true;
            type = AsciiString.cached("image/jpg");  //AsciiString.cached("image/webp,image/apng,image/*,*/*;q=0.8");
        }

        HttpUtils.HttpResponse responseFromUtils = null;

        ModifyItem modifyItem =   modifyItemMap.get(uri);

        String domain = Optional.ofNullable(modifyItem).map(p->p.getDomain()).orElse(Context.getHomePage());
        if (method.equals("GET")) {
            responseFromUtils = HttpUtils.get(domain + uri, cookie, isImage);
        } else if (method.equals("POST")) {
            requestContent = Optional.ofNullable(modifyItem).map(p->p.getParam()).orElse(requestContent);
            responseFromUtils = HttpUtils.postJsonBody(domain + uri, requestContent, cookie);
        }

        flush(ctx,responseFromUtils,request,uri,type);

    }

    private void login(ChannelHandlerContext ctx,FullHttpRequest request){
        HttpUtils.HttpResponse  responseFromUtils = HttpUtils.postJsonBody(Context.getHomePage() + "/auth-server/login/login", "{\"phone\":\"12345678900\",\"pwd\":\"ceeb83dd7ef2d96a2bd9d0aebef9d3c7\",\"verifyCode\":\"1111\"}",
                null);

        flush(ctx,responseFromUtils,request,"/auth-server/login/login",TEXT_HTML);
    }

    private String getCookie(String msg,String uri){
        if("/auth-server/login/login".equals(uri)){
            Cookie cookie = JSON.parseObject(msg,Cookie.class);
            return cookie.getData();
        }
        return null;
    }

    @Data
    private static class Cookie {
        private String data;
    }

    public void flush(ChannelHandlerContext ctx,HttpUtils.HttpResponse responseFromUtils,FullHttpRequest request,String uri, AsciiString type){

        ModifyItem modifyItem =   modifyItemMap.get(uri);
        if(modifyItem != null){
            Function<String, String> modifyResult = modifyItem.getResultModify();
            if (modifyResult != null) {
               String msg = modifyResult.apply("");

               doFlush(ctx,request,TEXT_HTML,msg);
               return ;
            }
        }

        if(uri.equalsIgnoreCase("/auth-server/AcUserLogicSystem/getOwnLogicSystems")){
            //含配置中心
            String msg = "{\"message\":\"success\",\"code\":200,\"data\":[{\"id\":7,\"logicSystemName\":\"订单系统\",\"logicSystemUrl\":\"#\",\"personInCharge\":\"admin\",\"logicSystemKey\":\"736cdfe8de344164a38bc435f1fd08b0\",\"logicSystemSecretKey\":null,\"iconCode\":\"12\",\"showOrder\":1,\"frozenStatus\":0,\"delFlag\":0,\"createTime\":\"2018-10-26\",\"updateTime\":\"2019-09-30\",\"keyWord\":null,\"relationId\":null},{\"id\":6,\"logicSystemName\":\"配置中心\",\"logicSystemUrl\":\"#\",\"personInCharge\":\"admin\",\"logicSystemKey\":\"370e3269b70245958e7801c1291dbb39\",\"logicSystemSecretKey\":null,\"iconCode\":\"\",\"showOrder\":5,\"frozenStatus\":0,\"delFlag\":0,\"createTime\":\"2018-10-25\",\"updateTime\":\"2018-10-25\",\"keyWord\":null,\"relationId\":null},{\"id\":15,\"logicSystemName\":\"财务系统\",\"logicSystemUrl\":\"#\",\"personInCharge\":\"admin\",\"logicSystemKey\":\"317a86e7f11d448e9a73077ad51815e3\",\"logicSystemSecretKey\":null,\"iconCode\":\"setting\",\"showOrder\":8,\"frozenStatus\":0,\"delFlag\":0,\"createTime\":\"2019-03-14\",\"updateTime\":\"2019-10-09\",\"keyWord\":null,\"relationId\":null},{\"id\":17,\"logicSystemName\":\"营销系统\",\"logicSystemUrl\":\"#\",\"personInCharge\":\"admin\",\"logicSystemKey\":\"dd00a5a2b87149989b598c47391c8891\",\"logicSystemSecretKey\":null,\"iconCode\":\"shopping-cart\",\"showOrder\":9,\"frozenStatus\":0,\"delFlag\":0,\"createTime\":\"2019-03-15\",\"updateTime\":\"2019-09-30\",\"keyWord\":null,\"relationId\":null},{\"id\":19,\"logicSystemName\":\"用户系统\",\"logicSystemUrl\":\"#\",\"personInCharge\":\"admin\",\"logicSystemKey\":\"5f15cbda873c4960bb4157abfadce5f7\",\"logicSystemSecretKey\":null,\"iconCode\":\"user\",\"showOrder\":11,\"frozenStatus\":0,\"delFlag\":0,\"createTime\":\"2019-03-21\",\"updateTime\":\"2019-09-30\",\"keyWord\":null,\"relationId\":null}],\"success\":true}";
            //不含配置中心
            msg = "{\"message\":\"success\",\"code\":200,\"data\":[{\"id\":7,\"logicSystemName\":\"订单系统\",\"logicSystemUrl\":\"#\",\"personInCharge\":\"admin\",\"logicSystemKey\":\"736cdfe8de344164a38bc435f1fd08b0\",\"logicSystemSecretKey\":null,\"iconCode\":\"12\",\"showOrder\":1,\"frozenStatus\":0,\"delFlag\":0,\"createTime\":\"2018-10-26\",\"updateTime\":\"2019-09-30\",\"keyWord\":null,\"relationId\":null},{\"id\":15,\"logicSystemName\":\"财务系统\",\"logicSystemUrl\":\"#\",\"personInCharge\":\"admin\",\"logicSystemKey\":\"317a86e7f11d448e9a73077ad51815e3\",\"logicSystemSecretKey\":null,\"iconCode\":\"setting\",\"showOrder\":8,\"frozenStatus\":0,\"delFlag\":0,\"createTime\":\"2019-03-14\",\"updateTime\":\"2019-10-09\",\"keyWord\":null,\"relationId\":null},{\"id\":19,\"logicSystemName\":\"用户系统\",\"logicSystemUrl\":\"#\",\"personInCharge\":\"admin\",\"logicSystemKey\":\"5f15cbda873c4960bb4157abfadce5f7\",\"logicSystemSecretKey\":null,\"iconCode\":\"user\",\"showOrder\":11,\"frozenStatus\":0,\"delFlag\":0,\"createTime\":\"2019-03-21\",\"updateTime\":\"2019-09-30\",\"keyWord\":null,\"relationId\":null}],\"success\":true}";

            doFlush(ctx,request,TEXT_HTML,msg);
            return ;
        }



        if(responseFromUtils.getResultBytes() != null){
            //images
            FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), OK,
                    Unpooled.wrappedBuffer(responseFromUtils.getResultBytes()));
            response.headers()
                    .set(CONTENT_TYPE, type)
                    .setInt(CONTENT_LENGTH, response.content().readableBytes());

            ChannelFuture f = ctx.write(response);

        }else {
            //stream
            String msg = responseFromUtils.getResult();

            String c = getCookie(msg, uri);
            if (c != null) {
                cookie = c;
            }

            doFlush(ctx,request,type,msg);
        }
    }

    private void doFlush(ChannelHandlerContext ctx,FullHttpRequest request, AsciiString type,String msg){
        FullHttpResponse response = new DefaultFullHttpResponse(request.protocolVersion(), OK,
                Unpooled.wrappedBuffer(msg.getBytes()));
        response.headers()
                .set(CONTENT_TYPE, type)
                .setInt(CONTENT_LENGTH, response.content().readableBytes());

        ChannelFuture f = ctx.write(response);
    }


//    public static void main(String[] args) {
//        String cookie = "4e6ef299a7b24af9849944c27ae57d6f";
//       String s =  HttpUtils.get("https://centertest2.starride.cn/auth-server/AcUserLogicSystem/getOwnLogicSystems",cookie).getResult();
//       // String s =  HttpUtils.get("http://localhost:10924/customer-marketing/dropBox/emails",cookie).getResult();
//        System.out.println(s);
//    }
}

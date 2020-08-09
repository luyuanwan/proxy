package com.xiang.proxy.modify;

/**
 * 菜单
 */
public class OrderMenutItem {




    public static ModifyItem modify(){

        /**
         * 订单
         */
        ModifyItem item =  new ModifyItem();
        item.setUrl("/auth-server/index/getMenuBySystemId?systemId=7");
        item.setResultModify(p->{
            String newContent = "{\"message\":\"success\",\"code\":200,\"data\":[{\"id\":17983,\"pid\":1,\"logicSystemId\":7,\"physicsSystemId\":9,\"urlName\":\"订单管理\",\"url\":\"#\",\"urlType\":1,\"isSpecial\":0,\"urlScope\":2,\"isNewOpen\":0,\"delFlag\":0,\"isVisible\":1,\"createTime\":\"2018-10-26\",\"updateTime\":\"2018-11-09\",\"logicSystemName\":null,\"physicsSystemName\":null,\"parentUrlName\":null,\"showOrder\":1,\"childUrl\":[{\"id\":17984,\"pid\":17983,\"logicSystemId\":7,\"physicsSystemId\":9,\"urlName\":\"订单查询\",\"url\":\"/order-center/index\",\"urlType\":1,\"isSpecial\":0,\"urlScope\":2,\"isNewOpen\":0,\"delFlag\":0,\"isVisible\":1,\"createTime\":\"2018-10-26\",\"updateTime\":\"2019-03-21\",\"logicSystemName\":null,\"physicsSystemName\":null,\"parentUrlName\":null,\"showOrder\":1,\"childUrl\":[],\"operateGroupName\":null,\"ids\":null,\"isCollected\":null,\"checkFlag\":null}],\"operateGroupName\":null,\"ids\":null,\"isCollected\":null,\"checkFlag\":null}],\"success\":true}";
            return newContent;
        });

        return item;
    }
}

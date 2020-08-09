package com.xiang.proxy.modify;

/**
 * 菜单
 */
public class MenutItem {


    public static ModifyItem caiwuModify(){
        /**
         * 财务
         */
        ModifyItem item = new ModifyItem();
        item.setUrl("/auth-server/index/getMenuBySystemId?systemId=15");
        item.setResultModify(p->{
            return "{\"message\":\"success\",\"code\":200,\"data\":[{\"id\":18597,\"pid\":1,\"logicSystemId\":15,\"physicsSystemId\":0,\"urlName\":\"应收款管理\",\"url\":\"#\",\"urlType\":1,\"isSpecial\":0,\"urlScope\":2,\"isNewOpen\":0,\"delFlag\":0,\"isVisible\":1,\"createTime\":\"2019-05-05\",\"updateTime\":\"2019-05-05\",\"logicSystemName\":null,\"physicsSystemName\":null,\"parentUrlName\":null,\"showOrder\":2,\"childUrl\":[{\"id\":18527,\"pid\":18597,\"logicSystemId\":15,\"physicsSystemId\":0,\"urlName\":\"收入流水\",\"url\":\"/finance-center/income-stream\",\"urlType\":1,\"isSpecial\":0,\"urlScope\":2,\"isNewOpen\":0,\"delFlag\":0,\"isVisible\":1,\"createTime\":\"2019-04-30\",\"updateTime\":\"2019-05-05\",\"logicSystemName\":null,\"physicsSystemName\":null,\"parentUrlName\":null,\"showOrder\":6,\"childUrl\":[],\"operateGroupName\":null,\"ids\":null,\"isCollected\":null,\"checkFlag\":null},{\"id\":18529,\"pid\":18597,\"logicSystemId\":15,\"physicsSystemId\":0,\"urlName\":\"收入流水汇总\",\"url\":\"/finance-center/income-stream-total\",\"urlType\":1,\"isSpecial\":0,\"urlScope\":2,\"isNewOpen\":0,\"delFlag\":0,\"isVisible\":1,\"createTime\":\"2019-04-30\",\"updateTime\":\"2019-05-05\",\"logicSystemName\":null,\"physicsSystemName\":null,\"parentUrlName\":null,\"showOrder\":7,\"childUrl\":[],\"operateGroupName\":null,\"ids\":null,\"isCollected\":null,\"checkFlag\":null}],\"operateGroupName\":null,\"ids\":null,\"isCollected\":null,\"checkFlag\":null}],\"success\":true}";
        });
        return item;
    }



    public static ModifyItem orderModify(){

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

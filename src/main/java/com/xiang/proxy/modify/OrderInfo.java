package com.xiang.proxy.modify;

public class OrderInfo {

    public static ModifyItem modify(){

        /**
         * 订单查询
         */
        ModifyItem item =  new ModifyItem();
        item.setUrl("/daimler-manage-admin/order/queryOrderList");
        item.setModify(p->{
            String newContent = "{\"pageSize\":10,\"pageNum\":1,\"orderNo\":\"40402501000\",\"orderStatusList\":[],\"pageNo\":1,\"bizLine\":9}";
            return newContent;
        });

        return item;
    }
}

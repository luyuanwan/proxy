package com.xiang.proxy.modify;

/**
 * 流水
 */
public class LiushuiItem {

    /**
     * 收入流水
     * @return
     */
    public static ModifyItem modify(){
        ModifyItem item =  new ModifyItem();
        item.setUrl("/account-manager/income/findPageInfo");
        item.setModify(p->{
            String newContent = "{\"pageSize\":10,\"pageNum\":1,\"owner\":1,\"useTimeStart\":\"2020-08-06 00:00:00\",\"useTimeEnd\":\"2020-08-07 23:59:59\",\"orderNo\":\"63801801228\"}";
            return newContent;
        });

        item.setResultModify(p->{
            return "{\"code\":200,\"data\":{\"list\":[{\"additionalFee\":23230,\"additionalFeeStr\":\"1.00\",\"bridgeFee\":3440,\"bridgeFeeStr\":\"2.00\",\"cancelAmount\":902909,\"cancelAmountStr\":\"3.00\",\"carNo\":\"24\",\"carNumber\":\"国A78965\",\"cashAmount\":39090,\"cashAmountStr\":\"389.00\",\"cityCode\":\"0571\",\"cityName\":\"美国式\",\"commission\":100990,\"commissionStr\":\"8.00\",\"companyAmount\":38920,\"companyAmountStr\":\"6.00\",\"companyCreditAmount\":99389,\"companyCreditAmountStr\":\"4.00\",\"companyDonateAmount\":1,\"companyDonateAmountStr\":\"1.00\",\"companyNo\":\"1\",\"customerAmount\":16000,\"customerAmountStr\":\"160.00\",\"customerDistributeAmount\":20,\"customerDistributeAmountStr\":\"10.00\",\"customerDonateAmount\":23,\"customerDonateAmountStr\":\"10.00\",\"discountAmount\":800,\"discountAmountStr\":\"800.00\",\"driverName\":\"黄磊磊\\u0028Test2\\u0029\",\"driverNo\":\"18039677691096\",\"driverPhone\":\"15567477778\",\"finishPayTime\":1596694583000,\"finishPayTimeStr\":\"2020-08-06 14:16:23\",\"freeStatusStr\":\"否\",\"highSpeedFee\":93893,\"highSpeedFeeStr\":\"900.00\",\"initPayTime\":1596694583000,\"initPayTimeStr\":\"2020-08-06 14:16:23\",\"merchantNo\":10001,\"onlineAmount\":93893,\"onlineAmountStr\":\"3.00\",\"orderNo\":\"63801801228\",\"orderSourceStr\":\"APP\",\"otherFee\":93803,\"otherFeeStr\":\"78.00\",\"owner\":1,\"ownerStr\":\"个人不用车\",\"parkFee\":3780,\"parkFeeStr\":\"89.00\",\"payMethod\":1,\"payMethodStr\":\"个人钱包\",\"reabteAmountStr\":\"988.00\",\"rebateAmount\":0,\"serviceType\":3,\"serviceTypeStr\":\"梅赛德斯-奔驰 W级\",\"subPayMethod\":2,\"subPayMethodStr\":\"本金账户\",\"totalAmount\":16500,\"totalAmountStr\":\"165.00\",\"travelFee\":16500,\"travelFeeStr\":\"165.00\",\"useTime\":1596697200000,\"useTimeStr\":\"2020-08-06 15:00:00\",\"wipeZeroAmount\":500,\"wipeZeroAmountStr\":\"5.00\"}],\"pageNum\":1,\"pageSize\":10,\"total\":1},\"message\":\"成功\",\"success\":true}";
        });


        //item.setDomain("http://localhost:10011");

        //item.setParam("{\"pageSize\":10,\"pageNum\":1,\"useTimeStart\":\"2020-05-11 00:00:00\",\"useTimeEnd\":\"2020-9-18 23:59:59\"}");
        return item;
    }
}

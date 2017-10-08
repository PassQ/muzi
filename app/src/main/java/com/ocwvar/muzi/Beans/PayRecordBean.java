package com.ocwvar.muzi.Beans;

/**
 * Created by 区成伟
 * Package: com.ocwvar.muzi.Beans
 * Date: 2016/5/19  14:15
 * Project: Muzi
 * 缴费记录信息Bean
 */
public class PayRecordBean {

    private String name;
    private String chargeSum;
    private String chargeTime;
    private String orderNo;
    private String totalCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChargeSum() {
        return chargeSum;
    }

    public void setChargeSum(String chargeSum) {
        this.chargeSum = chargeSum;
    }

    public String getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(String chargeTime) {
        this.chargeTime = chargeTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(String totalCount) {
        this.totalCount = totalCount;
    }
}

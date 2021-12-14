package com.treasure.million.bean;

import java.io.Serializable;

/**
 * @ClassName: UsdtInfo
 * @Description: java类作用描述
 * @Author: zt
 * @Date: 2021/9/25 15:36
 */
public class UsdtInfo implements Serializable {
    public String id;
    public String memberId;
    public double userable;
    public double yestodayUserable;
    public double freeze;
    public double totalUserable;
    public String isLock;
    public double minLimit;
    public double fee;
    public String descriptionToString;

    public double getTrc_fee() {
        return trc_fee;
    }

    public void setTrc_fee(double trc_fee) {
        this.trc_fee = trc_fee;
    }

    private double trc_fee;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public double getUserable() {
        return userable;
    }

    public void setUserable(double userable) {
        this.userable = userable;
    }

    public double getYestodayUserable() {
        return yestodayUserable;
    }

    public void setYestodayUserable(int yestodayUserable) {
        this.yestodayUserable = yestodayUserable;
    }

    public double getFreeze() {
        return freeze;
    }

    public void setFreeze(int freeze) {
        this.freeze = freeze;
    }

    public double getTotalUserable() {
        return totalUserable;
    }

    public void setTotalUserable(int totalUserable) {
        this.totalUserable = totalUserable;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public double getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(int minLimit) {
        this.minLimit = minLimit;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getDescriptionToString() {
        return descriptionToString;
    }

    public void setDescriptionToString(String descriptionToString) {
        this.descriptionToString = descriptionToString;
    }

}

package com.treasure.million.bean;

import java.io.Serializable;

/**
 * @ClassName: EthBean
 * @Description: java类作用描述
 * @Author: zt
 * @Date: 2021/12/13 11:28
 */
public class EthBean implements Serializable {
    public String id;
    public String coinTypeId;
    public String coinTypeName;
    public String address;
    public String memberId;
    public String memberName;
    public String createTime;
    public String stringCreateTime;
    public String descriptionToString;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoinTypeId() {
        return coinTypeId;
    }

    public void setCoinTypeId(String coinTypeId) {
        this.coinTypeId = coinTypeId;
    }

    public String getCoinTypeName() {
        return coinTypeName;
    }

    public void setCoinTypeName(String coinTypeName) {
        this.coinTypeName = coinTypeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getStringCreateTime() {
        return stringCreateTime;
    }

    public void setStringCreateTime(String stringCreateTime) {
        this.stringCreateTime = stringCreateTime;
    }

    public String getDescriptionToString() {
        return descriptionToString;
    }

    public void setDescriptionToString(String descriptionToString) {
        this.descriptionToString = descriptionToString;
    }


}

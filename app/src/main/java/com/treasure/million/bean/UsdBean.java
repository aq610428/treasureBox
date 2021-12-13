package com.treasure.million.bean;

import java.io.Serializable;

/**
 * @author: zt
 * @date: 2020/7/30
 * @name:UsdtBean
 */


public class UsdBean implements Serializable {


    public UsdtBean getUsdt() {
        return usdt;
    }

    public void setUsdt(UsdtBean usdt) {
        this.usdt = usdt;
    }

    public EthBean getEth() {
        return eth;
    }

    public void setEth(EthBean eth) {
        this.eth = eth;
    }

    public BoxBean getBox() {
        return box;
    }

    public void setBox(BoxBean box) {
        this.box = box;
    }

    /**
     * usdt : {"id":"b4c00624f3b540278dc6b6f5b7f3013b","coinTypeId":"USDT","coinTypeName":"USDT","memberId":"ac5d38af5a6a4a6dac01f68e542b303a","memberName":"15919936559","userable":167,"freeze":0,"createTime":"2021-01-22T08:31:13.000Z","updateTime":"2021-01-22T08:35:43.000Z","isLock":0,"descriptionToString":"{\"freeze\":\"冻结额度\",\"createTime\":\"创建时间\",\"coinTypeId\":\"币种类型\",\"userable\":\"可用额度\",\"memberName\":\"会员名称\",\"updateTime\":\"更新时间\",\"id\":\"foreignKey\",\"coinTypeName\":\"币种类型名称\",\"memberId\":\"会员\"}","stringCreateTime":"2021-01-22 16:31:13","stringUpdateTime":"2021-01-22 16:35:43"}
     * eth : {"id":"","coinTypeId":"ETH","coinTypeName":"ETH","memberId":"ac5d38af5a6a4a6dac01f68e542b303a","memberName":null,"userable":0,"freeze":0,"createTime":null,"updateTime":null,"isLock":0,"descriptionToString":"{\"freeze\":\"冻结额度\",\"createTime\":\"创建时间\",\"coinTypeId\":\"币种类型\",\"userable\":\"可用额度\",\"memberName\":\"会员名称\",\"updateTime\":\"更新时间\",\"id\":\"foreignKey\",\"coinTypeName\":\"币种类型名称\",\"memberId\":\"会员\"}","stringCreateTime":"","stringUpdateTime":""}
     * box : {"id":"024ecea4c4b14e4dae41411da5d007df","coinTypeId":"BOX","coinTypeName":"BOX","memberId":"ac5d38af5a6a4a6dac01f68e542b303a","memberName":"15919936559","userable":122,"freeze":0,"createTime":"2021-01-22T08:31:13.000Z","updateTime":"2021-01-22T08:35:43.000Z","isLock":0,"descriptionToString":"{\"freeze\":\"冻结额度\",\"createTime\":\"创建时间\",\"coinTypeId\":\"币种类型\",\"userable\":\"可用额度\",\"memberName\":\"会员名称\",\"updateTime\":\"更新时间\",\"id\":\"foreignKey\",\"coinTypeName\":\"币种类型名称\",\"memberId\":\"会员\"}","stringCreateTime":"2021-01-22 16:31:13","stringUpdateTime":"2021-01-22 16:35:43"}
     */

    private UsdtBean usdt;
    private EthBean eth;
    private BoxBean box;

    public static class UsdtBean implements Serializable{
        /**
         * id : b4c00624f3b540278dc6b6f5b7f3013b
         * coinTypeId : USDT
         * coinTypeName : USDT
         * memberId : ac5d38af5a6a4a6dac01f68e542b303a
         * memberName : 15919936559
         * userable : 167.0
         * freeze : 0.0
         * createTime : 2021-01-22T08:31:13.000Z
         * updateTime : 2021-01-22T08:35:43.000Z
         * isLock : 0
         * descriptionToString : {"freeze":"冻结额度","createTime":"创建时间","coinTypeId":"币种类型","userable":"可用额度","memberName":"会员名称","updateTime":"更新时间","id":"foreignKey","coinTypeName":"币种类型名称","memberId":"会员"}
         * stringCreateTime : 2021-01-22 16:31:13
         * stringUpdateTime : 2021-01-22 16:35:43
         */

        private String id;
        private String coinTypeId;
        private String coinTypeName;
        private String memberId;
        private String memberName;
        private double userable;
        private double freeze;
        private String createTime;
        private String updateTime;
        private int isLock;
        private String descriptionToString;

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

        public double getUserable() {
            return userable;
        }

        public void setUserable(double userable) {
            this.userable = userable;
        }

        public double getFreeze() {
            return freeze;
        }

        public void setFreeze(double freeze) {
            this.freeze = freeze;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getIsLock() {
            return isLock;
        }

        public void setIsLock(int isLock) {
            this.isLock = isLock;
        }

        public String getDescriptionToString() {
            return descriptionToString;
        }

        public void setDescriptionToString(String descriptionToString) {
            this.descriptionToString = descriptionToString;
        }

        public String getStringCreateTime() {
            return stringCreateTime;
        }

        public void setStringCreateTime(String stringCreateTime) {
            this.stringCreateTime = stringCreateTime;
        }

        public String getStringUpdateTime() {
            return stringUpdateTime;
        }

        public void setStringUpdateTime(String stringUpdateTime) {
            this.stringUpdateTime = stringUpdateTime;
        }

        private String stringCreateTime;
        private String stringUpdateTime;
    }


    public static class EthBean implements Serializable{
        /**
         * id :
         * coinTypeId : ETH
         * coinTypeName : ETH
         * memberId : ac5d38af5a6a4a6dac01f68e542b303a
         * memberName : null
         * userable : 0.0
         * freeze : 0.0
         * createTime : null
         * updateTime : null
         * isLock : 0
         * descriptionToString : {"freeze":"冻结额度","createTime":"创建时间","coinTypeId":"币种类型","userable":"可用额度","memberName":"会员名称","updateTime":"更新时间","id":"foreignKey","coinTypeName":"币种类型名称","memberId":"会员"}
         * stringCreateTime :
         * stringUpdateTime :
         */

        private String id;
        private String coinTypeId;
        private String coinTypeName;
        private String memberId;
        private Object memberName;
        private double userable;
        private double freeze;
        private Object createTime;
        private Object updateTime;
        private int isLock;

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

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public Object getMemberName() {
            return memberName;
        }

        public void setMemberName(Object memberName) {
            this.memberName = memberName;
        }

        public double getUserable() {
            return userable;
        }

        public void setUserable(double userable) {
            this.userable = userable;
        }

        public double getFreeze() {
            return freeze;
        }

        public void setFreeze(double freeze) {
            this.freeze = freeze;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public int getIsLock() {
            return isLock;
        }

        public void setIsLock(int isLock) {
            this.isLock = isLock;
        }

        public String getDescriptionToString() {
            return descriptionToString;
        }

        public void setDescriptionToString(String descriptionToString) {
            this.descriptionToString = descriptionToString;
        }

        public String getStringCreateTime() {
            return stringCreateTime;
        }

        public void setStringCreateTime(String stringCreateTime) {
            this.stringCreateTime = stringCreateTime;
        }

        public String getStringUpdateTime() {
            return stringUpdateTime;
        }

        public void setStringUpdateTime(String stringUpdateTime) {
            this.stringUpdateTime = stringUpdateTime;
        }

        private String descriptionToString;
        private String stringCreateTime;
        private String stringUpdateTime;
    }


    public static class BoxBean implements Serializable{
        /**
         * id : 024ecea4c4b14e4dae41411da5d007df
         * coinTypeId : BOX
         * coinTypeName : BOX
         * memberId : ac5d38af5a6a4a6dac01f68e542b303a
         * memberName : 15919936559
         * userable : 122.0
         * freeze : 0.0
         * createTime : 2021-01-22T08:31:13.000Z
         * updateTime : 2021-01-22T08:35:43.000Z
         * isLock : 0
         * descriptionToString : {"freeze":"冻结额度","createTime":"创建时间","coinTypeId":"币种类型","userable":"可用额度","memberName":"会员名称","updateTime":"更新时间","id":"foreignKey","coinTypeName":"币种类型名称","memberId":"会员"}
         * stringCreateTime : 2021-01-22 16:31:13
         * stringUpdateTime : 2021-01-22 16:35:43
         */

        private String id;
        private String coinTypeId;
        private String coinTypeName;
        private String memberId;
        private String memberName;
        private double userable;
        private double freeze;
        private String createTime;

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

        public double getUserable() {
            return userable;
        }

        public void setUserable(double userable) {
            this.userable = userable;
        }

        public double getFreeze() {
            return freeze;
        }

        public void setFreeze(double freeze) {
            this.freeze = freeze;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getIsLock() {
            return isLock;
        }

        public void setIsLock(int isLock) {
            this.isLock = isLock;
        }

        public String getDescriptionToString() {
            return descriptionToString;
        }

        public void setDescriptionToString(String descriptionToString) {
            this.descriptionToString = descriptionToString;
        }

        public String getStringCreateTime() {
            return stringCreateTime;
        }

        public void setStringCreateTime(String stringCreateTime) {
            this.stringCreateTime = stringCreateTime;
        }

        public String getStringUpdateTime() {
            return stringUpdateTime;
        }

        public void setStringUpdateTime(String stringUpdateTime) {
            this.stringUpdateTime = stringUpdateTime;
        }

        private String updateTime;
        private int isLock;
        private String descriptionToString;
        private String stringCreateTime;
        private String stringUpdateTime;
    }
}

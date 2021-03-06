package com.treasure.million.bean;

import java.io.Serializable;

/**
 * @author: zt
 * @date: 2020/8/18
 * @name:OreInfo
 */
public class OreInfo implements Serializable {


        private String price_usd;   //": 55839.88,
    private String volume_24h_cny;   //"": 0,
    private String symbol;   //"": "BTC",
    private String last_updated;   //"": 1.615427460657E12,
    private String total_supply;   //"": 0,
    private String  market_cap_cny;   //"": 0,
    private String price_cny;   //"": 362;   //"987.1399,
    private String logo_png;   //"": "http://img.jkabe.com/coins/btc.png",
    private String price_btc;   //"": "0",

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;

    public String getPrice_usd() {
        return price_usd;
    }

    public void setPrice_usd(String price_usd) {
        this.price_usd = price_usd;
    }

    public String getVolume_24h_cny() {
        return volume_24h_cny;
    }

    public void setVolume_24h_cny(String volume_24h_cny) {
        this.volume_24h_cny = volume_24h_cny;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
    }

    public String getTotal_supply() {
        return total_supply;
    }

    public void setTotal_supply(String total_supply) {
        this.total_supply = total_supply;
    }

    public String getMarket_cap_cny() {
        return market_cap_cny;
    }

    public void setMarket_cap_cny(String market_cap_cny) {
        this.market_cap_cny = market_cap_cny;
    }

    public String getPrice_cny() {
        return price_cny;
    }

    public void setPrice_cny(String price_cny) {
        this.price_cny = price_cny;
    }

    public String getLogo_png() {
        return logo_png;
    }

    public void setLogo_png(String logo_png) {
        this.logo_png = logo_png;
    }

    public String getPrice_btc() {
        return price_btc;
    }

    public void setPrice_btc(String price_btc) {
        this.price_btc = price_btc;
    }

    public String getAvailable_supply() {
        return available_supply;
    }

    public void setAvailable_supply(String available_supply) {
        this.available_supply = available_supply;
    }

    public String getMarket_cap_usd() {
        return market_cap_usd;
    }

    public void setMarket_cap_usd(String market_cap_usd) {
        this.market_cap_usd = market_cap_usd;
    }

    public String getPercent_change_1h() {
        return percent_change_1h;
    }

    public void setPercent_change_1h(String percent_change_1h) {
        this.percent_change_1h = percent_change_1h;
    }

    public String getPercent_change_24h() {
        return percent_change_24h;
    }

    public void setPercent_change_24h(String percent_change_24h) {
        this.percent_change_24h = percent_change_24h;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVolume_24h_usd() {
        return volume_24h_usd;
    }

    public void setVolume_24h_usd(String volume_24h_usd) {
        this.volume_24h_usd = volume_24h_usd;
    }

    public String getMax_supply() {
        return max_supply;
    }

    public void setMax_supply(String max_supply) {
        this.max_supply = max_supply;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPercent_change_7d() {
        return percent_change_7d;
    }

    public void setPercent_change_7d(String percent_change_7d) {
        this.percent_change_7d = percent_change_7d;
    }

    private String available_supply;   //"": 0,
    private String market_cap_usd;   //"": 0,
    private String percent_change_1h;   //"": 0,
    private String percent_change_24h;   //"": -0.88,
    private String name;   //"": "btcusdt",
    private String volume_24h_usd;   //"": 24916.8847,
    private String max_supply;   //"": 0,
    private String id;   //"": "btc",
    private String  percent_change_7d;   //"": 0

}

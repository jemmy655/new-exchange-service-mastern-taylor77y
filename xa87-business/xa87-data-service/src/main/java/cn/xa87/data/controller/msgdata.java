package cn.xa87.data.controller;

import java.math.BigDecimal;

public class msgdata {
    String appid;
    String hash;
    String from;
    String to;
    BigDecimal amount;
    BigDecimal fee_amount;
    String memo;
    String add_time;
    int type;
    int state;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getFee_amount() {
        return fee_amount;
    }

    public void setFee_amount(BigDecimal fee_amount) {
        this.fee_amount = fee_amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "msgdata{" +
                "appid='" + appid + '\'' +
                ", hash='" + hash + '\'' +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", amount=" + amount +
                ", fee_amount=" + fee_amount +
                ", memo='" + memo + '\'' +
                ", add_time='" + add_time + '\'' +
                ", type=" + type +
                ", state=" + state +
                '}';
    }
}

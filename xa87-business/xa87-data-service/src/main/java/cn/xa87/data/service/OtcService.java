package cn.xa87.data.service;

import cn.xa87.constant.SmsConstant;

public interface OtcService {

    boolean buyOrder();
    boolean sellOrder();
    void sign();
}
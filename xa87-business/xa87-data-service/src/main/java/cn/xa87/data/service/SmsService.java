package cn.xa87.data.service;

import cn.xa87.constant.SmsConstant;

public interface SmsService {

    boolean sendSms(String phone, String phoneCode, SmsConstant.Sms_Type type);

    boolean sendMail(String mailbox, SmsConstant.Sms_Type type);

}
package cn.xa87.data.controller;

import cn.xa87.common.geetest.sdk.GeetestConfig;
import cn.xa87.common.geetest.sdk.GeetestLib;
import cn.xa87.common.geetest.sdk.GeetestLibResult;
import cn.xa87.common.utils.PhoneFormatCheckUtils;
import cn.xa87.common.web.Response;
import cn.xa87.constant.SmsConstant;
import cn.xa87.data.service.SmsService;
import com.xiaoleilu.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(value = "发送短信/邮件", tags = {"发送短信/邮件"})
@Slf4j
@RestController
@RequestMapping("/sms")
public class SmsController {

    GeetestLib gtLib = new GeetestLib(GeetestConfig.GEETEST_ID, GeetestConfig.GEETEST_KEY);
    @Autowired
    private SmsService smsService;

    @ApiOperation("请求Geetest参数")
    @PostMapping(value = "/geetestCode")
    public Response geetestCode(@RequestParam String randomCode, @RequestParam(required = false, defaultValue = "web") String clientType, HttpServletRequest request) {
        String digestmod = "md5";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("digestmod", digestmod);
        paramMap.put("code", randomCode);
        paramMap.put("client_type", clientType);// web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式

        GeetestLibResult result = gtLib.register(digestmod, paramMap);

        request.getSession().setAttribute(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY, result.getStatus());
        request.getSession().setAttribute("randomCode", randomCode);

        com.xiaoleilu.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(result.getData());
        jsonObject.put("new_captcha", jsonObject.get("new_captcha") == null ? true : jsonObject.get("new_captcha"));
        jsonObject.put("status", result.getStatus());
        return Response.success(jsonObject);
    }


    @ApiOperation("发送短信")
    @PostMapping(value = "/sendSms")
    public Response sendSms(@RequestParam String phone,
                            String phoneCode,
                            SmsConstant.Sms_Type type,
                            @RequestParam(value = "status") Integer statu,
                            @RequestParam String randomCode,
                            @RequestParam String geetest_challenge,
                            @RequestParam String geetest_validate,
                            @RequestParam String geetest_seccode,
                            HttpServletRequest request) {


        Integer status = (Integer) request.getSession().getAttribute(GeetestLib.GEETEST_SERVER_STATUS_SESSION_KEY);

        GeetestLibResult result = null;
        if (null == status) {
            status = statu;
        }
        if (status == 1) {
            Map<String, String> paramMap = new HashMap<String, String>();
            String digestmod = "md5";
            paramMap.put("digestmod", digestmod);
            paramMap.put("code", randomCode);
            result = gtLib.successValidate(geetest_challenge, geetest_validate, geetest_seccode, paramMap);
        } else {
            result = gtLib.failValidate(geetest_challenge, geetest_validate, geetest_seccode);
        }
        if (result.getStatus() == 1) {
            //if (PhoneFormatCheckUtils.isChinaPhoneLegal(phone)) {
                smsService.sendSms(phone, phoneCode, type);
            //}

            return Response.success();
        } else {
            return Response.failure("geestest fail");
        }
    }


    @ApiOperation("发送邮件")
    @PostMapping(value = "/sendMail")
    public Response sendMail(@RequestParam String mailbox, SmsConstant.Sms_Type type) {
        smsService.sendMail(mailbox, type);
        return Response.success();
    }
}

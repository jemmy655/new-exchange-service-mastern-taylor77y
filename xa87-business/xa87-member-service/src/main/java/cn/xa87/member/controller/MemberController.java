package cn.xa87.member.controller;

import cn.xa87.common.exception.DefaultError;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.web.Response;
import cn.xa87.constant.MemConstant;
import cn.xa87.constant.SmsConstant;
import cn.xa87.member.check.HeaderChecker;
import cn.xa87.member.check.LogHeaderChecker;
import cn.xa87.member.mapper.MemberMapper;
import cn.xa87.member.service.MemberService;
import cn.xa87.model.Balance;
import cn.xa87.model.Member;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Api(value = "用户注册/登录/忘记密码/重置密码", tags = {"用户注册/登录/忘记密码/重置密码"})
@RestController
@RequestMapping("/member")
@Slf4j
public class MemberController {

    @Autowired
    MemberMapper memberMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private Xa87RedisRepository redisRepository;

    @ApiOperation("验证用户验证码")
    @PostMapping(value = "/checkSmsCode")
    public Response checkSmsCode(@RequestParam String phMail, String code, SmsConstant.Sms_Type type) {
        return Response.success(memberService.checkSmsCode(phMail, code, type));
    }

    @ApiOperation("用户注册")
    @PostMapping(value = "/register")
    public Response member_register(@RequestBody Member member, @RequestParam String code) {
        return Response.success(memberService.memberRegister(member, code));
    }

    @ApiOperation("用户登录")
    @PostMapping(value = "/login")
    public Response member_login(@RequestParam String phMail, String password, String areaCode) {
        return Response.success(memberService.memberLogin(phMail, password, areaCode));
    }

    @ApiOperation("重置密码")
    @PostMapping(value = "/resetPassword")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response resetPassword(@RequestParam String member, @RequestParam String oldPass, @RequestParam String newPass) {
        return Response.success(memberService.resetPassword(member, oldPass, newPass));
    }

    @ApiOperation("忘记密码")
    @PostMapping(value = "/forgetPass")
    public Response forgetPass(@RequestParam String phMail, String password, @RequestParam String code) {
        return Response.success(memberService.forgetPass(phMail, password, code));
    }

    @ApiOperation("设置支付密码")
    @PostMapping(value = "/setPayPass")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response setPayPass(@RequestParam String member, String password) {
        //this.checkSmsCode(phMail, code, Sms_Type.RESET);
        return Response.success(memberService.setPayPass(member, password));
    }

    @ApiOperation("重置支付密码")
    @PostMapping(value = "/resetPayPass")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response resetPayPass(@RequestParam String member, String oldPass, String password) {
        return Response.success(memberService.resetPayPass(member, oldPass, password));
    }

    @ApiOperation("首次注册设置支付密码")
    @PostMapping(value = "/resetPayPassFirst")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response resetPayPassFirst(@RequestParam String member,String password) {
        return Response.success(memberService.resetPayPassFirst(member, password));
    }

    @ApiOperation("忘记支付密码")
    @PostMapping(value = "/forgetPayPass")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response forgetPayPass(@RequestParam String member, String password, @RequestParam String phMail, @RequestParam String code) {
        return Response.success(memberService.forgetPayPass(member, password, phMail, code));
    }

    @ApiOperation("获取个人详情信息")
    @GetMapping(value = "/getMember")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response getMember(@RequestParam String member) {
        return Response.success(memberService.getMember(member));
    }

    @ApiOperation("修改个人详情信息")
    @PostMapping(value = "/updateMember")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response updateMember(@RequestParam String member, String nikeName, String avatarAddress) {
        return Response.success(memberService.updateMember(member,nikeName,avatarAddress));
    }

    @ApiOperation("绑定邮箱/手机")
    @PostMapping(value = "/setPhMail")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response setPhMail(@RequestParam String member, String phMail, MemConstant.Register_Type regType, @RequestParam String code, String areaCode) {
        return Response.success(memberService.setPhMail(member, phMail, regType, code, areaCode));
    }

    @ApiOperation("根据用户id和币种查询余额, 如果没有该币种balance数据则初始化")
    @PostMapping(value = "/queryBalanc")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response queryBalanc(@RequestHeader("userId") String userId, String currency) {
        try {
            List<Balance> balanceList = memberService.queryBalanc(userId, currency);
            return Response.success(balanceList);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.failure(DefaultError.SYSTEM_INTERNAL_ERROR);
        }
    }

    @ApiOperation(value = "二维码和邀请码", notes = "查询")
    @HeaderChecker(headerNames = {"token", "userId"})
    @ApiImplicitParam(name = "memberId", value = "用户id", required = true, paramType = "query", dataType = "String")
    @GetMapping("code")
    public Response selectCode(@RequestParam("memberId") String memberId) {
        return memberService.selectCode(memberId);
    }

    @ApiOperation(value = "邀请记录", notes = "查看")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping("invite_record")
    @ApiImplicitParam(name = "memberId", value = "用户id", required = true, paramType = "query", dataType = "String")
    public Response selectInviteRecord(@RequestParam("memberId") String memberId) {
        return memberService.selectInviteRecord(memberId);
    }

    @ApiOperation("实名认证")
    @PostMapping(value = "/setAuthen")
    @HeaderChecker(headerNames = {"token", "userId"})
    @LogHeaderChecker
    public Response setAuthen(@RequestParam String member, @RequestParam String name, @RequestParam String cardNo) {
        return Response.success(memberService.setAuthen(member, name, cardNo));
    }

    @ApiOperation("实名认证-认证页全部参数")
    @PostMapping(value = "/setAuthenWithAll")
    @HeaderChecker(headerNames = {"token", "userId"})
    @LogHeaderChecker
    public Response setAuthenWithAll(@RequestParam String member,@RequestParam String areaCode,
                                     @RequestParam String name, @RequestParam String cardNo,
                                     String positiveLink,String sideLink,String handLink) {
        return Response.success(memberService.setAuthenWithArea(member,areaCode, name, cardNo,positiveLink,sideLink,handLink));
    }

    @ApiOperation("设置昵称")
    @PostMapping(value = "/setNickName")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response setNickName(@RequestParam String member, @RequestParam String nickName) {
        return Response.success(memberService.setNickName(member, nickName));
    }

    @ApiOperation("上传微信收款码")
    @PostMapping(value = "/setPayWechat")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response setPayWechat(@RequestParam String member, @RequestParam String name, MultipartFile file, String code) {
        return Response.success(memberService.setPayWechat(member, name, file, code));
    }

    @ApiOperation("上传支付宝收款码")
    @PostMapping(value = "/setPayAliay")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response setPayAliay(@RequestParam String member, @RequestParam String name, MultipartFile file, String code) {
        return Response.success(memberService.setPayAliay(member, name, file, code));
    }

    @ApiOperation("上传身份证照片")
    @PostMapping(value = "/setCardImg")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response setCardImg(@RequestParam String member, MultipartFile positiveFile, MultipartFile sideFile, MultipartFile handLink) {
        return Response.success(memberService.setCardImg(member, positiveFile, sideFile, handLink));
    }

    @ApiOperation("上传身份证照片2")
    @PostMapping(value = "/setCardImg2")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response setCardImg2(@RequestParam String member, MultipartFile positiveFile, MultipartFile sideFile) {
        return Response.success(memberService.setCardImg2(member, positiveFile, sideFile));
    }

    @ApiOperation("绑定银行卡")
    @PostMapping(value = "/setBankInfo")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response setBankInfo(@RequestParam String member, String bankUserName, String bankCard, String bankName, String bankAddress, String code) {
        return Response.success(memberService.setBankInfo(member, bankUserName, bankCard, bankName, bankAddress, code));
    }

    @ApiOperation("申请成为商家")
    @PostMapping(value = "/setStore")
    @HeaderChecker(headerNames = {"token", "userId"})
    @LogHeaderChecker
    public Response setStore(@RequestParam String member, String storeName, String phone, String uuid, String wechatCode, String mail, String card) {
        return Response.success(memberService.setStore(member, storeName, phone, uuid, wechatCode, mail, card));
    }

    @ApiOperation("高级认证,实名认证")
    @PostMapping(value = "/advancedCertification")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response advancedCertification(@RequestParam String member, String name,String code, String positiveLink, String sideLink) {
        return Response.success(memberService.advancedDisplay(member, name, code, positiveLink, sideLink));
    }


    @ApiOperation("获取客服链接接口")
//    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getContactLink")
    public Response getContactLink(@RequestParam Integer type) {
        return Response.success(memberService.getContactLink(type));
    }

    @ApiOperation("校验资金密码")
    @PostMapping(value = "/verifyFundPassword")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response verifyFundPassword(@RequestParam String member, String password) {
        return Response.success(memberService.verifyFundPassword(member, password));
    }

    @ApiOperation("获取账单数据")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getBalanceRecord")
    public Response getBalanceRecord(@RequestParam String member,
                                     Integer type,
                                     @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        return Response.success(memberService.getBalanceRecord(member,type,pageNum,pageSize));
    }

    @ApiOperation("新增收款地址")
    @PostMapping(value = "/saveBillingAddress")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response saveBillingAddress(@RequestParam String member,@RequestParam String currency,@RequestParam String currencyType,@RequestParam String address) {
        return Response.success(memberService.saveBillingAddress(member, currency, currencyType, address));
    }

    @ApiOperation("修改收款地址")
    @PostMapping(value = "/updateBillingAddress")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response updateBillingAddress(@RequestParam String member,@RequestParam Integer baId,@RequestParam String address) {
        return Response.success(memberService.updateBillingAddress(member, baId, address));
    }

    @ApiOperation("获取收款地址列表")
    @HeaderChecker(headerNames = {"token", "userId"})
    @GetMapping(value = "/getBillingAddressList")
    public Response getBillingAddressList(@RequestParam String member,String [] currency) {
        return Response.success(memberService.getBillingAddressList(member, currency));
    }

    @ApiOperation("忘记密码修改")
    @PostMapping(value = "/recomposeInformation")
    @LogHeaderChecker
    public Response recomposeInformation(@RequestParam String phMail, String code, SmsConstant.Sms_Type type, String password) {
        return Response.success(memberService.recomposeInformation(phMail,code,type,password));
    }
}

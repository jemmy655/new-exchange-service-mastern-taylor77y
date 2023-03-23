package cn.xa87.member.service;

import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.web.Response;
import cn.xa87.constant.MemConstant;
import cn.xa87.constant.SmsConstant;
import cn.xa87.model.Balance;
import cn.xa87.model.Member;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MemberService extends IService<Member> {

    boolean checkSmsCode(String phMail, String code, SmsConstant.Sms_Type type);

    Member memberRegister(Member member, String code);

    Map<String, String> memberLogin(String phMail, String password, String areaCode);

    boolean resetPassword(String member, String oldPass, String newPass);

    boolean forgetPass(String phMail, String password, String code);

    boolean setPayPass(String member, String password);

    boolean resetPayPass(String member, String oldPass, String password);

    boolean resetPayPassFirst(String member,String password);

    boolean forgetPayPass(String member, String password, String phMail, String code);

    Member getMember(String member);

    boolean setPhMail(String member, String phMail, MemConstant.Register_Type regType, String code, String areaCode);

    boolean setAuthen(String member, String name, String cardNo);

    boolean setAuthenWithArea(String member, String areaCode,String name, String cardNo,String positiveLink,String sideLink,String handLink);

    boolean setNickName(String member, String nickName);

    boolean setPayWechat(String member, String name, MultipartFile file, String code);

    boolean setPayAliay(String member, String name, MultipartFile file, String code);

    boolean setCardImg(String member, MultipartFile positiveFile, MultipartFile sideFile, MultipartFile handLink);

    boolean setCardImg2(String member, MultipartFile positiveFile, MultipartFile sideFile);

    boolean setStore(String member, String storeName, String phone, String uuid, String wechatCode, String mail, String card);

    boolean setBankInfo(String member, String bankUserName, String bankCard, String bankName, String bankAddress, String code);

    @Transactional(rollbackFor = BusinessException.class)
    List<Balance> queryBalanc(String userId, String currency);

    Response selectCode(String memberId);

    Response selectInviteRecord(String memberId);

    /**
     *
     * @param member 用户ID
     * @param name 用户名称
     * @param code 身份证件号码
     * @param positiveLink 身份证正面
     * @param sideLink 身份证反面
     * @return
     */
    Object advancedDisplay(String member, String name, String code, String positiveLink, String sideLink);

    Object getContactLink(Integer type);

    Response verifyFundPassword(String member, String password);

    Response getBalanceRecord(String member, Integer type, Integer pageNum, Integer pageSize);

    Response saveBillingAddress(String member, String currency, String currencyType, String address);

    Response updateBillingAddress(String member, Integer baId, String address);

    Response getBillingAddressList(String member);

    Response updateMember(String member, String nikeName, String avatarAddress);

    Response recomposeInformation(String phMail, String code, SmsConstant.Sms_Type type, String password);

}
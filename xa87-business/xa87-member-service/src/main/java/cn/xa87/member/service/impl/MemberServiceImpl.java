package cn.xa87.member.service.impl;

import cn.xa87.common.constants.CacheConstants;
import cn.xa87.common.constants.SYSconfig;
import cn.xa87.common.exception.BusinessException;
import cn.xa87.common.exception.TokenException;
import cn.xa87.common.redis.lock.RedisDistributedLock;
import cn.xa87.common.redis.template.Xa87RedisRepository;
import cn.xa87.common.utils.*;
import cn.xa87.common.web.Response;
import cn.xa87.constant.AjaxResultEnum;
import cn.xa87.constant.MemConstant;
import cn.xa87.constant.SmsConstant;
import cn.xa87.constant.SmsConstant.Sms_Type;
import cn.xa87.member.mapper.*;
import cn.xa87.member.product.RegisterProducer;
import cn.xa87.member.service.MemberService;
import cn.xa87.model.*;
import cn.xa87.po.InviteRecordPo;
import cn.xa87.vo.MemberCodeVo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xiaoleilu.hutool.collection.CollUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.xa87.common.constants.BrokerQueueTypeConstant.*;

@Slf4j
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
    @Resource
    private MemberMapper memberMapper;
    @Resource
    private BalanceMapper balanceMapper;
    @Resource
    private PairsMapper pairsMapper;
    @Resource
    private BrokerageRecordMapper brokerageRecordMapper;
    @Resource
    private Xa87RedisRepository redisRepository;
    @Resource
    private RegisterProducer registerProducer;
    @Resource
    private SysConfigMapper sysConfigMapper;
    @Resource
    private CustomerServiceMapper customerServiceMapper;
    @Resource
    private BalanceRecordMapper balanceRecordMapper;
    @Resource
    private BillingAddressMapper billingAddressMapper;

    @Override
    public boolean checkSmsCode(String phMail, String code, Sms_Type type) {
        if(Sms_Type.OTCWECHAT.equals( type)||
                Sms_Type.OTCALIAY.equals( type)||
                Sms_Type.OTCBANK.equals( type)
               ){//暂时不验证OTC短信
            return Boolean.TRUE;
        }
        if (Strings.isBlank(code)) {
            throw new BusinessException(AjaxResultEnum.VERIFICATION_CODE_IS_EMPTY.getMessage());
        }
        String redisCode = redisRepository.get(CacheConstants.DEFAULT_CODE_KEY + phMail + "-" + type);
        if (Strings.isBlank(redisCode)) {
            throw new BusinessException(AjaxResultEnum.VERIFICATION_CODE_HAS_EXPIRED.getMessage());
        }
        if (!code.equals(redisCode)) {
            throw new BusinessException(AjaxResultEnum.VERIFICATION_CODE_ERROR.getMessage());
        }
        return Boolean.TRUE;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Member memberRegister(Member member, String code) {
        if (member.getRegType().equals(MemConstant.Register_Type.PHONE)) {
//            this.checkSmsCode(member.getPhone(), code, Sms_Type.REGISTER);
//            if (member.getPhone().length() != 11) {
//                throw new BusinessException("电话号码位数有误");
//            }
            QueryWrapper<Member> wrapper = new QueryWrapper<Member>();
            wrapper.eq("phone", member.getPhone());
            List<Member> members = memberMapper.selectList(wrapper);
            if (!members.isEmpty()) {
                throw new BusinessException(AjaxResultEnum.THIS_PHONE_REGISTERED.getMessage());
            }
        } else if (member.getRegType().equals(MemConstant.Register_Type.MAIL)) {
//            this.checkSmsCode(member.getMail(), code, Sms_Type.REGISTER);
            String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern p = Pattern.compile(regEx1);
            Matcher m = p.matcher(member.getMail());
            boolean isMatch = m.matches();
            if (!isMatch) {
                throw new BusinessException(AjaxResultEnum.EMAIL_FORMAT_INCORRECT.getMessage());
            }
            QueryWrapper<Member> wrapper = new QueryWrapper<Member>();
            wrapper.eq("mail", member.getMail());
            List<Member> members = memberMapper.selectList(wrapper);
            if (!members.isEmpty()) {
                throw new BusinessException(AjaxResultEnum.THIS_EMAIL_ALREADY_REGISTERED.getMessage());
            }
        }else if (member.getRegType().equals(MemConstant.Register_Type.ACCOUNT)){
            QueryWrapper<Member> wrapper = new QueryWrapper<Member>();
            wrapper.eq("account", member.getAccount());
            List<Member> members = memberMapper.selectList(wrapper);
            if (!members.isEmpty()) {
                throw new BusinessException(AjaxResultEnum.THIS_ACCOUNT_ALREADY_REGISTERED.getMessage());
            }
        }
        if (Strings.isNotBlank(member.getWelCode())) {
            QueryWrapper<Member> wrapperWel = new QueryWrapper<Member>();
            wrapperWel.eq("wel_code", member.getWelCode());
            List<Member> welMembers = memberMapper.selectList(wrapperWel);
            if (welMembers.isEmpty()) {
                throw new BusinessException(AjaxResultEnum.INVITATION_CODE_ERROR.getMessage());
            } else {
                member.setWelMember(welMembers.iterator().next().getId());
                // 2020/02/04 新增存储邀请节点路径 begin
                String welId = welMembers.iterator().next().getId();
                member.setNodePath(welMembers.iterator().next().getNodePath());
                // end
            }
        }
        String welCode = genRandomNum();
        QueryWrapper<Member> wrapperWel = new QueryWrapper<Member>();
        wrapperWel.eq("wel_code", welCode);
        List<Member> welMembers = memberMapper.selectList(wrapperWel);
        if (!welMembers.isEmpty()) {
            welCode = genRandomNum();
        }
        String salt = UUID.randomUUID().toString().replaceAll("-", "");
        member.setPassword(PasswordHelper.encryString(member.getPassword(), salt));
        member.setSalt(salt);
        member.setWelCode(welCode);
        String uuid = genUuid();
        QueryWrapper<Member> wrapperUuid = new QueryWrapper<Member>();
        wrapperUuid.eq("uuid", uuid);
        List<Member> uuidMembers = memberMapper.selectList(wrapperUuid);
        if (!uuidMembers.isEmpty()) {
            uuid = genUuid();
        }
        member.setUuid(uuid);
        member.setUserStatus(MemConstant.Fb_Status.NORMAL.toString());

//        if (Strings.isNotBlank(member.getPassword())) {
//            member.setPayPassword(PasswordHelper.encryString(member.getPayPassword(), member.getSalt()));
//        }

        memberMapper.insert(member);
        registerProducer.putRegister(member.getId());
        registerProducer.putWalletPool(member.getId());
        sendUserRegistry(member);
        return member;
    }

    private void sendUserRegistry(Member member) {
        Map<String, Object> map = new HashMap<>();
        map.put(BROKER_QUEUE_TYPE, USER_REGISTER);
        map.put("memberId", member.getId());
        map.put("registerTime", System.currentTimeMillis());
        map.put("phone", member.getPhone() == null ? "" : member.getPhone());
        map.put("mail", member.getMail() == null ? "" : member.getMail());
        map.put("pId", member.getWelMember() == null ? "" : member.getWelMember());
        registerProducer.putBrokerManage(JSONObject.toJSONString(map));
    }

    public String genRandomNum() {
        int maxNum = 36;
        int i;
        int count = 0;
        char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < 6) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    public static void main(String[] args) {
        int maxNum = 36;
        int i;
        int count = 0;
        char[] str = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < 6) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        System.out.println(pwd.toString());
    }

    public String genUuid() {
        int maxNum = 8;
        int i;
        int count = 0;
        char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while (count < 8) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count++;
            }
        }
        return pwd.toString();
    }

    @Override
    public Map<String, String> memberLogin(String phMail, String password, String areaCode) {
        LambdaQueryWrapper<Member> memberLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberLambdaQueryWrapper.eq(Member::getPhone, phMail).eq(Member::getAreaCode, areaCode);
        List<Member> members = memberMapper.selectList(memberLambdaQueryWrapper);
        if (members.isEmpty()) {
            QueryWrapper<Member> wrapperMail = new QueryWrapper<Member>();
            wrapperMail.eq("mail", phMail);
            members = memberMapper.selectList(wrapperMail);
            if (members.isEmpty()){
                QueryWrapper<Member> wrapperAccount = new QueryWrapper<Member>();
                wrapperAccount.eq("account", phMail);
                members = memberMapper.selectList(wrapperAccount);
            }
        }
        if (members.isEmpty()) {
            throw new BusinessException(AjaxResultEnum.LOGIN_ACCOUNT_WRONG.message());
        }
        Member member = members.iterator().next();
        password = PasswordHelper.encryString(password, member.getSalt());
        if (!password.equals(member.getPassword())) {
            throw new BusinessException(AjaxResultEnum.INCORRECT_LOGIN_PASSWORD.message());
        }
        if (member.getUserStatus().equals(MemConstant.Fb_Status.UNNORMAL.toString())) {
            throw new TokenException(AjaxResultEnum.ACCOUNT_EXCEPTION.message());
        }
        try {
            RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
            String ipAddr = IpUtil.getIpAddr((HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST));
            member.setLastLoginIp(ipAddr);

            memberMapper.updateById(member);
        }catch (Exception e){
            log.error("IP获取或者更新异常",e);
        }

        String token = UUID.randomUUID().toString().replaceAll("-", "");
        redisRepository.setExpire(CacheConstants.MEMBER_TOKEN_KEY + member.getId(), token, 1296000);
        Map<String, String> map = new HashMap<String, String>();
        map.put("token", token);
        map.put("id", member.getId());
        return map;
    }

    @Override
    public boolean resetPassword(String memberId, String oldPass, String newPass) {
        Member member = memberMapper.selectById(memberId);
        if (!member.getPassword().equals(PasswordHelper.encryString(oldPass, member.getSalt()))) {
            throw new BusinessException(AjaxResultEnum.WRONG_PASSWORD.message());
        }
        member.setPassword(PasswordHelper.encryString(newPass, member.getSalt()));
        memberMapper.updateById(member);
        return true;
    }

    @Override
    public boolean forgetPass(String phMail, String password, String code) {
        QueryWrapper<Member> wrapperPhone = new QueryWrapper<Member>();
        wrapperPhone.eq("phone", phMail);
        List<Member> members = memberMapper.selectList(wrapperPhone);
        if (members.isEmpty()) {
            QueryWrapper<Member> wrapperMail = new QueryWrapper<Member>();
            wrapperMail.eq("mail", phMail);
            members = memberMapper.selectList(wrapperMail);
            if (members.isEmpty()) {
                throw new BusinessException(AjaxResultEnum.WRONG_ACCOUNT.message());
            }
        }
        this.checkSmsCode(phMail, code, Sms_Type.RETRIEVE);
        Member member = members.iterator().next();
        password = PasswordHelper.encryString(password, member.getSalt());
        member.setPassword(password);
        memberMapper.updateById(member);
        return true;
    }

    @Override
    public boolean setPayPass(String memberId, String password) {
        Member member = memberMapper.selectById(memberId);
        member.setPayPassword(PasswordHelper.encryString(password, member.getSalt()));
        memberMapper.updateById(member);
        return true;
    }

    @Override
    public Member getMember(String member) {
        Member member1 = memberMapper.selectById(member);
        if (member1.getUserStatus().equals(MemConstant.Fb_Status.UNNORMAL.toString())) {
            throw new TokenException(AjaxResultEnum.ACCOUNT_EXCEPTION.message());
        }
        return member1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setPhMail(String memberId, String phMail, MemConstant.Register_Type regType, String code, String areaCode) {
        Member member = memberMapper.selectById(memberId);
        if (regType.equals(MemConstant.Register_Type.PHONE)) {

//            if (phMail.length() != 11) {
//                throw new BusinessException("电话号码位数有误");
//            }
//			String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
//			Pattern p = Pattern.compile(regex);
//			Matcher m = p.matcher(phMail);
//			boolean isMatch = m.matches();
//			if (!isMatch) {
//				throw new BusinessException("电话号码格式有误");
//			}
            QueryWrapper<Member> wrapper = new QueryWrapper<Member>();
            wrapper.eq("phone", phMail);
            List<Member> members = memberMapper.selectList(wrapper);
            if (!members.isEmpty()) {
                throw new BusinessException(AjaxResultEnum.THIS_PHONE_REGISTERED.message());
            }

            member.setPhone(phMail);
            member.setAreaCode(areaCode);
        } else {
            String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern p = Pattern.compile(regEx1);
            Matcher m = p.matcher(phMail);
            boolean isMatch = m.matches();
            if (!isMatch) {
                throw new BusinessException(AjaxResultEnum.EMAIL_FORMAT_INCORRECT.message());
            }
            QueryWrapper<Member> wrapper = new QueryWrapper<Member>();
            wrapper.eq("mail", phMail);
            List<Member> members = memberMapper.selectList(wrapper);
            if (!members.isEmpty()) {
                throw new BusinessException(AjaxResultEnum.THIS_EMAIL_ALREADY_REGISTERED.message());
            }
            member.setMail(phMail);
        }
        this.checkSmsCode(phMail, code, Sms_Type.SETPHMAIL);
        memberMapper.updateById(member);
        sendQueueAddNumber(member);
        return true;
    }

    private void sendQueueAddNumber(Member member) {
        Map<String, Object> map = new HashMap<>();
        map.put(BROKER_QUEUE_TYPE, USER_ADD_NUMBER);
        map.put("memberId", member.getId());
        map.put("phone", member.getPhone() == null ? "" : member.getPhone());
        map.put("mail", member.getMail() == null ? "" : member.getMail());
        registerProducer.putBrokerManage(JSONObject.toJSONString(map));
    }

    @Override
    public boolean forgetPayPass(String memberId, String password, String phMail, String code) {
        this.checkSmsCode(phMail, code, Sms_Type.PAYRESET);
        Member member = memberMapper.selectById(memberId);
        member.setPayPassword(PasswordHelper.encryString(password, member.getSalt()));
        memberMapper.updateById(member);
        return true;
    }

    @Override
    public boolean resetPayPass(String memberId, String oldPass, String password) {
        Member member = memberMapper.selectById(memberId);
        if (!member.getPayPassword().equals(PasswordHelper.encryString(oldPass, member.getSalt()))) {
            throw new BusinessException(AjaxResultEnum.WRONG_PASSWORD.message());
        }
        member.setPayPassword(PasswordHelper.encryString(password, member.getSalt()));
        memberMapper.updateById(member);
        return true;
    }

    @Override
    public boolean resetPayPassFirst(String memberId,String password){
        Member member = memberMapper.selectById(memberId);
        member.setPayPassword(PasswordHelper.encryString(password, member.getSalt()));
        memberMapper.updateById(member);
        return true;
    }

    @Override
    public List<Balance> queryBalanc(String userId, String currency) {
        Boolean isOne = false;
        if (StrUtil.isNotBlank(currency)) {
            isOne = true;
        }
        QueryWrapper<Balance> wrapper = new QueryWrapper<Balance>();
        wrapper.eq("user_id", userId);
        List<Balance> blanceList = balanceMapper.selectList(wrapper);
        if (isOne) {
            wrapper.eq("currency", currency);
        }
        if (CollUtil.isNotEmpty(blanceList) && isOne) {
            return blanceList;
        } else {
            List<String> currencys = Lists.newArrayList();
            int successCount;
            if (isOne) {
                currencys.add(currency);
                successCount = initAccountByCurrencyList(userId, currencys);
            } else {
                List<Pairs> pairsLists = pairsMapper.selectList(null);
                if (CollUtil.isNotEmpty(blanceList)) {
                    List<String> currencyStrs = Lists.newArrayList();
                    for (int i = 0; i < blanceList.size(); i++) {
                        currencyStrs.add(blanceList.get(i).getCurrency());
                    }
                    for (int i = 0; i < pairsLists.size(); i++) {
                        String tempCurrencyStr = pairsLists.get(i).getTokenCur();
                        if (!currencyStrs.contains(tempCurrencyStr)) {
                            currencys.add(tempCurrencyStr);
                        }
                    }
                } else {
                    for (int i = 0; i < pairsLists.size(); i++) {
                        currencys.add(pairsLists.get(i).getTokenCur());
                    }
                }
                successCount = initAccountByCurrencyList(userId, currencys);
            }
            if (successCount == currencys.size()) {
                return balanceMapper.selectList(wrapper);
            } else {
                throw new BusinessException(AjaxResultEnum.ACCOUNT_INITIALIZATION_FAILED.message());
            }
        }
    }

    @Override
    public Response selectCode(String memberId) {
        MemberCodeVo codeVo = new MemberCodeVo();
        Member member = memberMapper.selectById(memberId);
        BeanUtils.copyProperties(member, codeVo);
        return Response.success(codeVo);
    }

    @Override
    public Response selectInviteRecord(String memberId) {
        // 邀请数量
        Integer inviteNumber = memberMapper.selectInviteNumber(memberId);
        QueryWrapper<BrokerageRecord> brWrapper = new QueryWrapper<>();
        brWrapper.eq("brokerage_user_id", memberId).eq("status", 1).groupBy("consume_user_id").select("consume_user_id",
                "sum(brokerage_number) as brokerageNumber");
        List<BrokerageRecord> recordList = brokerageRecordMapper.selectList(brWrapper);
        List<InviteRecordPo> poList = new ArrayList<>();
        List<String> notIdList = new ArrayList<>();
        // poList add有返佣的
        for (BrokerageRecord br : recordList) {
            // 被邀请人信息
            Member member = memberMapper.selectById(br.getConsumeUserId());
            String param;
            if (member.getPhone() != null) {
                param = HideDataUtil.mobile(member.getPhone());
            } else {
                param = HideDataUtil.email(member.getMail());
            }
            InviteRecordPo po = new InviteRecordPo();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            po.setPhone(param);
            po.setCreateTime(format.format(member.getCreateTime()));
            po.setNumber(br.getBrokerageNumber());
            poList.add(po);
            notIdList.add(member.getId());
        }
        // poList add没有返佣只有邀请记录的
        QueryWrapper<Member> memberWrapper = new QueryWrapper<>();
        memberWrapper.eq("wel_member", memberId).notIn("id", notIdList);
        List<Member> memberList = memberMapper.selectList(memberWrapper);
        for (Member member : memberList) {
            InviteRecordPo recordPo = new InviteRecordPo();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            recordPo.setCreateTime(format.format(member.getCreateTime()));
            String param;
            if (member.getPhone() != null) {
                param = HideDataUtil.mobile(member.getPhone());
            } else {
                param = HideDataUtil.email(member.getMail());
            }
            recordPo.setPhone(param);
            recordPo.setNumber(new BigDecimal("0"));
            poList.add(recordPo);
        }
        // 对poList先按照佣金升序排，在按照创建时间倒序排
        poList.sort(Comparator.comparing(InviteRecordPo::getNumber).thenComparing(InviteRecordPo::getCreateTime)
                .reversed());
        Map<String, Object> result = new HashMap<>();
        result.put("inviteNumber", inviteNumber);
        result.put("inviteRecord", poList);
//        result.put("inviteBrokerage", "待确定");
        result.put("inviteBrokerage", "To be determined");
        return Response.success(result);
    }

    private int initAccountByCurrencyList(String userId, List<String> currencys) {
        int resCount = 0;
        if (CollUtil.isNotEmpty(currencys)) {
            for (int i = 0; i < currencys.size(); i++) {
                // 初始化 currency 账户数据
                Balance balance = new Balance();
                balance.setCurrency(currencys.get(i));
                balance.setUserId(userId);
                int insertSuccessNum = balanceMapper.insert(balance);
                resCount += insertSuccessNum;
            }
        }
        return resCount;
    }

    @Override
    public boolean setAuthen(String memberId, String name, String cardNo) {
        LambdaQueryWrapper<Member> memberLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberLambdaQueryWrapper.eq(Member::getCardNo, cardNo);
        List<Member> members = memberMapper.selectList(memberLambdaQueryWrapper);
        if (members.size() > 0) {
            throw new BusinessException(AjaxResultEnum.THIS_ID_IS_ALREADY_REGISTERED.message());
        }
        String host = "https://yxidcard.market.alicloudapi.com";
        String path = "/idcard";
        String method = "GET";
        String appcode = "a4c7f986186f4a5cb795507ef1764c9e";
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("idcard", cardNo);
        querys.put("realname", name);
        try {
            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
            String result = EntityUtils.toString(response.getEntity());
            JSONObject json = JSONObject.parseObject(result);
            if (json.getString("code").equals("200")) {
                JSONObject jsonData = json.getJSONObject("data");
                String birth = jsonData.getString("birth");
                String sex = jsonData.getString("sex");
                String addr = jsonData.getString("addr");
                Member member = memberMapper.selectById(memberId);
                member.setAddress(addr);
                member.setSex(sex);
                member.setBirth(birth);
                member.setCardNo(cardNo);
                member.setUname(name);
                memberMapper.updateById(member);
            } else {
                throw new BusinessException(AjaxResultEnum.NAME_DOES_NOT_MATCH_ID_NUMBER.message());
            }
        } catch (Exception e) {
            throw new BusinessException(AjaxResultEnum.NAME_DOES_NOT_MATCH_ID_NUMBER.message());
        }
        return true;
    }

    @Override
    public boolean setAuthenWithArea(String memberId, String areaCode,String name, String cardNo,
                                     String positiveLink,String sideLink,String handLink ) {
        LambdaQueryWrapper<Member> memberLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberLambdaQueryWrapper.eq(Member::getCardNo, cardNo);
        List<Member> members = memberMapper.selectList(memberLambdaQueryWrapper);
        if (members.size() > 0) {
            throw new BusinessException(AjaxResultEnum.THIS_ID_IS_ALREADY_REGISTERED.message());
        }
        Member member = memberMapper.selectById(memberId);
        member.setCardNo(cardNo);
        member.setUname(name);
        member.setAreaCode(areaCode);
        member.setPositiveLink(positiveLink);
        member.setSideLink(sideLink);
        member.setHandLink(handLink);
        memberMapper.updateById(member);
        return true;
//        String host = "https://yxidcard.market.alicloudapi.com";
//        String path = "/idcard";
//        String method = "GET";
//        String appcode = "a4c7f986186f4a5cb795507ef1764c9e";
//        Map<String, String> headers = new HashMap<String, String>();
//        headers.put("Authorization", "APPCODE " + appcode);
//        Map<String, String> querys = new HashMap<String, String>();
//        querys.put("idcard", cardNo);
//        querys.put("realname", name);
//        try {
//            HttpResponse response = HttpUtils.doGet(host, path, method, headers, querys);
//            String result = EntityUtils.toString(response.getEntity());
//            JSONObject json = JSONObject.parseObject(result);
//            if (json.getString("code").equals("200")) {
//                JSONObject jsonData = json.getJSONObject("data");
//                String birth = jsonData.getString("birth");
//                String sex = jsonData.getString("sex");
//                String addr = jsonData.getString("addr");
//                Member member = memberMapper.selectById(memberId);
//                member.setAddress(addr);
//                member.setSex(sex);
//                member.setBirth(birth);
//                member.setCardNo(cardNo);
//                member.setUname(name);
//                member.setAreaCode(areaCode);
//                member.setPositiveLink(positiveLink);
//                member.setSideLink(sideLink);
//                member.setHandLink(handLink);
//                memberMapper.updateById(member);
//            } else {
//                throw new BusinessException(AjaxResultEnum.NAME_DOES_NOT_MATCH_ID_NUMBER.message());
//            }
//        } catch (Exception e) {
//            throw new BusinessException(AjaxResultEnum.NAME_DOES_NOT_MATCH_ID_NUMBER.message());
//        }
//        return true;
    }

    @Override
    public boolean setNickName(String memberId, String nickName) {
        Member member = memberMapper.selectById(memberId);
        if (member.getNickName() != null) {
            throw new BusinessException(AjaxResultEnum.NICKNAME_CAN_ONLY_BE_SET_ONCE.message());
        }
        member.setNickName(nickName);
        memberMapper.updateById(member);
        return false;
    }

    @Override
    public boolean setPayWechat(String memberId, String name, MultipartFile file, String code) {
        Member member = memberMapper.selectById(memberId);
        if (member.getUname() == null) {
            throw new BusinessException(AjaxResultEnum.PLEASE_AUTHENTICATE_FIRST.message());
        }
        this.checkSmsCode(member.getPhone(), code, SmsConstant.Sms_Type.OTCWECHAT);
        if (file == null) {
            member.setWechatName(name);
            memberMapper.updateById(member);
        } else {
            try {
                String url = getAliyunClient().uploadObject2OSS(new ByteArrayInputStream(file.getBytes()), file.getOriginalFilename());
                member.setPayWechat(url);
                member.setWechatName(name);
                memberMapper.updateById(member);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean setPayAliay(String memberId, String name, MultipartFile file, String code) {
        Member member = memberMapper.selectById(memberId);
        if (member.getUname() == null) {
            throw new BusinessException(AjaxResultEnum.PLEASE_AUTHENTICATE_FIRST.message());
        }
        this.checkSmsCode(member.getPhone(), code, SmsConstant.Sms_Type.OTCALIAY);
        if (file == null) {
            member.setAliayName(name);
            memberMapper.updateById(member);
        } else {
            try {
                String url = getAliyunClient().uploadObject2OSS(new ByteArrayInputStream(file.getBytes()), file.getOriginalFilename());
                member.setPayAliay(url);
                member.setAliayName(name);
                memberMapper.updateById(member);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public boolean setCardImg(String memberId, MultipartFile positiveFile, MultipartFile sideFile, MultipartFile handLink) {
        Member member = memberMapper.selectById(memberId);
        if (member.getUname() == null) {
            throw new BusinessException(AjaxResultEnum.PLEASE_AUTHENTICATE_FIRST.message());
        }
        try {
            member.setPositiveLink(getAliyunClient().uploadObject2OSS(new ByteArrayInputStream(positiveFile.getBytes()), positiveFile.getOriginalFilename()));
            member.setSideLink(getAliyunClient().uploadObject2OSS(new ByteArrayInputStream(sideFile.getBytes()), sideFile.getOriginalFilename()));
            member.setHandLink(getAliyunClient().uploadObject2OSS(new ByteArrayInputStream(handLink.getBytes()), handLink.getOriginalFilename()));
            member.setCardState(MemConstant.Card_Sate.WAIT);
            memberMapper.updateById(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean setCardImg2(String memberId, MultipartFile positiveFile, MultipartFile sideFile) {
        Member member = memberMapper.selectById(memberId);
        if (member.getUname() == null) {
            throw new BusinessException(AjaxResultEnum.PLEASE_AUTHENTICATE_FIRST.message());
        }
        try {
            member.setPositiveLink(getAliyunClient().uploadObject2OSS(new ByteArrayInputStream(positiveFile.getBytes()), positiveFile.getOriginalFilename()));
            member.setSideLink(getAliyunClient().uploadObject2OSS(new ByteArrayInputStream(sideFile.getBytes()), sideFile.getOriginalFilename()));
            member.setCardState(MemConstant.Card_Sate.WAIT);
            memberMapper.updateById(member);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean setStore(String memberId, String storeName, String phone, String uuid, String wechatCode, String mail,
                            String card) {
        if (StrUtil.isBlank(storeName)) {
            throw new BusinessException(AjaxResultEnum.STORE_NAME_ALLOWED_EMPTY.message());
        }
//        if (StrUtil.isBlank(wechatCode)) {
//            throw new BusinessException("微信号码不允许为空");
//        }
        if (StrUtil.isBlank(mail)) {
            throw new BusinessException(AjaxResultEnum.EMAIL_NOT_ALLOWED_EMPTY.message());
        }
        Member member = memberMapper.selectById(memberId);
        if (member.getUname() == null) {
            throw new BusinessException(AjaxResultEnum.PLEASE_AUTHENTICATE_FIRST.message());
        }
        if (member.getCardState() == null) {
            throw new BusinessException(AjaxResultEnum.PLEASE_AUTHENTICATE_FIRST.message());
        }
        if (!member.getCardState().equals(MemConstant.Card_Sate.PASS)) {
            throw new BusinessException(AjaxResultEnum.ADVANCED_AUTHENTICATION_NOT_COMPLETED.message());
        }
        setStoreBalance(memberId);
        member.setStoreName(storeName);
        member.setStoreState(MemConstant.Card_Sate.WAIT);
        memberMapper.updateById(member);
        return true;
    }

    private void setStoreBalance(String userId) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(redisRepository.getRedisTemplate());
        boolean lock_coin = redisDistributedLock.lock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId,
                5000, 50, 100);
        if (lock_coin) {
            QueryWrapper<Balance> wrapper = new QueryWrapper<Balance>();
            wrapper.eq("user_id", userId);
            wrapper.eq("currency", "USDT");
            Balance userBalance = balanceMapper.selectOne(wrapper);
            if (userBalance.getAssetsBalance().compareTo(new BigDecimal("1000")) == -1) {
                redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
                throw new BusinessException(AjaxResultEnum.INSUFFICIENT_BALANCE.message());
            }
            userBalance.setAssetsBalance(userBalance.getAssetsBalance().subtract(new BigDecimal("1000")));
            userBalance.setAssetsBlockedBalance(userBalance.getAssetsBlockedBalance().add(new BigDecimal("1000")));
            balanceMapper.updateById(userBalance);
            redisDistributedLock.releaseLock(CacheConstants.MEMBER_BALANCE_COIN_KEY + userId);
        } else {
            setStoreBalance(userId);
        }

    }

    @Override
    public boolean setBankInfo(String memberId, String bankUserName, String bankCard, String bankName,
                               String bankAddress, String code) {
        Member member = memberMapper.selectById(memberId);
        this.checkSmsCode(member.getPhone(), code, SmsConstant.Sms_Type.OTCBANK);
        member.setBankUserName(bankUserName);
        member.setBankCard(bankCard);
        member.setBankName(bankName);
        member.setBankAddress(bankAddress);
        memberMapper.updateById(member);
        return false;
    }


    @Override
    public Object advancedDisplay(String memberId, String name, String code, String positiveLink, String sideLink) {
        LambdaQueryWrapper<Member> memberLambdaQueryWrapper = new LambdaQueryWrapper<>();
        memberLambdaQueryWrapper.eq(Member::getCardNo, code);
        List<Member> members = memberMapper.selectList(memberLambdaQueryWrapper);
        if (members.size() > 0) {
            throw new BusinessException(AjaxResultEnum.THIS_ID_IS_ALREADY_REGISTERED.message());
        }

        try {
            Member member = memberMapper.selectById(memberId);

            member.setPositiveLink(positiveLink);
            member.setSideLink(sideLink);
            member.setUname(name);
            member.setCardNo(code);
            member.setIsBok(1);
            member.setCardState(MemConstant.Card_Sate.WAIT);
            memberMapper.updateById(member);
        }catch (Exception e){
            log.error("高级认证操作失败:{}",e.getMessage());
        }

        return "SUCCESS";
    }

    @Override
    public Object getContactLink(Integer type) {
        QueryWrapper<CustomerService> wrapper = new QueryWrapper<>();
        wrapper.eq("status", 1);
        wrapper.eq("type", type);
        List<CustomerService> customerServices = customerServiceMapper.selectList(wrapper);
        if(customerServices.size() > 0){
            Random random = new Random();
            return customerServices.get(random.nextInt(customerServices.size()));
        }
        return null;
    }

    @Override
    public Response verifyFundPassword(String memberId, String password) {
        Member member = memberMapper.selectById(memberId);
        if (!member.getPayPassword().equals(PasswordHelper.encryString(password, member.getSalt()))) {
            throw new BusinessException(AjaxResultEnum.WRONG_PASSWORD.message());
        }
        return Response.success();
    }

    @Override
    public Response getBalanceRecord(String member, Integer type, Integer pageNum, Integer pageSize) {
        QueryWrapper<BalanceRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", member).orderByDesc("create_time");
        if (null != type) {
            wrapper.eq("data_classification", type);
        }
        wrapper.ne("balance_difference",0);
//        wrapper.notIn("balance_type",52,54);
        IPage<BalanceRecord> page = balanceRecordMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return Response.success(page);
    }

    @Override
    public Response saveBillingAddress(String member, String currency, String currencyType, String address) {
        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setMemberId(member);
        billingAddress.setCurrency(currency);
        billingAddress.setCurrencyType(currencyType);
        billingAddress.setAddress(address);
        billingAddress.setCreateTime(new Date());

        return Response.success(billingAddressMapper.insert(billingAddress));
    }

    /**
     *
     * @param member  用户id
     * @param baId    提币id
     * @param address
     * @return
     */
    @Override
    public Response updateBillingAddress(String member, Integer baId, String address) {
        BillingAddress billingAddress = billingAddressMapper.selectById(baId);
        if(null != billingAddress){
            billingAddress.setAddress(address);
            billingAddressMapper.updateById(billingAddress);
        }
        return Response.success();
    }

    @Override
    public Response getBillingAddressList(String member, String [] currency) {
        String ids=null;
        for (int i=0;i<currency.length;i++){
            if (ids==null){
                ids=currency[i];
            }else {
                ids += ',' + currency[i];
            }
        }
        QueryWrapper<BillingAddress> wrapper = new QueryWrapper<>();
        wrapper.eq("member_id", member).in("currency", ids).orderByDesc("create_time");
        return Response.success(billingAddressMapper.selectList(wrapper));
    }

    @Override
    public Response updateMember(String memberId, String nikeName, String avatarAddress) {
        Member member = memberMapper.selectById(memberId);
        if (null != nikeName) {
            member.setNickName(nikeName);
        }
        if(null != avatarAddress){
            member.setHandLink(avatarAddress);
        }
        return Response.success(memberMapper.updateById(member));
    }

    @Override
    public Response recomposeInformation(String phMail, String code, Sms_Type type, String password) {
        if(checkSmsCode(phMail,code,type)){
            Member member =  memberMapper.findMemberByPhoneOrMail(phMail);
            if(null != member){
                member.setPassword(PasswordHelper.encryString(password, member.getSalt()));
                memberMapper.updateById(member);
            }
        }
        return Response.success();
    }


    public AliyunClient getAliyunClient() {
        final String folder = sysConfigMapper.selectByParamKey(SYSconfig.FOLDER);
        final String access_key_id = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_ID);
        final String bucket = sysConfigMapper.selectByParamKey(SYSconfig.Bucket);
        final String access_key_secret = sysConfigMapper.selectByParamKey(SYSconfig.ACCESS_KEY_SECRET);
        final String regionid = sysConfigMapper.selectByParamKey(SYSconfig.REGIONID);
        final String endpoint = sysConfigMapper.selectByParamKey(SYSconfig.ENDPOINT);
        return new AliyunClient(endpoint, access_key_id, access_key_secret, bucket, folder);
    }
}

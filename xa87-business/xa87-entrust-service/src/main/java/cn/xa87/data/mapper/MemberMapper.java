package cn.xa87.data.mapper;

import cn.xa87.model.Member;
import cn.xa87.po.php.BrokerOrderDto;
import cn.xa87.po.php.SettlementDto;
import cn.xa87.vo.php.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberMapper extends BaseMapper<Member> {

    /**
     * 查询memberId的邀请成功次数
     *
     * @param memberId memberId
     * @return
     */
    Integer selectInviteNumber(@Param("memberId") String memberId);

    /**
     * 校验账号和uuid
     *
     * @param phone 手机号
     * @param mail  邮箱
     * @param uuid  uuid
     * @return
     */
    MemberVo verify(@Param("phone") String phone,
                    @Param("mail") String mail,
                    @Param("uuid") String uuid);

    /**
     * 查看伞级用户信息列表
     *
     * @param page          page
     * @param nodePath      邀请节点路径
     * @param currency      币种
     * @param myselfPhone   自己手机号
     * @param myselfMail    自己邮箱
     * @param superiorPhone 上级手机号
     * @param superiorMail  上级邮箱
     * @return
     */
    IPage<SubordinateMemberVo> selectSubordinateMember(Page<Integer> page,
                                                       @Param("nodePath") String nodePath,
                                                       @Param("currency") String currency,
                                                       @Param("myselfPhone") String myselfPhone,
                                                       @Param("myselfMail") String myselfMail,
                                                       @Param("superiorPhone") String superiorPhone,
                                                       @Param("superiorMail") String superiorMail);

    /**
     * 查看伞级用户信息列表
     *
     * @param page          page
     * @param nodePath      邀请节点路径
     * @param myselfPhone   自己手机号
     * @param myselfMail    自己邮箱
     * @param superiorPhone 上级手机号
     * @param superiorMail  上级邮箱
     * @return
     */
    IPage<BrokerManagementVo> selectBrokerManagement(Page<Integer> page,
                                                     @Param("nodePath") String nodePath,
                                                     @Param("myselfPhone") String myselfPhone,
                                                     @Param("myselfMail") String myselfMail,
                                                     @Param("superiorPhone") String superiorPhone,
                                                     @Param("superiorMail") String superiorMail);

    /**
     * 获取伞级用户订单列表
     *
     * @param page     page
     * @param currency currency
     * @param nodePath 邀请节点路径
     * @param phone    手机号
     * @param mail     邮箱
     * @param dto      BrokerOrderDto
     * @return
     */
    IPage<BrokerOrderVo> getOrderList(Page<Integer> page,
                                      @Param("currency") String currency,
                                      @Param("nodePath") String nodePath,
                                      @Param("phone") String phone,
                                      @Param("mail") String mail,
                                      @Param("dto") BrokerOrderDto dto);

    /**
     * 获取伞级用户结算列表
     *
     * @param page        page
     * @param nodePath    邀请节点路径
     * @param phone       手机号
     * @param mail        邮箱
     * @param currency    币种
     * @param orderStatus 订单状态
     * @param dto         dto
     * @return
     */
    IPage<SettlementVo> getSettlementList(Page<Integer> page,
                                          @Param("nodePath") String nodePath,
                                          @Param("phone") String phone,
                                          @Param("mail") String mail,
                                          @Param("currency") String currency,
                                          @Param("orderStatus") String orderStatus,
                                          @Param("dto") SettlementDto dto);

    /**
     * 获取nodePath下的memberIdList
     *
     * @param nodePath nodePath
     * @return
     */
    List<String> selectOverview(@Param("nodePath") String nodePath);

    /**
     * 修改经纪人等级
     *
     * @param column       列名
     * @param nodePath     邀请路径
     * @param memberIdList memberIdList
     * @return
     */
    Integer updateBrokerGrade(@Param("column") String column,
                              @Param("memberId") String memberId,
                              @Param("nodePath") String nodePath,
                              @Param("list") List<String> memberIdList);
}
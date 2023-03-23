package cn.xa87.member.mapper;

import cn.xa87.model.MemberRecharge;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface MemberRechargeMapper extends BaseMapper<MemberRecharge> {

    IPage<MemberRecharge> rechargeCurrencyRecord(Page<Object> objectPage, String memberId, String currency);
}

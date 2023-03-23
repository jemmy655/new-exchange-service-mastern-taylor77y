package cn.xa87.member.mapper;

import cn.xa87.model.DepositHistory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

public interface DepositHistoryMapper extends BaseMapper<DepositHistory> {

    IPage<DepositHistory> selectRechargeRecord(Page<Object> objectPage, String memberId, String currency);
}
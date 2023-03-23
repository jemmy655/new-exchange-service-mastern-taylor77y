package cn.xa87.job.mapper;

import cn.xa87.po.WalletPo;
import cn.xa87.vo.WalletVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface WalletMapper extends BaseMapper<WalletVo> {

    List<WalletPo> selectUserList();
}
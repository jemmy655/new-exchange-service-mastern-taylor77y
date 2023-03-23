package cn.xa87.job.mapper;

import cn.xa87.model.BrokerageRecord;
import cn.xa87.model.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface MemberMapper extends BaseMapper<Member> {

    int updateBrokerage(@Param("list") List<BrokerageRecord> brokerageRecordList,
                        @Param("brokerageUpper") BigDecimal BrokerageUpper);

    @Select("SELECT id from t_member")
    List<String> getIds();
}

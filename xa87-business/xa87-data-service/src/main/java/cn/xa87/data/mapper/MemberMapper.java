package cn.xa87.data.mapper;

import cn.xa87.model.Member;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;

import java.util.Map;

public interface MemberMapper extends BaseMapper<Member> {
    @Insert("INSERT INTO `t_message_record` ( `account`, `msg`, `type`, `create_time`) VALUES ( #{account}, #{msg}, #{type}, NOW());")
    Integer insertRecord(Map<String, Object> map);
}
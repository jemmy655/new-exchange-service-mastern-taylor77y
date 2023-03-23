package cn.xa87.member.service.impl;

import cn.xa87.member.mapper.MemberMapper;
import cn.xa87.member.service.MemberService;
import cn.xa87.model.Member;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

}

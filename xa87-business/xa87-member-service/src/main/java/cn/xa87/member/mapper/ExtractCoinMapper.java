package cn.xa87.member.mapper;

import cn.xa87.model.ExtractCoin;
import cn.xa87.vo.ExtractCoinVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExtractCoinMapper extends BaseMapper<ExtractCoin> {

    /**
     * 提现记录
     *
     * @param page     page
     * @param memberId memberId
     * @param state    状态
     * @param currency 币种
     * @return
     */
    IPage<ExtractCoinVo> selectExtractRecording(Page<Integer> page,
                                                @Param("memberId") String memberId,
                                                @Param("state") String state,
                                                @Param("currency") String currency);


    /**
     * 提币币种
     *
     * @param memberId
     * @return
     */
    List<String> selectWithdrawCurrency(@Param("memberId") String memberId);

    /**
     * 获取提现总数
     *
     * @param memberIdList memberIdList
     * @param currency     币种
     * @return
     */
    BigDecimal selectTotalFee(@Param("list") List<String> memberIdList,
                              @Param("currency") String currency);
}
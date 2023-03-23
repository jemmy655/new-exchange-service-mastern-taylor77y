package cn.xa87.member.mapper;

import cn.xa87.model.TansferInfo;
import cn.xa87.vo.php.DealVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface TansferInfoMapper extends BaseMapper<TansferInfo> {
    /**
     * 查看伞下用户出入金记录
     *
     * @param page            page
     * @param nodePath        nodePath
     * @param phone           手机号
     * @param mail            邮箱
     * @param createTimeBegin 开始时间
     * @param createTimeEnd   结束时间
     * @param transferType    类型
     * @return
     */
    IPage<DealVo> selectDeal(Page<Integer> page,
                             @Param("nodePath") String nodePath,
                             @Param("phone") String phone,
                             @Param("mail") String mail,
                             @Param("createTimeBegin") String createTimeBegin,
                             @Param("createTimeEnd") String createTimeEnd,
                             @Param("transferType") String transferType);
}
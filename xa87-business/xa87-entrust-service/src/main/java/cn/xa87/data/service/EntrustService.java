package cn.xa87.data.service;

import cn.xa87.common.web.Response;
import cn.xa87.model.Entrust;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface EntrustService extends IService<Entrust> {

    boolean setEntrust(Entrust entrust);

    Response getHistoryEntrust(String member, String pairsName, Integer pageNum, Integer pageSize);

    List<Entrust> getEntrustList(String member, String pairsName);

    boolean closeEntrust(String entrust);

    boolean test();

    Object setEntrustBackstage(String entrust);

    Object createKlineData(Integer number, Boolean isClear, String coin);

}
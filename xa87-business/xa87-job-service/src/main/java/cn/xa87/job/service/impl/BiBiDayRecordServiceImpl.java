package cn.xa87.job.service.impl;

import cn.xa87.job.mapper.BiBiDayRecordMapper;
import cn.xa87.job.mapper.PairsMapper;
import cn.xa87.job.service.BiBiDayRecordService;
import cn.xa87.model.BiBiDayRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BiBiDayRecordServiceImpl implements BiBiDayRecordService {

    @Autowired
    BiBiDayRecordMapper biBiDayRecordMapper;
    @Autowired
    PairsMapper pairsMapper;

    @Override
    public void isBurst() {

        List<String> pairNames = pairsMapper.getPairName();
        for (String pairName : pairNames) {
            BigDecimal sum_buy = biBiDayRecordMapper.getSum(pairName, "BUY");
            BigDecimal sum_sell = biBiDayRecordMapper.getSum(pairName, "SELL");
            BiBiDayRecord biBiDayRecord = new BiBiDayRecord();
            biBiDayRecord.setCurrency(pairName);
            biBiDayRecord.setCreateTime(new Date());
            biBiDayRecord.setSumBibiBuy(sum_buy);
            biBiDayRecord.setSumBibiSell(sum_sell);
            biBiDayRecordMapper.insert(biBiDayRecord);
        }

        //法币
        List<String> fbCurrencys = biBiDayRecordMapper.getFbCurrency();
        for (String fbCurrency : fbCurrencys) {
            BigDecimal buy = biBiDayRecordMapper.getFbSum(fbCurrency, "BUY");
            BigDecimal sell = biBiDayRecordMapper.getFbSum(fbCurrency, "SELL");
            Map<String, Object> map = new HashMap<>();
            map.put("currency", fbCurrency);
            map.put("sum_buy", buy);
            map.put("sum_sell", sell);
            biBiDayRecordMapper.saveFbSum(map);
        }

    }
}

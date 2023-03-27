package cn.xa87.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderCheck implements Serializable {
    private String name;

    private BigDecimal minx;

    private BigDecimal min;

    private Date buyDate; //买入时间

    private Date startDate;//起息日

    private String distribute;//每天

    private Date endDate;//结束时间

    private String ransomRate;//

    private String predictRate;//预计收益

    private String todayRate;//今天收益率

    private String buyPairName;//购买币
    private String periodDay;//周期
}

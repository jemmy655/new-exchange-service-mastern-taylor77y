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

    private Date buyDate;

    private Date startDate;

    private String distribute;

    private Date endDate;

    private String ransomRate;

    private String predictRate;

    private String todayRate;
}

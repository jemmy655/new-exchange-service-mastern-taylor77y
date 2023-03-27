package cn.xa87.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("n_contract_record")
public class NContractRecord {
    @TableId(value = "id", type = IdType.ID_WORKER)
    private Long id;
    private String pairsName;
    private Double number;
    private Integer type;
    private Integer win;
    private Double yield;
    private Integer status;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
}

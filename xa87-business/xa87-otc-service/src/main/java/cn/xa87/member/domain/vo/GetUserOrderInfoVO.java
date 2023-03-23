package cn.xa87.member.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author peter
 * @create 2020-07-08-18:28
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserOrderInfoVO implements Serializable {

    public interface GetUserOrderInfo { }

    @ApiModelProperty(value = "区号")
    @NotBlank(message = "区号不能为空", groups = {GetUserOrderInfo.class})
    private String areaCode;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    @ApiModelProperty(value = "手机号")
    @NotBlank(message = "区号不能为空", groups = {GetUserOrderInfo.class})
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}


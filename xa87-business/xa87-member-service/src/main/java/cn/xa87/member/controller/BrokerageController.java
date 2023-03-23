package cn.xa87.member.controller;

import cn.xa87.common.exception.DefaultError;
import cn.xa87.common.web.Response;
import cn.xa87.member.service.BrokerageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 回扣榜单.
 *
 * @author ZYQ
 * @date 2020/2/4 16:45
 */
@Slf4j
@Api(tags = "回扣榜单")
@RestController
@RequestMapping("brokerage_top")
public class BrokerageController {

    @Autowired
    private BrokerageService brokerageService;

    @ApiOperation(value = "查看", notes = "每月")
    @GetMapping("month")
    public Response month() {
        try {
            return brokerageService.selectMonth();
        } catch (Exception e) {
            log.error(e.getMessage());
            return Response.failure(DefaultError.SYSTEM_INTERNAL_ERROR);
        }
    }
}

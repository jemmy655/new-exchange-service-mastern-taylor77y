package cn.xa87.member.controller;

import cn.hutool.core.util.StrUtil;
import cn.xa87.common.web.Response;
import cn.xa87.member.check.HeaderChecker;
import cn.xa87.member.check.LogHeaderChecker;
import cn.xa87.member.mapper.PEProjectMapper;
import cn.xa87.member.service.PEOrderService;
import cn.xa87.model.PEOrder;
import cn.xa87.model.PEProject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Api(value = "用户私募接口", tags = {"用户私募接口"})
@RestController
@RequestMapping("/ieo")
@Slf4j
public class IEOController {
    @Autowired
    private PEOrderService peOrderService;

    @Autowired
    private PEProjectMapper peProjectMapper;

    @ApiOperation("IEO项目列表")
    @GetMapping(value = "/peList")
    public Response resetPassword(String status) {
        QueryWrapper<PEProject> objectQueryWrapper = new QueryWrapper<PEProject>();
        objectQueryWrapper.orderByAsc("order_index");
        objectQueryWrapper.eq("status", status);
        List<PEProject> peProjects = peProjectMapper.selectList(objectQueryWrapper);
        return Response.success(peProjects);
    }

    @ApiOperation("IEO项目募资查询")
    @GetMapping(value = "/peOrder")
    public Response currencyList(@RequestParam String member, String peId) {
        QueryWrapper<PEOrder> objectQueryWrapper = new QueryWrapper<PEOrder>();
        if (!StrUtil.isNullOrUndefined(peId)) {
            objectQueryWrapper.eq("pe_id", peId);
        }
        objectQueryWrapper.eq("member", member);
        List<PEOrder> peOrders = peOrderService.list(objectQueryWrapper);
        return Response.success(peOrders);
    }

    @ApiOperation("IEO购买份额下单")
    @HeaderChecker(headerNames = {"token", "userId"})
    @PostMapping(value = "/order")
    @LogHeaderChecker
    public Response placeAnOrder(@RequestParam String projectId, @RequestParam String member, @RequestParam BigDecimal num) {
        return Response.success(peOrderService.order(projectId, member, num));
    }
}

package cn.xa87.data.controller;

import cn.xa87.common.web.Response;
import cn.xa87.data.service.ContractMulService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "交易配置", tags = {"交易配置"})
@RestController
@RequestMapping("/contractMul")
public class ContractMulController {

    @Autowired
    private ContractMulService contractMulService;

    @ApiOperation("获取交易配置")
    @GetMapping(value = "/getContractMul")
    public Response getContractCfg(String pairsName) {
        return Response.success(contractMulService.getContractMul(pairsName));
    }

    @ApiOperation("获取杠杆列表")
    @GetMapping(value = "/getLevers")
    public Response getLevers(@RequestParam String pairsName) {
        return Response.success(contractMulService.getLevers(pairsName));
    }

    @ApiOperation("获取指数价格")
    @GetMapping(value = "/getIndexPrice")
    public Response getIndexPrice(@RequestParam String pairsName) {
        return Response.success(contractMulService.getIndexPrice(pairsName));
    }
}

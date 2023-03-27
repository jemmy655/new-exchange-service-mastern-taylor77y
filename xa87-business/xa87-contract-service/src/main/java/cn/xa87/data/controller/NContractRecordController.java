package cn.xa87.data.controller;

import cn.xa87.common.web.Response;
import cn.xa87.data.service.NContractRecordService;
import cn.xa87.model.NContractRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(value = "新交易", tags = {"交易"})
@RestController
@RequestMapping("/new/contract")
public class NContractRecordController {
    @Resource
    NContractRecordService nContractRecordService;

    @ApiOperation("开多,开空,平多,平空")
    @PostMapping("/add")
    public Response add(@RequestBody NContractRecord contract){

        return Response.success();
    }
}

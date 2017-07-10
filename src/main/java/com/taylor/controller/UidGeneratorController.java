package com.taylor.controller;

import com.taylor.common.CommonResponse;
import com.taylor.common.ResponseStatusEnum;
import com.taylor.service.UidGateWayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Michael.Wang
 * @date 2017/4/21
 */
@Slf4j
@Controller
public class UidGeneratorController {

    @Autowired
    private UidGateWayService uidGateWayService;

    /**
     * 获取uid
     *
     * @return uid
     */
    @RequestMapping(value = "/api/common/uid/direct/{biz_type}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse<Long> generateUID(@PathVariable("biz_type") String bizType) {
        Long uid = uidGateWayService.generateUID(bizType);
        return new CommonResponse<>(uid);
    }

    @RequestMapping(value = "/api/common/uid/direct/init/{moudle}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse init(@PathVariable String moudle) {
        uidGateWayService.init(moudle);
        CommonResponse response = new CommonResponse();
        response.setCode(ResponseStatusEnum.SUCCESS.getCode());
        return response;
    }

}

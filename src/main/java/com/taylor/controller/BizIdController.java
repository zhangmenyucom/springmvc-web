package com.taylor.controller;

import com.taylor.common.CommonResponse;
import com.taylor.service.BizIdService;
import com.taylor.uuid.entity.BizIdEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Michael.Wang
 * @date 2017/4/21
 */
@Slf4j
@Controller
public class BizIdController {
    @Autowired
    private BizIdService bizIdService;

    /**
     * 生成最大Id
     *
     * @return
     */
    @RequestMapping(value = "/api/common/uid/generate/{biz_type}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse<String> generate(@PathVariable("biz_type") String bizType) {
        String maxId = String.valueOf(bizIdService.fetchNextMaxId(bizType));
        log.debug("maxId  generated is :{}", maxId);
        return new CommonResponse<String>(maxId);
    }

    /**
     * @desc模块相关所有id
     * @param module
     * @return
     */
    @RequestMapping(value = "/api/common/uid/fetch/{module}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResponse<List<BizIdEntity>> fetchAll(@PathVariable("module") String module) {
        log.info("start to fetch for module{}",module);
        List<BizIdEntity> entities = bizIdService.fetchAll(module);
        log.debug("bizid entities size is:{}", ((entities != null) ? entities.size() : 0));
        return new CommonResponse<>(entities);
    }
}

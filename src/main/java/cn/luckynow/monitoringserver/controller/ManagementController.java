package cn.luckynow.monitoringserver.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.entity.WarningLink;
import cn.luckynow.monitoringserver.service.IWarningLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/management")
@Slf4j
public class ManagementController {

    @Autowired
    private IWarningLinkService iWarningLinkService;

    @PostMapping("/link")
    public Result addLink(@RequestBody String linkData) {
        JSONObject linkDataJson = JSONUtil.parseObj(linkData);
        String linkUrl = linkDataJson.get("linkUrl", String.class);
        WarningLink warningLink = new WarningLink();
        warningLink.setLink(linkUrl);
        // 查看是否已经存在这个url
        boolean success = iWarningLinkService.saveUrl(warningLink);
        if (!success) {
            return Result.failed("添加失败");
        }
        List<WarningLink> list = iWarningLinkService.list();
        return Result.successWithData(list);
    }

    @GetMapping("/link")
    public Result getLink() {
        List<WarningLink> list = iWarningLinkService.list();
        return Result.successWithData(list);
    }

    @DeleteMapping("/link")
    public Result deleteLink(@RequestBody WarningLink warningLink) {
        log.info(warningLink.toString());
        boolean success = iWarningLinkService.removeById(warningLink.getId());
        if (!success) {
            return Result.failed("删除失败");
        }
        List<WarningLink> list = iWarningLinkService.list();
        return Result.successWithData(list);
    }


}

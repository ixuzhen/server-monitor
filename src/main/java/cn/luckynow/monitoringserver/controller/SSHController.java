package cn.luckynow.monitoringserver.controller;


import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.luckynow.monitoringserver.common.Constants;
import cn.luckynow.monitoringserver.entity.Result;
import cn.luckynow.monitoringserver.entity.SshHost;
import cn.luckynow.monitoringserver.entity.User;
import cn.luckynow.monitoringserver.service.ISshHostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/ssh")
@Slf4j
@CrossOrigin
public class SSHController {

    @Resource
    private ISshHostService iSshHostService;


    /**
     * 增加一条数据
     *
     * @param data
     * @param request
     * @return
     */
    @PostMapping("/shhHost")
    public Result addSshHost(@RequestBody String data, HttpServletRequest request) {
        User user = (User) request.getAttribute(Constants.REQUEST_USER);
        Long userId = user.getId();

        JSONObject dataJson = JSONUtil.parseObj(data);
        String address = (String) dataJson.get("address");
        String port = (String) dataJson.get("port");
        String username = (String) dataJson.get("username");
        String password = (String) dataJson.get("password");
        SshHost sshHost = new SshHost();
        sshHost.setAddress(address);
        sshHost.setPort(port);
        sshHost.setUsername(username);
        sshHost.setPassword(password);
        sshHost.setUserId(userId);
        // 现在
        sshHost.setUpdateTime(DateTime.now().toTimestamp());
        boolean success = iSshHostService.save(sshHost);
        if (!success) {
            Result.failed("操作失败");
        }

        List<SshHost> res = iSshHostService.getListByUserId(userId);
        return Result.successWithData(res);
    }


    @GetMapping("/shhHost")
    public Result getSshHost(HttpServletRequest request) {
        User user = (User) request.getAttribute(Constants.REQUEST_USER);
        Long userId = user.getId();
        List<SshHost> res = iSshHostService.getListByUserId(userId);
        return Result.successWithData(res);
    }

    @PutMapping("/shhHost")
    public Result UpdateSshHost(@RequestBody SshHost sshHost, HttpServletRequest request) {
        User user = (User) request.getAttribute(Constants.REQUEST_USER);
        Long userId = user.getId();
        sshHost.setUserId(userId);
        boolean success = iSshHostService.updateById(sshHost);
        if (!success) {
            Result.failed("操作失败");
        }
        List<SshHost> res = iSshHostService.getListByUserId(userId);
        return Result.successWithData(res);
    }

    @DeleteMapping("/shhHost/{sshHostId}")
    public Result deleteSshHost(@PathVariable int sshHostId, HttpServletRequest request) {
        User user = (User) request.getAttribute(Constants.REQUEST_USER);
        Long userId = user.getId();
//        sshHost.setUserId(userId);
        boolean success = iSshHostService.removeById(sshHostId);
        if (!success) {
            Result.failed("操作失败");
        }
        List<SshHost> res = iSshHostService.getListByUserId(userId);
        return Result.successWithData(res);
    }

}

package com.wangbowen.modules.sys.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wangbowen.common.base.SuperController;
import com.wangbowen.common.result.MsgResult;
import com.wangbowen.common.result.PageResult;
import com.wangbowen.common.utils.StringUtils;
import com.wangbowen.modules.sys.entity.SysLog;
import com.wangbowen.modules.sys.entity.SysMenu;
import com.wangbowen.modules.sys.entity.SysUser;
import com.wangbowen.modules.sys.service.ISysLogService;
import com.wangbowen.modules.sys.service.ISysUserService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 系统日志表 前端控制器
 * </p>
 *
 * @author luckyxz999
 * @since 2017-10-16
 */
@Controller
@RequestMapping("${adminPath}/sys/log")
public class SysLogController extends SuperController {
	private final ISysLogService sysLogService;
    private final ISysUserService sysUserService;

    @Autowired
    public SysLogController(ISysLogService sysLogService, ISysUserService sysUserService) {
        this.sysLogService = sysLogService;
        this.sysUserService = sysUserService;
    }

    @RequiresPermissions("sys:log:view")
    @RequestMapping("")
    String user(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("params", params);
        model.addAttribute("logList", null);
        return "admin/sys/log/logList";
    }

    @RequiresPermissions("sys:log:view")
    @RequestMapping("/list")
    @ResponseBody
    PageResult list(@RequestParam Map<String, String> params, Integer pageNo, Integer pageSize) {
        EntityWrapper<SysLog> wrapper = new EntityWrapper<>();
        if (StringUtils.isNotBlank(params.get("title"))) {
            wrapper.like("title", params.get("title"));
        }
        wrapper.orderBy("create_date desc");
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageNo == null) {
            pageNo = 1;
        } else {
            pageNo = pageNo / pageSize + 1;//页面上传过来的是offset，需要转成页码
        }
        //分页查询数据
        Page<SysLog> logPage = new Page<>(pageNo, pageSize);
        wrapper.setSqlSelect("id", "title", "remote_addr", "user_agent", "request_uri", "method", "create_by", "create_by_name", "create_date");
        logPage = sysLogService.selectPage(logPage, wrapper);
        return new PageResult(logPage.getRecords(), logPage.getTotal());
    }

    @RequiresPermissions("sys:log:view")
    @GetMapping("/view")
    String view(Model model, Long id) {
        if (id != null) {
            SysLog sysLog = sysLogService.selectById(id);
            model.addAttribute("log", sysLog);

            if (sysLog != null && sysLog.getCreateBy() != null) {
                SysUser sysUser = sysUserService.selectById(sysLog.getCreateBy());
                if (sysUser != null) {
                    sysLog.setCreateByName(sysUser.getName());
                }
            }
        }

        return "admin/sys/log/logView";
    }

    @RequiresPermissions("sys:log:delete")
    @PostMapping("/delete")
    @ResponseBody
    MsgResult delete(Long id) {
        if (sysLogService.deleteById(id)) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }
}

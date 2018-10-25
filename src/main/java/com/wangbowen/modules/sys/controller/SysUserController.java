package com.wangbowen.modules.sys.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wangbowen.common.base.SuperController;
import com.wangbowen.common.exception.BusinessException;
import com.wangbowen.common.result.MsgResult;
import com.wangbowen.common.result.PageResult;
import com.wangbowen.common.shiro.ShiroUtils;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.common.utils.PasswordUtils;
import com.wangbowen.common.utils.StringUtils;
import com.wangbowen.modules.sys.entity.SysRole;
import com.wangbowen.modules.sys.entity.SysUser;
import com.wangbowen.modules.sys.service.ISysRoleService;
import com.wangbowen.modules.sys.service.ISysUserService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.*;

/**
 * <p>
 * 系统用户表 前端控制器
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@Controller
@RequestMapping("${adminPath}/sys/user")
public class SysUserController extends SuperController {
	private final ISysUserService sysUserService;
    private final ISysRoleService sysRoleService;

    @Autowired
    public SysUserController(ISysUserService sysUserService, ISysRoleService sysRoleService) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping("")
    String user(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("params", params);
        model.addAttribute("userList", null);
        return "admin/sys/user/userList";
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping("/list")
    @ResponseBody
    PageResult list(@RequestParam Map<String, String> params, Integer pageNo, Integer pageSize) {
        EntityWrapper<SysUser> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        if (StringUtils.isNotBlank(params.get("loginName"))) {
            wrapper.like("login_name", params.get("loginName"));
        }
        if (StringUtils.isNotBlank(params.get("name"))) {
            wrapper.like("name", params.get("name"));
        }
        wrapper.orderBy("create_date", false);
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageNo == null) {
            pageNo = 1;
        } else {
            pageNo = pageNo / pageSize + 1;//页面上传过来的是offset，需要转成页码
        }
        //分页查询数据
        Page<SysUser> userPage = new Page<>(pageNo, pageSize);
        userPage = sysUserService.selectPage(userPage, wrapper);
        return new PageResult(userPage.getRecords(), userPage.getTotal());
    }

    @RequiresPermissions("sys:user:view")
    @GetMapping("/edit")
    String edit(Model model, Long id) {
        EntityWrapper<SysRole> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.eq("status", ConstUtils.YES);
        wrapper.orderBy("create_date", false);
        List<SysRole> allRoles = sysRoleService.selectList(wrapper);
        model.addAttribute("allRoles", allRoles);

        if (id != null) {
            SysUser sysUser = sysUserService.selectById(id);
            model.addAttribute("user", sysUser);

            List<SysRole> hasRoles = sysRoleService.selectUserRoles(id);
            model.addAttribute("hasRoles", hasRoles);
            if (allRoles != null && allRoles.size() > 0 && hasRoles != null && hasRoles.size() > 0) {
                for (SysRole role : allRoles) {
                    for (SysRole hasRole : hasRoles) {
                        if (hasRole.getId().equals(role.getId())) {
                            role.setExtend("checked");
                            break;
                        }
                    }
                }
            }
        }


        return "admin/sys/user/userEdit";
    }

    @RequiresPermissions("user:info:edit")
    @GetMapping("/info")
    String info(Model model) {
        SysUser user = sysUserService.selectById(ShiroUtils.getUserId());
        model.addAttribute("user", user);

        return "admin/sys/user/infoEdit";
    }

    @RequiresPermissions("sys:user:edit")
    @GetMapping("/add")
    String add(Model model) {
        EntityWrapper<SysRole> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.eq("status", ConstUtils.YES);
        wrapper.orderBy("create_date", false);
        List<SysRole> allRoles = sysRoleService.selectList(wrapper);
        model.addAttribute("allRoles", allRoles);
        return "admin/sys/user/userAdd";
    }

    @RequiresPermissions("sys:user:edit")
    @PostMapping("/save")
    @ResponseBody
    MsgResult save(SysUser user, String roleIds) {
        try {
            if (user.getId() == null) {
                user.setCreateBy(ShiroUtils.getUserId());
                user.setCreateDate(new Date());
            }
            user.setUpdateBy(ShiroUtils.getUserId());
            user.setUpdateDate(new Date());
            if (sysUserService.saveOrUpdateUser(user, roleIds)) {
                return MsgResult.ok();
            }
        } catch (BusinessException e) {
            logger.error("保存用户信息失败：{}", e.getMessage());
            return MsgResult.error(e.getMessage());
        } catch (Exception e) {
            logger.error("保存用户信息失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }

        return MsgResult.error();
    }

    @RequiresPermissions("sys:user:delete")
    @PostMapping("/delete")
    @ResponseBody
    MsgResult delete(Long id) {
        if (1 == id) {
            return MsgResult.error("不允许删除管理员");
        }
        SysUser user = new SysUser();
        user.setId(id);
        user.setDelFlag(ConstUtils.DEL_FLAG_DELETE);
        if (sysUserService.updateById(user)) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    @RequiresPermissions("sys:user:edit")
    @PostMapping("/resetPwd")
    @ResponseBody
    MsgResult resetPwd(Long id) {
        if (1 == id) {
            return MsgResult.error("不允许重置管理员密码");
        }
        SysUser user = new SysUser();
        user.setId(id);
        user.setPassword(PasswordUtils.entryptPassword("123456"));
        if (sysUserService.updateById(user)) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 验证登录名是否有效
     * @param oldLoginName 老的登录名
     * @param loginName 新的登录名
     */
    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "checkLoginNameAjax")
    public String checkLoginName(String oldLoginName, String loginName) {
        if (loginName != null && loginName.equals(oldLoginName)) {
            return "true";
        } else if (loginName !=null && sysUserService.getByLoginName(loginName) == null) {
            return "true";
        }
        return "false";
    }

    @RequiresPermissions("user:pwd:edit")
    @GetMapping("/pwd")
    String pwd(Model model) {
        return "admin/sys/user/pwdUpdate";
    }

    @RequiresPermissions("user:pwd:edit")
    @PostMapping("/updatePwd")
    @ResponseBody
    MsgResult updatePwd(String oldPassword, String newPassword, String reNewPassword) {
        if (StringUtils.isBlank(oldPassword)) {
            return MsgResult.error("旧密码不能为空");
        }
        if (StringUtils.isBlank(newPassword)) {
            return MsgResult.error("新密码不能为空");
        }
        if (!newPassword.equals(reNewPassword)) {
            return MsgResult.error("确认密码与新密码不一致");
        }
        SysUser user = sysUserService.selectById(ShiroUtils.getUserId());
        if (user == null) {
            return MsgResult.error("登录已超时");
        }
        // 密码错误
        if (!PasswordUtils.validatePassword(oldPassword, user.getPassword())) {
            return MsgResult.error("旧密码不正确");
        }

        //更新密码
        SysUser updateUser = new SysUser();
        updateUser.setId(user.getId());
        updateUser.setPassword(PasswordUtils.entryptPassword(newPassword));
        if (sysUserService.updateById(updateUser)) {
            return MsgResult.ok();
        }

        return MsgResult.error();
    }
}

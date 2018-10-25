package com.wangbowen.modules.sys.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.wangbowen.common.base.SuperController;
import com.wangbowen.common.result.MsgResult;
import com.wangbowen.common.result.PageResult;
import com.wangbowen.common.shiro.ShiroUtils;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.modules.sys.entity.SysMenu;
import com.wangbowen.modules.sys.entity.SysRole;
import com.wangbowen.modules.sys.service.ISysMenuService;
import com.wangbowen.modules.sys.service.ISysRoleService;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 系统角色表 前端控制器
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@Controller
@RequestMapping("/admin/sys/role")
public class SysRoleController extends SuperController {
    private final ISysRoleService sysRoleService;
    private final ISysMenuService sysMenuService;

    @Autowired
    public SysRoleController(ISysRoleService sysRoleService, ISysMenuService sysMenuService) {
        this.sysRoleService = sysRoleService;
        this.sysMenuService = sysMenuService;
    }

    @RequiresPermissions("sys:role:view")
    @RequestMapping("")
    String user(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("params", params);
        model.addAttribute("roleList", null);
        return "admin/sys/role/roleList";
    }

    @RequiresPermissions("sys:role:view")
    @RequestMapping("/list")
    @ResponseBody
    PageResult list(Integer pageNo, Integer pageSize) {
        EntityWrapper<SysRole> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
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
        Page<SysRole> rolePage = new Page<>(pageNo, pageSize);
        rolePage = sysRoleService.selectPage(rolePage, wrapper);
        return new PageResult(rolePage.getRecords(), rolePage.getTotal());
    }

    @RequiresPermissions("sys:role:view")
    @GetMapping("/edit")
    String edit(Model model, Long id) {
        if (id != null) {
            SysRole sysRole = sysRoleService.selectById(id);
            model.addAttribute("role", sysRole);

            List<SysMenu> hasMenus = sysMenuService.listRoleMenu(id);
            if (hasMenus != null && hasMenus.size() > 0) {
                String selectedIds = "";
                for (SysMenu menu : hasMenus) {
                    selectedIds += menu.getId() + ",";
                }
                model.addAttribute("selectedIds", selectedIds);
            }
        }

        model.addAttribute("menuList", getMenuMapList());

        return "admin/sys/role/roleEdit";
    }

    @RequiresPermissions("sys:role:edit")
    @GetMapping("/add")
    String add(Model model) {
        model.addAttribute("menuList", getMenuMapList());
        return "admin/sys/role/roleAdd";
    }

    /**
     * 获取所有菜单列表，转换成ztree树结构
     */
    private List<Map<String, Object>> getMenuMapList() {
        EntityWrapper<SysMenu> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.eq("status", ConstUtils.YES);
        wrapper.orderBy("order_no, create_date desc");
        List<SysMenu> allMenus = sysMenuService.selectList(wrapper);

        if (allMenus != null && allMenus.size() > 0) {
            List<Map<String, Object>> mapList = Lists.newArrayList();
            for (SysMenu menu : allMenus) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", menu.getId());
                map.put("pId", menu.getParentId());
                map.put("name", menu.getName());
                mapList.add(map);
            }

            return mapList;
        }

        return null;
    }

    @RequiresPermissions("sys:role:edit")
    @PostMapping("/save")
    @ResponseBody
    MsgResult save(SysRole role, String menuIds) {
        try {
            if (role.getId() == null) {
                role.setCreateBy(ShiroUtils.getUserId());
                role.setCreateDate(new Date());
            }
            role.setUpdateBy(ShiroUtils.getUserId());
            role.setUpdateDate(new Date());
            if (sysRoleService.saveOrUpdateRole(role, menuIds)) {
                return MsgResult.ok();
            }
        } catch (Exception e) {
            logger.error("保存角色信息失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }

        return MsgResult.error();
    }

    @RequiresPermissions("sys:role:delete")
    @PostMapping("/delete")
    @ResponseBody
    MsgResult delete(Long id) {
        SysRole role = new SysRole();
        role.setId(id);
        role.setDelFlag(ConstUtils.DEL_FLAG_DELETE);
        if (sysRoleService.updateById(role)) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 验证角色名是否有效
     * @param oldName 老的角色名
     * @param name 新的角色名
     */
    @ResponseBody
    @RequestMapping(value = "checkLoginNameAjax")
    public String checkRoleName(String oldName, String name) {
        if (name != null && name.equals(oldName)) {
            return "true";
        } else if (name != null) {
            EntityWrapper<SysRole> wrapper = new EntityWrapper<>();
            wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
            wrapper.eq("name", name);
            if (sysRoleService.selectCount(wrapper) == 0) {
                return "true";
            }
        }
        return "false";
    }
}

package com.wangbowen.modules.sys.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wangbowen.common.base.SuperController;
import com.wangbowen.common.result.MsgResult;
import com.wangbowen.common.shiro.ShiroUtils;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.modules.sys.entity.SysMenu;
import com.wangbowen.modules.sys.service.ISysMenuService;

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
 * 系统菜单表 前端控制器
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@Controller
@RequestMapping("${adminPath}/sys/menu")
public class SysMenuController extends SuperController {
    private final ISysMenuService sysMenuService;

    @Autowired
    public SysMenuController(ISysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    @RequiresPermissions("sys:menu:view")
    @RequestMapping("")
    String user(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("params", params);
        model.addAttribute("menuList", null);
        return "admin/sys/menu/menuList";
    }

    @RequiresPermissions("sys:menu:view")
    @RequestMapping("/list")
    @ResponseBody
    List<SysMenu> list() {
        EntityWrapper<SysMenu> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.orderBy("order_no, create_date desc");
        //查询数据
        return sysMenuService.selectList(wrapper);
    }

    @RequiresPermissions("sys:menu:view")
    @GetMapping("/edit")
    String edit(Model model, Long id) {
        if (id != null) {
            SysMenu sysMenu = sysMenuService.selectById(id);
            model.addAttribute("menu", sysMenu);
            model.addAttribute("parentMenu", new SysMenu());

            if (sysMenu != null) {
                Long parentId = sysMenu.getParentId();
                if (parentId != null) {
                    SysMenu parentMenu = sysMenuService.selectById(parentId);
                    if (parentMenu != null) {
                        model.addAttribute("parentMenu", parentMenu);
                    }
                }
            }
        }

        return "admin/sys/menu/menuEdit";
    }

    @RequiresPermissions("sys:menu:edit")
    @GetMapping("/add")
    String add(Model model, Long parentId) {
        if (parentId != null) {
            SysMenu sysMenu = sysMenuService.selectById(parentId);
            model.addAttribute("parentMenu", sysMenu);
        } else {
            model.addAttribute("parentMenu", new SysMenu());
        }

        // 获取排序号，最末节点排序号+30
        Integer maxOrderNo = sysMenuService.getMaxOrderNo(parentId);
        if (maxOrderNo != null) {
            model.addAttribute("maxOrderNo", maxOrderNo + 30);
        } else {
            model.addAttribute("maxOrderNo", 30);
        }

        return "admin/sys/menu/menuAdd";
    }

    @RequiresPermissions("sys:menu:edit")
    @PostMapping("/save")
    @ResponseBody
    MsgResult save(SysMenu menu) {
        try {
            if (menu.getId() == null) {
                menu.setCreateBy(ShiroUtils.getUserId());
                menu.setCreateDate(new Date());
                sysMenuService.insert(menu);
            } else {
                SysMenu oldMenu = sysMenuService.selectById(menu.getId());
                oldMenu.setName(menu.getName());
                oldMenu.setParentId(menu.getParentId());
                oldMenu.setIcon(menu.getIcon());
                oldMenu.setType(menu.getType());
                oldMenu.setHref(menu.getHref());
                oldMenu.setPermission(menu.getPermission());
                oldMenu.setStatus(menu.getStatus());
                oldMenu.setOrderNo(menu.getOrderNo());
                oldMenu.setUpdateBy(ShiroUtils.getUserId());
                oldMenu.setUpdateDate(new Date());

                sysMenuService.updateAllColumnById(oldMenu);
            }
            return MsgResult.ok();
        } catch (Exception e) {
            logger.error("保存菜单失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }

    }

    @RequiresPermissions("sys:menu:edit")
    @PostMapping("/saveSort")
    @ResponseBody
    MsgResult saveSort(Long[] ids, Integer[] orderNos) {
        try {
            if (ids != null && ids.length > 0) {
                for (int i = 0; i < ids.length; i++) {
                    SysMenu menu = new SysMenu();
                    menu.setId(ids[i]);
                    menu.setOrderNo(orderNos[i]);
                    sysMenuService.updateById(menu);
                }
            }
            return MsgResult.ok();
        } catch (Exception e) {
            logger.error("保存菜单失败：系统异常，{}", e.getMessage());
            return MsgResult.error("系统异常");
        }
    }

    @RequiresPermissions("sys:menu:delete")
    @PostMapping("/delete")
    @ResponseBody
    MsgResult delete(Long id) {
        if (sysMenuService.deleteMenu(id) > 0) {
            return MsgResult.ok();
        }
        return MsgResult.error();
    }

    /**
     * 菜单树形数据结构
     */
    @ResponseBody
    @RequestMapping(value = "treeData")
    public List<Map<String, Object>> treeData() {
        List<Map<String, Object>> mapList = Lists.newArrayList();
        EntityWrapper<SysMenu> wrapper = new EntityWrapper<>();
        wrapper.eq("del_flag", ConstUtils.DEL_FLAG_NOT_DELETE);
        wrapper.eq("type", ConstUtils.MENU_TYPE_MENU);
        wrapper.orderBy("sort, create_date desc");
        List<SysMenu> list = sysMenuService.selectList(wrapper);
        if (list != null && list.size() > 0) {
            for (SysMenu e : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", e.getId());
                map.put("pId", e.getParentId());
                map.put("name", e.getName());
                mapList.add(map);
            }
        }

        return mapList;
    }
}

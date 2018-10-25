package com.wangbowen.modules.sys.service.impl;

import com.wangbowen.common.utils.CacheUtils;
import com.wangbowen.common.utils.ConstUtils;
import com.wangbowen.modules.sys.entity.SysMenu;
import com.wangbowen.modules.sys.mapper.SysMenuMapper;
import com.wangbowen.modules.sys.service.ISysMenuService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 系统菜单表 服务实现类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {
    @Override
    public Set<String> listUserPermissions(Long userId) {
        if (userId == null) {
            return null;
        }

        Set<String> permissionList = (Set<String>) CacheUtils.getSysCache("perms_" + userId);
        if (permissionList == null){
            List<String> perms;
            if (1L == userId) {
                perms = baseMapper.listAllPermissions();
            } else {
                perms = baseMapper.listUserPermissions(userId);
            }

            if (perms != null && perms.size() > 0) {
                permissionList = new HashSet<>();
                for (String perm : perms) {
                    if (StringUtils.isNotBlank(perm)) {
                        permissionList.addAll(Arrays.asList(perm.trim().split(",")));
                    }
                }
                CacheUtils.putSysCache("perms_" + userId, permissionList);
            }
        }

        return permissionList;
    }

    @Override
    public List<SysMenu> listUserMenu(Long userId) {
        if (userId == null) {
            return null;
        }
        List<SysMenu> menuList = (List<SysMenu>) CacheUtils.getSysCache("menus_" + userId);
        if (menuList == null){
            if (1L == userId) {
                menuList = baseMapper.listAllMenu();
            } else {
                menuList = baseMapper.getUserMenuList(userId);
            }

            if (menuList != null && menuList.size() > 0) {
                CacheUtils.putSysCache("menus_" + userId, menuList);
            }
        }

        return menuList;
    }

    @Override
    public List<SysMenu> listRoleMenu(Long roleId) {
        return baseMapper.listRoleMenu(roleId);
    }

    @Override
    public int deleteMenu(Long parentId) {
        String childIds = selectAllChildIds(parentId);
        int count = 0;
        if (childIds != null) {
            String[] idArr = childIds.split(",");
            //循环删除所有本身及子节点
            for (String id : idArr) {
                if (StringUtils.isNotBlank(id) && StringUtils.isNumeric(id)) {
                    SysMenu menu = new SysMenu();
                    menu.setId(Long.parseLong(id));
                    menu.setDelFlag(ConstUtils.DEL_FLAG_DELETE);
                    count += baseMapper.updateById(menu);
                }
            }
        }
        return count;
    }

    @Override
    public String selectAllChildIds(Long parentId) {
        return baseMapper.selectAllChildIds(parentId);
    }

    @Override
    public Integer getMaxOrderNo(Long parentId) {
        return baseMapper.getMaxOrderNo(parentId);
    }
}

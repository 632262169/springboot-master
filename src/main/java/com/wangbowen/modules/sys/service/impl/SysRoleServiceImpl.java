package com.wangbowen.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wangbowen.common.utils.StringUtils;
import com.wangbowen.modules.sys.entity.SysRole;
import com.wangbowen.modules.sys.entity.SysRoleMenu;
import com.wangbowen.modules.sys.entity.SysUserRole;
import com.wangbowen.modules.sys.mapper.SysRoleMapper;
import com.wangbowen.modules.sys.mapper.SysRoleMenuMapper;
import com.wangbowen.modules.sys.service.ISysRoleService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 系统角色表 服务实现类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    private final SysRoleMenuMapper sysRoleMenuMapper;

    @Autowired
    public SysRoleServiceImpl(SysRoleMenuMapper sysRoleMenuMapper) {
        this.sysRoleMenuMapper = sysRoleMenuMapper;
    }

    @Override
    public List<SysRole> selectUserRoles(Long userId) {
        return baseMapper.selectUserRoles(userId);
    }

    @Override
    @Transactional
    public boolean saveOrUpdateRole(SysRole role, String menuIds) throws Exception {
        if (role == null) {
            return false;
        }
        if (role.getId() == null) {
            //新增角色
            baseMapper.insert(role);
            Long roleId = role.getId();
            if (roleId != null && StringUtils.isNotBlank(menuIds)) {
                String[] menuIdArr = menuIds.split(",");
                for (String menuId : menuIdArr) {
                    if (StringUtils.isNumeric(menuId)) {
                        SysRoleMenu sysRoleMenu = new SysRoleMenu();
                        sysRoleMenu.setRoleId(roleId);
                        sysRoleMenu.setMenuId(Long.parseLong(menuId));
                        sysRoleMenuMapper.insert(sysRoleMenu);
                    }
                }
            }
        } else {
            baseMapper.updateById(role);
            Long roleId = role.getId();
            //删除已关联的菜单
            EntityWrapper<SysRoleMenu> wrapper = new EntityWrapper<>();
            wrapper.eq("role_id", roleId);
            sysRoleMenuMapper.delete(wrapper);
            //关联新的菜单

            if (StringUtils.isNotBlank(menuIds)) {
                String[] menuIdArr = menuIds.split(",");
                for (String menuId : menuIdArr) {
                    if (StringUtils.isNumeric(menuId)) {
                        SysRoleMenu sysRoleMenu = new SysRoleMenu();
                        sysRoleMenu.setRoleId(roleId);
                        sysRoleMenu.setMenuId(Long.parseLong(menuId));
                        sysRoleMenuMapper.insert(sysRoleMenu);
                    }
                }
            }
        }

        return true;
    }
}

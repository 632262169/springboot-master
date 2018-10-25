package com.wangbowen.modules.sys.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wangbowen.common.exception.BusinessException;
import com.wangbowen.common.utils.CacheUtils;
import com.wangbowen.common.utils.PasswordUtils;
import com.wangbowen.common.utils.StringUtils;
import com.wangbowen.modules.sys.entity.SysUser;
import com.wangbowen.modules.sys.entity.SysUserRole;
import com.wangbowen.modules.sys.mapper.SysUserMapper;
import com.wangbowen.modules.sys.mapper.SysUserRoleMapper;
import com.wangbowen.modules.sys.service.ISysUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {
    private final SysUserRoleMapper sysUserRoleMapper;

    @Autowired
    public SysUserServiceImpl(SysUserRoleMapper sysUserRoleMapper) {
        this.sysUserRoleMapper = sysUserRoleMapper;
    }

    @Override
    public SysUser getByLoginName(String loginName) {
        if (StringUtils.isNotBlank(loginName)) {
            return baseMapper.getByLoginName(loginName);
        }
        return null;
    }

    @Override
    @Transactional
    public boolean saveOrUpdateUser(SysUser user, String roleIds) throws BusinessException {
        if (user == null) {
            return false;
        }
        //新增
        if (user.getId() == null) {
            if (getByLoginName(user.getLoginName()) != null) {
                throw new BusinessException("登录名不能重复");
            }
            user.setPassword(PasswordUtils.entryptPassword(user.getPassword()));
            baseMapper.insert(user);

            //保存关联的角色
            if (user.getId() != null && StringUtils.isNotBlank(roleIds)) {
                String[] roleIdArr = roleIds.split(",");
                for (String roleId : roleIdArr) {
                    if (StringUtils.isNumeric(roleId)) {
                        SysUserRole sysUserRole = new SysUserRole();
                        sysUserRole.setUserId(user.getId());
                        sysUserRole.setRoleId(Long.parseLong(roleId));

                        sysUserRoleMapper.insert(sysUserRole);
                    }
                }
            }
        } else {//修改，注意：此处不修改为null的字段，防止修改密码
            baseMapper.updateById(user);

            //先删除已关联的角色
            EntityWrapper<SysUserRole> wrapper = new EntityWrapper<>();
            wrapper.eq("user_id", user.getId());
            sysUserRoleMapper.delete(wrapper);

            //保存新关联的角色
            if (StringUtils.isNotBlank(roleIds)) {
                String[] roleIdArr = roleIds.split(",");
                for (String roleId : roleIdArr) {
                    if (StringUtils.isNumeric(roleId)) {
                        SysUserRole sysUserRole = new SysUserRole();
                        sysUserRole.setUserId(user.getId());
                        sysUserRole.setRoleId(Long.parseLong(roleId));

                        sysUserRoleMapper.insert(sysUserRole);
                    }
                }
            }
        }

        CacheUtils.removeSysCache("perms_" + user.getId());
        return true;
    }
}

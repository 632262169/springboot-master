package com.wangbowen.modules.sys.service;

import com.wangbowen.common.exception.BusinessException;
import com.wangbowen.modules.sys.entity.SysUser;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 系统用户表 服务类
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
public interface ISysUserService extends IService<SysUser> {
    /**
     * 根据登录名查询用户信息
     * @param loginName 登录名
     * @return 用户信息对象
     */
    SysUser getByLoginName(String loginName);

    /**
     * 保存用户信息
     * @param user 用户信息
     * @param roleIds 分配的角色列表
     */
    boolean saveOrUpdateUser(SysUser user, String roleIds) throws BusinessException;


}

package com.wangbowen.modules.sys.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wangbowen.modules.sys.entity.SysUser;

import org.springframework.stereotype.Repository;

/**
 * <p>
  * 系统用户表 Mapper 接口
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
@Repository
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 根据登录名查询用户信息
     * @param loginName 登录名
     * @return 用户信息
     */
    SysUser getByLoginName(String loginName);
}
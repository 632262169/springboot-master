package com.wangbowen.modules.sys.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.wangbowen.modules.sys.entity.SysRole;

import java.util.List;

/**
 * <p>
  * 系统角色表 Mapper 接口
 * </p>
 *
 * @author luckyxz999
 * @since 2017-09-19
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 查询用户角色列表
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectUserRoles(Long userId);
}
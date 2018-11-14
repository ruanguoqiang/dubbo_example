package com.genaratexml;


import com.common.model.RpcPermission;
import com.common.pojo.Permission;
import com.common.pojo.PermissionExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;



public interface PermissionMapper {
    int countByExample(PermissionExample example);

    int deleteByExample(PermissionExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Permission record);

    int insertSelective(Permission record);

    List<Permission> selectByExample(PermissionExample example);

    Permission selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Permission record, @Param("example") PermissionExample example);

    int updateByExample(@Param("record") Permission record, @Param("example") PermissionExample example);

    int updateByPrimaryKeySelective(Permission record);

    int updateByPrimaryKey(Permission record);

    List<Permission> findPermissionByRole(@Param("appid") Integer appid,@Param("roleid") Integer roleid);

    List<RpcPermission> findListById(@Param("appCode")String appcode, @Param(("userId")) Integer userid );
}
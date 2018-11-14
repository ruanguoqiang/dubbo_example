package com.qiangge.interf;





import com.common.model.Result;
import com.common.pojo.User;

import java.util.List;

/**
 * 用户服务接口
 * 
 * @author Joe
 */
public interface UserService  {
	
	/**
	 * 登录
	 * 
	 * @param account
	 *            登录名
	 * @param password
	 *            密码
	 * @return 用户ID和应用编码集合Map
	 *             认证异常
	 */
	public Result login(String ip, String account, String password);
	
	/**
	 * 启用禁用操作
	 * @param isEnable 是否启用
	 * @param idList 用户ID集合
	 * @return
	 */
	public void enable(Boolean isEnable, List<Integer> idList);
	
	/**
	 * 重置密码
	 * @param password 初始化密码(已加密)
	 * @param idList 
	 */
	public void resetPassword(String password, List<Integer> idList);

	
	/**
	 * 根据登录名和应用ID查询
	 * @param account 登录名
	 * @return
	 */
	public User findByAccount(String account);
	
	/**
	 * 更新密码
	 * 
	 * @param id
	 *            用户ID
	 * @param newPassword
	 *            新密码
	 * @return
	 */
	public void updatePassword(Integer id, String newPassword);
	
	
	public void save(User user, List<Integer> roleIdList);
}

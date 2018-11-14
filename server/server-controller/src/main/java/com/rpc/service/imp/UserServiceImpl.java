package com.rpc.service.imp;


import com.common.model.Result;
import com.common.pojo.User;
import com.qiangge.interf.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl  implements UserService {


	@Override
	public Result login(String ip, String account, String password) {
		return null;
	}

	@Override
	public void enable(Boolean isEnable, List<Integer> idList) {

	}

	@Override
	public void resetPassword(String password, List<Integer> idList) {

	}

	@Override
	public User findByAccount(String account) {
		return null;
	}

	@Override
	public void updatePassword(Integer id, String newPassword) {

	}

	@Override
	public void save(User user, List<Integer> roleIdList) {

	}
}

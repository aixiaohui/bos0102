package cn.itcast.bos.dao;

import cn.itcast.bos.dao.base.IBaseDao;
import cn.itcast.bos.domain.User;

public interface IUserDao extends IBaseDao<User>{

	public User findUserByUsernameAndPassword(String username, String password);

	public User findUserByUsername(String username);

}

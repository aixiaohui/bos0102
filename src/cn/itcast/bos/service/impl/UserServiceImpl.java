package cn.itcast.bos.service.impl;

import java.net.IDN;

import org.activiti.engine.IdentityService;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.IRoleDao;
import cn.itcast.bos.dao.IUserDao;
import cn.itcast.bos.domain.Role;
import cn.itcast.bos.domain.User;
import cn.itcast.bos.service.IUserService;
import cn.itcast.bos.utils.MD5Utils;
import cn.itcast.bos.utils.PageBean;
@Service
@Transactional
public class UserServiceImpl implements IUserService{
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IRoleDao roleDao;
	
	public User login(User user) {
		String password = MD5Utils.md5(user.getPassword());
		return userDao.findUserByUsernameAndPassword(user.getUsername(),password);
	}
	
	//根据用户id修改密码 
	public void editPassword(String password, String id) {
		password = MD5Utils.md5(password);
		userDao.executeUpdate("editPasswordById", password,id);
	}

	@Autowired
	private IdentityService identityService;
	
	/**
	 * 添加用户，关联角色
	 * 同步数据到Activiti的act_id_user、act_id_membership
	 */
	public void save(User user, String[] roleIds) {
		String password = user.getPassword();
		password = MD5Utils.md5(password);
		user.setPassword(password);
		userDao.save(user);//持久对象---t_user
		
		//使用t_user表的id作为act_id_user表的id
		org.activiti.engine.identity.User actuser = new UserEntity(user.getId());
		identityService.saveUser(actuser);
		
		for (String roleId : roleIds) {
			Role role = roleDao.findById(roleId);//持久对象
			user.getRoles().add(role);//用户关联角色
			
			//同步到act_id_membership
			identityService.createMembership(actuser.getId(), role.getName());
		}
	}

	public void pageQuery(PageBean pageBean) {
		userDao.pageQuery(pageBean);
	}
}

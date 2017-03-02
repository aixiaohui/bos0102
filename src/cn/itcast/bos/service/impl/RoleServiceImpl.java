package cn.itcast.bos.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.IRoleDao;
import cn.itcast.bos.domain.Function;
import cn.itcast.bos.domain.Role;
import cn.itcast.bos.service.IRoleService;
import cn.itcast.bos.utils.PageBean;
@Service
@Transactional
public class RoleServiceImpl implements IRoleService {
	@Resource
	private IRoleDao roleDao;
	@Resource
	private IdentityService identityService;
	
	/**
	 * 添加一个角色，同时关联权限
	 * 同时通报角色数据到Activiti的act_id_group表中
	 */
	public void save(Role role, String functionIds) {
		roleDao.save(role);//持久对象
		Group group = new GroupEntity();
		//使用角色的名称作为组的id
		group.setId(role.getName());
		//同步到act_id_group
		identityService.saveGroup(group);
		
		String[] ids = functionIds.split(",");
		for (String functionId : ids) {
			Function function = new Function();
			function.setId(functionId);//托管对象
			role.getFunctions().add(function);//角色关联权限---持久对象关联托管对象
		}
	}

	public void pageQuery(PageBean pageBean) {
		roleDao.pageQuery(pageBean);
	}
	
	public List<Role> findAll() {
		return roleDao.findAll();
	}
}

package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.Role;
import cn.itcast.bos.service.IRoleService;
import cn.itcast.bos.web.action.base.BaseAction;

/**
 * 角色管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class RoleAction extends BaseAction<Role>{
	//权限id ----1,2,3,4
	private String functionIds;
	
	@Autowired
	private IRoleService roleService;
	
	/**
	 * 添加角色
	 */
	public String add(){
		roleService.save(model,functionIds);
		return LIST;
	}
	
	/**
	 * 分页查询
	 * @throws IOException 
	 */
	public String pageQuery() throws IOException{
		roleService.pageQuery(pageBean);
		this.writeObject2Json(pageBean, new String[]{"currentPage","pageSize","detachedCriteria","functions","users"});
		return NONE;
	}
	
	public String listajax(){
		List<Role> list = roleService.findAll();
		this.writeList2Json(list, new String[]{"functions","users"});
		return NONE;
	}
	
	public void setFunctionIds(String functionIds) {
		this.functionIds = functionIds;
	}
}

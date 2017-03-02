package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.Staff;
import cn.itcast.bos.service.IStaffService;
import cn.itcast.bos.utils.PageBean;
import cn.itcast.bos.web.action.base.BaseAction;

/**
 * 取派员管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class StaffAction extends BaseAction<Staff> {
	//注入Service
	@Autowired
	private IStaffService staffService;
	/**
	 * 添加取派员
	 */
	public String add(){
		staffService.save(getModel());
		return LIST;
	}
	
	
	/**
	 * 分页查询方法
	 * @throws IOException 
	 */
	public String pageQuery() throws IOException{
		staffService.pageQuery(pageBean);
		//使用json-lib将PageBean对象转为json
		//通过输出流向客户端浏览器写回json数据
		this.writeObject2Json(pageBean, new String[]{"currentPage","pageSize","detachedCriteria","decidedzones"});
		return NONE;
	}

	private String ids;
	/**
	 * 批量删除
	 */
	//执行这个方法，需要当前用户具有staff.delete这个权限
	@RequiresPermissions("staff.delete")
	public String delete(){
		staffService.deleteBatch(ids);
		return LIST;
	}
	
	/**
	 * 修改取派员信息
	 */
	public String edit(){
		//当前用户对象
		Subject subject = SecurityUtils.getSubject();
		subject.checkPermission("staff.edit");//检查当前用户是否具有staff.edit这个权限
		
		//先查询数据库中原始数据
		String id = getModel().getId();
		Staff staff = staffService.findById(id);
		//使用model进行覆盖
		staff.setName(model.getName());
		staff.setTelephone(model.getTelephone());
		staff.setStation(model.getStation());
		staff.setStandard(model.getStandard());
		staff.setHaspda(model.getHaspda());
		
		staffService.update(staff);
		return LIST;
	}
	
	public String getIds() {
		return ids;
	}

	public void setIds(String ids) {
		this.ids = ids;
	}
	
	/**
	 * 查询未作废的取派员数据，返回json
	 */
	public String listajax(){
		List<Staff> list = staffService.findListNotDelete();
		this.writeList2Json(list, new String[]{"decidedzones"});
		return NONE;
	}
}

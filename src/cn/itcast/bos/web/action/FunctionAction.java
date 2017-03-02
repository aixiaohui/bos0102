package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.Function;
import cn.itcast.bos.service.IFunctionService;
import cn.itcast.bos.utils.BOSContext;
import cn.itcast.bos.web.action.base.BaseAction;

/**
 * 权限管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class FunctionAction extends BaseAction<Function>{
	//注入Service
	@Autowired
	private IFunctionService functionService;
	/**
	 * 查询所有权限，返回json
	 */
	public String listajax(){
		List<Function> list = functionService.findAll();
		this.writeList2Json(list, new String[]{"parentFunction","children","roles"});
		return NONE;
	}
	
	/**
	 * 添加权限
	 */
	public String add(){
		functionService.save(model);
		return LIST;
	}
	
	/**
	 * 分页查询
	 * @throws IOException 
	 */
	public String pageQuery() throws IOException{
		String page = model.getPage();
		pageBean.setCurrentPage(Integer.parseInt(page));
		functionService.pageQuery(pageBean);
		this.writeObject2Json(pageBean, new String[]{"currentPage","pageSize","detachedCriteria","parentFunction","children","roles"});
		return NONE;
	}
	
	/**
	 * 根据登录人查询对应的菜单数据
	 */
	public String findMenu(){
		List<Function> list = functionService.findMenuByUser(BOSContext.getLoginUser());
		this.writeList2Json(list, new String[]{"parentFunction","children","roles"});
		return NONE;
	}
}

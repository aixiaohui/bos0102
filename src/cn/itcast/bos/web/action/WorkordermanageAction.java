package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionContext;

import cn.itcast.bos.domain.Workordermanage;
import cn.itcast.bos.service.IWorkordermanageService;
import cn.itcast.bos.web.action.base.BaseAction;

/**
 * 工作单管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class WorkordermanageAction extends BaseAction<Workordermanage>{
	@Resource
	private IWorkordermanageService workordermanageService;
	/**
	 * 添加工作单
	 * @throws IOException 
	 */
	public String add() throws IOException{
		String f = "1";
		try{
			workordermanageService.save(model);
		}catch (Exception e) {
			e.printStackTrace();
			f = "0";
		}
		ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(f);
		return NONE;
	}
	
	/**
	 * 查询所有状态为“未启动”的工作单
	 */
	public String list(){
		List<Workordermanage> list = workordermanageService.findListNotStart();
		//压栈
		 ActionContext.getContext().getValueStack().set("list", list);
		return LIST;
	}
	
	/**
	 * 启动物流配送流程
	 */
	public String start(){
		String id = model.getId();//获取工作单id
		try{
			workordermanageService.start(id);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return "toList";
	}
}

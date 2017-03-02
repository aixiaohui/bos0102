package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.Workordermanage;
import cn.itcast.bos.service.IWorkordermanageService;
import cn.itcast.bos.utils.BOSContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 任务操作
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class TaskAction extends ActionSupport{
	//注入TaskService
	@Autowired
	private TaskService taskService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private IWorkordermanageService workordermanageService;
	/**
	 * 查询当前登录人的组任务
	 */
	public String findGroupTask(){
		//任务查询对象
		TaskQuery query = taskService.createTaskQuery();
		//添加过滤条件---组任务（根据候选人过滤）
		query.taskCandidateUser(BOSContext.getLoginUser().getId());
		List<Task> list = query.list();
		ActionContext.getContext().getValueStack().set("list", list);
		return "groupTaskList";
	}
	
	
	//接收任务id
	private String taskId;
	
	/**
	 * 根据任务id查询流程变量
	 * @throws IOException 
	 */
	public String showData() throws IOException{
		Map<String, Object> variables = taskService.getVariables(getTaskId());
		ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(variables.toString());
		return NONE;
	}
	
	/**
	 * 拾取组任务
	 */
	public String takeTask(){
		taskService.claim(getTaskId(), BOSContext.getLoginUser().getId());
		return "toGroupTaskList";
	}
	
	/**
	 * 查询个人任务
	 */
	public String findPersonalTask(){
		TaskQuery query = taskService.createTaskQuery();
		//添加过滤条件---根据当前登录用户过滤
		query.taskAssignee(BOSContext.getLoginUser().getId());
		List<Task> list = query.list();
		ActionContext.getContext().getValueStack().set("list", list);
		return "personalTaskList";
	}
	
	//接收审核结果
	private Integer check;
	
	/**
	 * 办理审核工作单任务
	 */
	public String checkWorkOrderManage(){
		if(check == null){//跳转到审核工作单页面
			//根据任务id查询Task对象
			Task task = taskService.createTaskQuery().taskId(getTaskId()).singleResult();
			//根据任务对象查询流程实例id
			String processInstanceId = task.getProcessInstanceId();
			//根据流程实例id查询流程实例对象
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			String workordermanageId = processInstance.getBusinessKey();
			//查询工作单信息
			Workordermanage workordermanage = workordermanageService.findById(workordermanageId);
			//压栈
			ActionContext.getContext().getValueStack().set("workordermanage", workordermanage);
			return "checkUI";
		}else{//办理审核任务
			workordermanageService.checkWorkOrderManage(taskId,check);
			return "toPersonalTaskList";
		}
	}

	//办理其他任务
	public String outStore(){
		taskService.complete(taskId);
		return "toPersonalTaskList";
	}
	
	public String transferGoods(){
		taskService.complete(taskId);
		return "toPersonalTaskList";
	}
	
	public String receive(){
		taskService.complete(taskId);
		return "toPersonalTaskList";
	}
	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Integer getCheck() {
		return check;
	}

	public void setCheck(Integer check) {
		this.check = check;
	}
}

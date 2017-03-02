package cn.itcast.bos.web.action;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.utils.ActivitiUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 流程实例管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class ProcessInstanceAction extends ActionSupport{
	@Autowired
	private RuntimeService runtimeService;
	/**
	 * 查询正在运行的流程实例
	 */
	public String list(){
		List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
		//压栈
		ActionContext.getContext().getValueStack().set("list", list);
		return "list";
	}
	
	//接收流程实例id
	private String id;
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 根据流程实例id查询对应的流程变量
	 * @throws IOException 
	 */
	public String findData() throws IOException{
		Map<String, Object> variables = runtimeService.getVariables(id);
		ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(variables.toString());
		return NONE;
	}
	
	/**
	 * 根据流程实例id查询坐标(定位div位置的)、部署id、图片名称(获取png图片输入流)
	 * @return
	 */
	public String showPng(){
		Map<String, Object> map = ActivitiUtils.getActivitiInfo(id, repositoryService, runtimeService);
		ActionContext.getContext().getValueStack().push(map);
		return "showPng";
	}

	private String deploymentId;
	private String imageName;
	
	@Autowired
	private RepositoryService repositoryService;
	
	/**
	 * 根据部署id和图片名称查询输入流
	 */
	public String viewImage(){
		InputStream pngStream = repositoryService.getResourceAsStream(deploymentId, imageName);
		ActionContext.getContext().getValueStack().set("pngStream", pngStream);
		return "viewImage";
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
}

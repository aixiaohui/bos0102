package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletOutputStream;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RuntimeServiceImpl;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionChainResult;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 流程定义管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class ProcessDefinitionAction extends ActionSupport{
	//注入activiti框架中对应的Service
	@Autowired
	private RepositoryService repositoryService;
	/**
	 * 查询最新版本的流程定义数据
	 */
	public String list(){
		//流程定义查询对象
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		//添加过滤条件---最新版本
		query.latestVersion();
		List<ProcessDefinition> list = query.list();
		//压入值栈
		ActionContext.getContext().getValueStack().set("list", list);
		return "list";
	}
	
	public void setZipFile(File zipFile) {
		this.zipFile = zipFile;
	}

	//接收上传的临时文件
	private File zipFile;
	
	/**
	 * 部署流程定义
	 * @throws FileNotFoundException 
	 */
	public String deploy() throws FileNotFoundException{
		DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
		//加载上传的zip文件
		try{
			deploymentBuilder.addZipInputStream(new ZipInputStream(new FileInputStream(zipFile)));
			deploymentBuilder.deploy();
		}catch (NullPointerException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return "toList";
	}
	
	public void setId(String id) {
		this.id = id;
	}

	//接收流程定义 id
	private String id;
	
	/**
	 * 查看png图片
	 * @throws IOException 
	 */
	public String showpng() throws IOException{
		InputStream pngStream = repositoryService.getProcessDiagram(id);
		/*ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();
		byte[] b = new byte[1024];
		pngStream.read(b);
		out.write(b);
		out.flush();
		out.close();
		pngStream.close();*/
		//将pngStream压入值栈
		ActionContext.getContext().getValueStack().set("pngStream", pngStream);
		return "showpng";
	}
}

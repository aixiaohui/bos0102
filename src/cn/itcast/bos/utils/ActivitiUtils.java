package cn.itcast.bos.utils;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

public class ActivitiUtils {
	/**
	 * 根据流程实例id查询坐标、部署id、png图片名称
	 */
	public static Map<String, Object> getActivitiInfo(String id,
			RepositoryService repositoryService, RuntimeService runtimeService) {
		// 1、根据流程实例id查询流程实例对象
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery().processInstanceId(id)
				.singleResult();
		// 2、根据流程实例查询流程定义id
		String processDefinitionId = processInstance.getProcessDefinitionId();
		// 3、根据流程定义id查询流程定义对象
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		// 4、根据流程定义对象查询部署id和图片名称
		String pngName = processDefinition.getDiagramResourceName();
		String deploymentId = processDefinition.getDeploymentId();

		// 5、根据流程实例对象查询当前流程实例执行到哪个任务了
		String activityId = processInstance.getActivityId();// usertask3
		// 6、根据流程定义id查询bytearray表获得坐标相关信息
		ProcessDefinitionEntity pd = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processDefinitionId);
		// 内部包装了坐标
		ActivityImpl activityImpl = pd.findActivity(activityId);
		int x = activityImpl.getX();
		int y = activityImpl.getY();
		int width = activityImpl.getWidth();
		int height = activityImpl.getHeight();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("x", x);
		map.put("y", y);
		map.put("width", width);
		map.put("height", height);
		map.put("pngName", pngName);
		map.put("deploymentId", deploymentId);
		return map;
	}
}

package cn.itcast.bos.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.IWorkordermanageDao;
import cn.itcast.bos.domain.Workordermanage;
import cn.itcast.bos.service.IWorkordermanageService;
@Service
@Transactional
public class WorkordermanageServiceImpl implements IWorkordermanageService {
	@Autowired
	private IWorkordermanageDao workordermanageDao ;
	public void save(Workordermanage model) {
		model.setUpdatetime(new Date());
		workordermanageDao.save(model);
	}
	
	/**
	 * 查询所有状态为“未启动”的工作单
	 */
	public List<Workordermanage> findListNotStart() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Workordermanage.class);
		//添加过滤条件---start等于0
		detachedCriteria.add(Restrictions.eq("start", "0"));
		return workordermanageDao.findByCriteria(detachedCriteria);
	}

	@Autowired
	private RuntimeService runtimeService;
	
	/**
	 * 启动配送流程
	 */
	public void start(String id) {
		//根据工作单id查询工作单对象
		Workordermanage workordermanage = workordermanageDao.findById(id);
		workordermanage.setStart("1");//已启动
		
		String processDefinitionKey = "transfer";//物流配送流程的key
		//业务键作用是：使工作流框架可以找到业务数据
		String businessKey = id;//业务键,通常设置的值为当前对应的业务表(工作单表)的主键值
		Map<String, Object> variables = new HashMap<String, Object>();//流程变量
		variables.put("业务数据", workordermanage);
		runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
	}

	public Workordermanage findById(String workordermanageId) {
		return workordermanageDao.findById(workordermanageId);
	}

	@Autowired
	private TaskService taskService;
	@Autowired
	private HistoryService historyService;
	
	/**
	 * 办理审核工作单任务
	 */
	public void checkWorkOrderManage(String taskId, Integer check) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		//根据任务对象查询流程实例id
		String processInstanceId = task.getProcessInstanceId();
		//根据流程实例id查询流程实例对象
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
		String workordermanageId = processInstance.getBusinessKey();
		//查询工作单信息(持久状态对象)
		Workordermanage workordermanage = workordermanageDao.findById(workordermanageId);
		workordermanage.setManagerCheck("1");//改为1，表示已审核
		
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("check", check);
		taskService.complete(taskId, variables);//设置流程变量
		
		if(check == 0){//审核不通过
			//将工作单中的start重新改为0
			workordermanage.setStart("0");
			//手动删除历史流程实例数据
			historyService.deleteHistoricProcessInstance(processInstanceId);
		}
	}
}

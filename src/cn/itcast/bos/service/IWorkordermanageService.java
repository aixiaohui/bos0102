package cn.itcast.bos.service;

import java.util.List;

import cn.itcast.bos.domain.Workordermanage;

public interface IWorkordermanageService {

	public void save(Workordermanage model);

	public List<Workordermanage> findListNotStart();

	public void start(String id);

	public Workordermanage findById(String workordermanageId);

	public void checkWorkOrderManage(String taskId, Integer check);

}

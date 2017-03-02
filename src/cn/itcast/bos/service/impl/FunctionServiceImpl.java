package cn.itcast.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.IFunctionDao;
import cn.itcast.bos.domain.Function;
import cn.itcast.bos.domain.User;
import cn.itcast.bos.service.IFunctionService;
import cn.itcast.bos.utils.PageBean;
@Service
@Transactional
public class FunctionServiceImpl implements IFunctionService {
	@Autowired
	private IFunctionDao functionDao;
	public List<Function> findAll() {
		return functionDao.findAll();
	}
	public void save(Function model) {
		if(model.getParentFunction().getId().equals("")){
			model.setParentFunction(null);
		}
		functionDao.save(model);
	}
	public void pageQuery(PageBean pageBean) {
		functionDao.pageQuery(pageBean);
	}
	/**
	 * 根据登录人查询对应的菜单
	 */
	public List<Function> findMenuByUser(User user) {
		if(user.getUsername().equals("admin")){
			return functionDao.findAllMenu();
		}else{
			return functionDao.findMenuByUserId(user.getId());
		}
	}
}

package cn.itcast.bos.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.itcast.bos.dao.IStaffDao;
import cn.itcast.bos.domain.Staff;
import cn.itcast.bos.service.IStaffService;
import cn.itcast.bos.utils.PageBean;

@Service
@Transactional
public class StaffServiceImpl implements IStaffService{
	@Resource
	private IStaffDao staffDao;
	public void save(Staff model) {
		staffDao.save(model);
	}
	
	/**
	 * 取派员分页查询
	 */
	public void pageQuery(PageBean pageBean) {
		staffDao.pageQuery(pageBean);
	}

	/**
	 * 批量删除取派员，逻辑删除，将staff的deltag改为1
	 */
	public void deleteBatch(String ids) {//1,2,3,4
		String[] staffIds = ids.split(",");
		for (String id : staffIds) {
			staffDao.executeUpdate("deleteById", id);
		}
	}
	
	public Staff findById(String id) {
		return staffDao.findById(id);
	}
	
	public void update(Staff staff) {
		staffDao.update(staff);
	}

	//查询所有未作废的取派员-----deltag == 0
	public List<Staff> findListNotDelete() {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Staff.class);
		//添加查询条件
		//detachedCriteria.add(Restrictions.eq("deltag", "0"));
		detachedCriteria.add(Restrictions.ne("deltag", "1"));
		//添加排序条件
		detachedCriteria.addOrder(Order.desc("id"));
		return staffDao.findByCriteria(detachedCriteria);
	}
}

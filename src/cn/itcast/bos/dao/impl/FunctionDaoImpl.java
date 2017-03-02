package cn.itcast.bos.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import cn.itcast.bos.dao.IFunctionDao;
import cn.itcast.bos.dao.base.impl.BaseDaoImpl;
import cn.itcast.bos.domain.Function;

@Repository
public class FunctionDaoImpl extends BaseDaoImpl<Function> implements
		IFunctionDao {
	/**
	 * 根据用户id查询其对应的实际权限
	 */
	public List<Function> findFunctionsByUserId(String userId) {
		String hql = "SELECT DISTINCT f FROM Function f LEFT OUTER JOIN f.roles r "
				+ "LEFT OUTER JOIN r.users u WHERE u.id = ?";
		return this.getHibernateTemplate().find(hql, userId);
	}

	/**
	 * 查询所有菜单
	 */
	public List<Function> findAllMenu() {
		String hql = "FROM Function f WHERE f.generatemenu = '1' ORDER BY f.zindex DESC";
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * 根据用户id查询其对应的菜单
	 */
	public List<Function> findMenuByUserId(String userId) {
		String hql = "SELECT DISTINCT f FROM Function f LEFT OUTER JOIN f.roles r "
				+ "LEFT OUTER JOIN r.users u WHERE u.id = ? AND f.generatemenu = '1' " +
				"ORDER BY f.zindex DESC";
		return this.getHibernateTemplate().find(hql, userId);
	}
}

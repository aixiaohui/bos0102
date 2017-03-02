package cn.itcast.bos.dao.base;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import cn.itcast.bos.domain.Region;
import cn.itcast.bos.utils.PageBean;

/**
 * 持久层通用方法抽取
 * @author zhaoqx
 *
 * @param <T>
 */
public interface IBaseDao<T> {
    public void save(T entity);
	public void delete(T entity);
	public void update(T entity);
	public T findById(Serializable id);
	public List<T> findAll();
	public void saveOrUpdate(T entity);
	//通用更新方法
	public void executeUpdate(String queryName,Object...objects);
	//通用分页查询方法
	public void pageQuery(PageBean pageBean);
	//根据离线查询条件进行查询
	public List<T> findByCriteria(DetachedCriteria detachedCriteria);
}

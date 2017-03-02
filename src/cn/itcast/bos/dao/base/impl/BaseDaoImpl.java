package cn.itcast.bos.dao.base.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import cn.itcast.bos.dao.base.IBaseDao;
import cn.itcast.bos.utils.PageBean;
/**
 * 持久层通用实现
 * @author zhaoqx
 *
 * @param <T>
 */
public class BaseDaoImpl<T> extends HibernateDaoSupport implements IBaseDao<T> {
	private  Class<T> entityClass;//操作的实体的类型
	
	//注入SessionFactory对象
	@Resource
	public void setMySessionFactory(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * 在构造方法中动态获得entityClass
	 */
	public BaseDaoImpl() {
		//获得父类（BaseDaoImpl）类型
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		//获得父类上声明的泛型数组
		Type[] actualTypeArguments = type.getActualTypeArguments();
		entityClass = (Class<T>) actualTypeArguments[0];
	}
	
	public void save(T entity) {
		this.getHibernateTemplate().save(entity);
	}

	public void delete(T entity) {
		this.getHibernateTemplate().delete(entity);
	}

	public void update(T entity) {
		this.getHibernateTemplate().update(entity);
	}

	public T findById(Serializable id) {
		return this.getHibernateTemplate().get(entityClass, id);
	}

	public List<T> findAll() {
		String hql = "FROM " + entityClass.getSimpleName();
		return this.getHibernateTemplate().find(hql);
	}

	/**
	 * 通用更新
	 */
	public void executeUpdate(String queryName, Object... objects) {
		//根据queryName获得一个查询对象
		Query query = this.getSession().getNamedQuery(queryName);
		int i = 0;
		for (Object arg : objects) {
			query.setParameter(i++,arg);
		}
		query.executeUpdate();
	}

	/**
	 * 通用分页查询方法实现
	 */
	public void pageQuery(PageBean pageBean) {
		int currentPage = pageBean.getCurrentPage();
		int pageSize = pageBean.getPageSize();
		DetachedCriteria detachedCriteria = pageBean.getDetachedCriteria();
		//查询total----总记录数
		//改变hibernate框架发出的sql形式,select count(*) from xxx...
		detachedCriteria.setProjection(Projections.rowCount());
		List<Long> countList = this.getHibernateTemplate().findByCriteria(detachedCriteria);
		Long total = countList.get(0);
		pageBean.setTotal(total.intValue());
		//重新将hibernate框架发出的sql修改为select * from xxx..
		detachedCriteria.setProjection(null);
		detachedCriteria.setResultTransformer(DetachedCriteria.ROOT_ENTITY);
		int firstResult = (currentPage - 1) * pageSize;
		int maxResults = pageSize;
		//查询rows-----当前页要展示的数据集合
		List rows = this.getHibernateTemplate().findByCriteria(detachedCriteria, firstResult, maxResults);
		pageBean.setRows(rows);
	}

	public void saveOrUpdate(T entity) {
		this.getHibernateTemplate().saveOrUpdate(entity);
	}

	//根据任意条件查询
	public List<T> findByCriteria(DetachedCriteria detachedCriteria) {
		return this.getHibernateTemplate().findByCriteria(detachedCriteria);
	}
}

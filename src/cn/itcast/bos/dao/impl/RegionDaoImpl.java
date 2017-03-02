package cn.itcast.bos.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.itcast.bos.dao.IRegionDao;
import cn.itcast.bos.dao.base.impl.BaseDaoImpl;
import cn.itcast.bos.domain.Region;

@Repository
public class RegionDaoImpl extends BaseDaoImpl<Region> implements IRegionDao{

	public List<Region> findByQ(String q) {
		String hql = "FROM Region r WHERE r.province LIKE ? OR r.city LIKE ? OR r.district LIKE ?";
		return this.getHibernateTemplate().find(hql,"%"+q+"%","%"+q+"%","%"+q+"%");
	}

}

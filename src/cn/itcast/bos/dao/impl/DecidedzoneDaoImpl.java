package cn.itcast.bos.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import cn.itcast.bos.dao.IDecidedzoneDao;
import cn.itcast.bos.dao.base.impl.BaseDaoImpl;
import cn.itcast.bos.domain.Decidedzone;
import cn.itcast.bos.utils.PageBean;

@Repository
public class DecidedzoneDaoImpl extends BaseDaoImpl<Decidedzone> implements
		IDecidedzoneDao {

}

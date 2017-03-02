package cn.itcast.bos.service.impl;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.bos.dao.IDecidedzoneDao;
import cn.itcast.bos.dao.INoticebillDao;
import cn.itcast.bos.dao.IWorkbillDao;
import cn.itcast.bos.domain.Decidedzone;
import cn.itcast.bos.domain.Noticebill;
import cn.itcast.bos.domain.Staff;
import cn.itcast.bos.domain.User;
import cn.itcast.bos.domain.Workbill;
import cn.itcast.bos.service.INoticebillService;
import cn.itcast.crm.CustomerService;
@Service
@Transactional
public class NoticebillServiceImpl implements INoticebillService {
	@Autowired
	private INoticebillDao noticebillDao;
	//注入crm的代理对象
	@Autowired
	private CustomerService customerService;
	@Autowired
	private IDecidedzoneDao decidedzoneDao;
	@Autowired
	private IWorkbillDao workbillDao;
	
	public void save(Noticebill model, User user) {
		model.setUser(user);
		noticebillDao.save(model);//保存一个业务通知单
		//尝试自动分单,根据取件地址查询crm服务，获取客户所关联到的定区id
		String decidedzoneId = customerService.findDecidedzoneIdByAddress(model.getPickaddress());
		if(decidedzoneId != null){
			//查询到了定区id，可以完成自动分单
			Decidedzone decidedzone = decidedzoneDao.findById(decidedzoneId);
			Staff staff = decidedzone.getStaff();
			model.setStaff(staff);//业务通知单关联匹配到的取派员
			model.setOrdertype("自动分单");
			//为取派员产生一个工单
			Workbill workbill  = new Workbill();
			workbill.setAttachbilltimes(0);
			workbill.setBuildtime(new Timestamp(System.currentTimeMillis()));//工单产生的时间---系统时间
			workbill.setNoticebill(model);//工单关联业务通知单
			workbill.setPickstate("未取件");//取件状态
			workbill.setRemark(model.getRemark());//备注
			workbill.setStaff(staff);//工单关联取派员
			workbill.setType("新单");//工单类型
			//保存工单
			workbillDao.save(workbill);
			//通过webservice等技术调用短信服务
		}else{
			//不能完成自动分单，转入人工分单
			model.setOrdertype("人工分单");
		}
	}
}

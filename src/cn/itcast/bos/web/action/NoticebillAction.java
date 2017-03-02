package cn.itcast.bos.web.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.Noticebill;
import cn.itcast.bos.domain.User;
import cn.itcast.bos.service.INoticebillService;
import cn.itcast.bos.utils.BOSContext;
import cn.itcast.bos.web.action.base.BaseAction;
import cn.itcast.crm.CustomerService;
import cn.itcast.crm.domain.Customer;

@Controller
@Scope("prototype")
public class NoticebillAction extends BaseAction<Noticebill>{
	//注入crm代理对象
	@Autowired
	private CustomerService proxy;
	@Autowired
	private INoticebillService noticebillService;
	
	/**
	 * 根据手机号查询客户信息
	 */
	public String findCustomerByTelephone(){
		Customer customer = proxy.findByTelephone(model.getTelephone());
		//将Customer对象转为json返回
		this.writeObject2Json(customer, new String[]{});
		return NONE;
	}
	
	/**
	 * 添加业务通知单，并尝试自动分单
	 */
	public String add(){
		User user = BOSContext.getLoginUser();
		noticebillService.save(model,user);
		return "noticebill_add";
	}
}

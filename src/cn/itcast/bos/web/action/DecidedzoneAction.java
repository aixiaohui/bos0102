package cn.itcast.bos.web.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.Decidedzone;

import cn.itcast.bos.service.IDecidedzoneService;
import cn.itcast.bos.web.action.base.BaseAction;
import cn.itcast.crm.CustomerService;
import cn.itcast.crm.domain.Customer;

/**
 * 定区管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class DecidedzoneAction extends BaseAction<Decidedzone>{
	private String[] subareaid;
	@Autowired
	private IDecidedzoneService decidedzoneService;
	/**
	 * 添加定区
	 */
	public String add(){
		decidedzoneService.save(model,subareaid);
		return LIST;
	}
	
	public void setSubareaid(String[] subareaid) {
		this.subareaid = subareaid;
	}
	
	public String pageQuery(){
		decidedzoneService.pageQuery(pageBean);
		this.writeObject2Json(pageBean, new String[]{"subareas","decidedzones"});
		return NONE;
	}
	
	//注入代理对象，调用crm服务
	@Autowired
	private CustomerService customerService;
	
	/**
	 * 查询未关联到定区的客户
	 */
	public String findnoassociationCustomers(){
		List<Customer> list = customerService.findnoassociationCustomers();
		this.writeList2Json(list, new String[]{});
		return NONE;
	}
	
	/**
	 * 查询已经关联到指定定区的客户
	 */
	public String findhasassociationCustomers(){
		String id = model.getId();
		List<Customer> list = customerService.findhasassociationCustomers(id);
		this.writeList2Json(list, new String[]{});
		return NONE;
	}
	
	private Integer[] customerIds;//客户id
	
	/**
	 * 定区关联客户
	 */
	public String assigncustomerstodecidedzone(){
		String id = model.getId();
		customerService.assignCustomersToDecidedZone(customerIds, id);
		return LIST;
	}

	public Integer[] getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(Integer[] customerIds) {
		this.customerIds = customerIds;
	}
}

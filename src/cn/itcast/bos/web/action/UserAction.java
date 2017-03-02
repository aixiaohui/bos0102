package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.User;
import cn.itcast.bos.service.IUserService;
import cn.itcast.bos.utils.BOSContext;
import cn.itcast.bos.utils.MD5Utils;
import cn.itcast.bos.web.action.base.BaseAction;
import cn.itcast.crm.CustomerService;
import cn.itcast.crm.domain.Customer;

/**
 * 用户管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")//指定Action为多例
public class UserAction extends BaseAction<User> {
	//接收页面输入的验证码
	private String checkcode;
	@Autowired
	private IUserService userService;
	/**
	 * 使用shiro框架提供的方式进行认证
	 */
	public String login(){
		//从session中获取生成的验证码
		String key = (String) ServletActionContext.getRequest().getSession().getAttribute("key");
		if(StringUtils.isNotBlank(checkcode) && checkcode.equals(key)){
			//验证码输入正确
			//使用shiro框架提供的方式进行认证
			Subject subject = SecurityUtils.getSubject();//获取当前用户对象，状态为“未认证”
			String password = MD5Utils.md5(model.getPassword());
			//用户名密码令牌，对象内部包装了页面提交过来的用户名和密码
			AuthenticationToken token = new UsernamePasswordToken(model.getUsername(), password);
			try{
				subject.login(token);
			}catch (UnknownAccountException e) {//账号不存在异常
				e.printStackTrace();
				//认证失败
				this.addActionError("用户名或者密码错误！");
				return LOGIN;
			}catch (IncorrectCredentialsException e) {//密码错误异常
				this.addActionError("用户名或者密码错误！");
				e.printStackTrace();
				return LOGIN;
			}
			//认证通过
			//获取当前登录用户
			User user = (User) subject.getPrincipal();
			ServletActionContext.getRequest().getSession().setAttribute("loginUser", user);
			return HOME;
		}else{
			//验证码输入错误,设置错误提示信息，跳转到登录页面
			this.addActionError("验证码输入错误！");
			return LOGIN;
		}
	}
	
	/**
	 * 登录方法
	 */
	public String login_bak(){
		//从session中获取生成的验证码
		String key = (String) ServletActionContext.getRequest().getSession().getAttribute("key");
		if(StringUtils.isNotBlank(checkcode) && checkcode.equals(key)){
			//验证码输入正确
			User user = userService.login(getModel());
			if(user != null){
				//登录成功，跳转到系统首页,将User对象放入session中
				ServletActionContext.getRequest().getSession().setAttribute("loginUser", user);
				return HOME;
			}else{
				//登录失败，设置错误信息，跳转到登录页面
				this.addActionError("用户名或者密码错误！");
				return LOGIN;
			}
		}else{
			//验证码输入错误,设置错误提示信息，跳转到登录页面
			this.addActionError("验证码输入错误！");
			return LOGIN;
		}
	}
	
	/**
	 * 注销
	 */
	public String logout(){
		ServletActionContext.getRequest().getSession().invalidate();
		return LOGIN;
	}
	
	/**
	 * 修改密码
	 * @throws IOException 
	 */
	public String editPassword() throws IOException{
		String password = getModel().getPassword();
		String id = BOSContext.getLoginUser().getId();
		String f = "1";
		try{
			userService.editPassword(password,id);
		}catch (Exception e) {
			e.printStackTrace();
			f = "0";
		}
		ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(f);
		return NONE;
	}
	
	//接收多个角色id
	private String[] roleIds;
	
	/**
	 * 添加用户
	 */
	public String add(){
		userService.save(model,roleIds);
		return LIST;
	}
	
	/**
	 * 分页查询
	 */
	public String pageQuery(){
		userService.pageQuery(pageBean);
		this.writeObject2Json(pageBean, new String[]{"noticebills","roles"});
		return NONE;
	}
	
	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	public String[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String[] roleIds) {
		this.roleIds = roleIds;
	}
}

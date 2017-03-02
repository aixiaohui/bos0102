package cn.itcast.bos.web.interceptor;

import org.apache.struts2.ServletActionContext;
import cn.itcast.bos.domain.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 实现用户未登录自动跳转到登录页面
 * @author zhaoqx
 *
 */
public class BOSLoginInterceptor extends MethodFilterInterceptor{
	/**
	 * 拦截的方法 
	 */
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		ActionProxy proxy = invocation.getProxy();
		String namespace = proxy.getNamespace();
		String actionName = proxy.getActionName();
		String url = namespace + actionName;
		System.out.println("客户端访问URL："+url);
		//从session中获取当前登录用户
		User user = (User) ServletActionContext.getRequest().getSession().getAttribute("loginUser");
		if(user == null){
			//没有登录，跳转到登录页面
			return "login";
		}else{
			//已经登录，放行
			return invocation.invoke();
		}
	}
}

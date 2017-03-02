package cn.itcast.bos.shiro;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import cn.itcast.bos.dao.IFunctionDao;
import cn.itcast.bos.dao.IUserDao;
import cn.itcast.bos.dao.base.IBaseDao;
import cn.itcast.bos.domain.Function;
import cn.itcast.bos.domain.User;

/**
 * 自定义realm，进行认证和授权
 * @author zhaoqx
 *
 */
public class BOSRealm extends AuthorizingRealm{
	@Autowired
	private IUserDao userDao;
	//认证方法
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) 
				throws AuthenticationException {
		System.out.println("======认证方法=====");
		UsernamePasswordToken upToken = (UsernamePasswordToken)token;
		String username = upToken.getUsername();//页面提交过来的用户名
		char[] password = upToken.getPassword();
		//根据username查询数据库
		User user = userDao.findUserByUsername(username);
		if(user == null){
			//用户名不存在
			return null;
		}
		
		//包装一个简单认证信息对象
		//参数一：任意类型的对象，和线程绑定
		//参数二：数据库中存储密码
		//参数三：当前realm的名称（字符串）
		AuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
		return info;
	}
	
	@Autowired
	private IFunctionDao functionDao;
	//授权方法
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		User user = (User) principals.getPrimaryPrincipal();
		Subject subject = SecurityUtils.getSubject();
		User user2 = (User) subject.getPrincipal();
		System.out.println(user == user2);
		
		List<Function> list = null;
		if(user.getUsername().equals("admin")){
			//当前登录用户为超级管理员,查询所有权限，为其授权
			list = functionDao.findAll();
		}else{
			//查询当前登录用户实际所对应的权限
			list = functionDao.findFunctionsByUserId(user.getId());
		}
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		for (Function function : list) {
			info.addStringPermission(function.getCode());
		}
		
		return info;
	}
}

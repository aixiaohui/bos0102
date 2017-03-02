package cn.itcast.bos.web.action.base;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;

import cn.itcast.bos.domain.Staff;
import cn.itcast.bos.utils.PageBean;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 表现层抽取
 * @author zhaoqx
 *
 * @param <T>
 */
public class BaseAction<T> extends ActionSupport implements ModelDriven<T>{
	protected T model;//模型对象
	public T getModel() {
		return model;
	}

	public static final String HOME = "home";
	public static final String LIST = "list";
	
	protected PageBean pageBean = new PageBean();
	//使用离线条件查询对象包装查询条件
	DetachedCriteria detachedCriteria = null;//DetachedCriteria.forClass(Staff.class);
	
	public void setPage(int page) {
		pageBean.setCurrentPage(page);
	}

	public void setRows(int rows) {
		pageBean.setPageSize(rows);
	}
	
	/**
	 * 将对象转为json数据，并通过输出流写回客户端浏览器
	 */
	public void writeObject2Json(Object object,String[] excludes){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);//指定哪些属性不需要转json
		String json = JSONObject.fromObject(object,jsonConfig).toString();
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		try {
			ServletActionContext.getResponse().getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将List对象转为json数据，并通过输出流写回客户端浏览器
	 */
	public void writeList2Json(List list,String[] excludes){
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(excludes);//指定哪些属性不需要转json
		String json = JSONArray.fromObject(list,jsonConfig).toString();
		ServletActionContext.getResponse().setContentType("text/json;charset=UTF-8");
		try {
			ServletActionContext.getResponse().getWriter().print(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 在构造方法中动态获取T的类型，并通过反射创建对象
	 */
	public BaseAction() {
		//获得父类（BaseAction）类型
		ParameterizedType type = null;//(ParameterizedType) this.getClass().getGenericSuperclass();
		Type genericSuperclass = this.getClass().getGenericSuperclass();
		if(genericSuperclass instanceof ParameterizedType){
			type = (ParameterizedType) this.getClass().getGenericSuperclass();
		}else{
			type = (ParameterizedType) this.getClass().getSuperclass().getGenericSuperclass();
		}
		//获得父类上声明的泛型数组
		Type[] actualTypeArguments = type.getActualTypeArguments();
		Class<T> entityClass = (Class<T>) actualTypeArguments[0];
		//获取类型后，创建离线条件查询对象
		detachedCriteria = DetachedCriteria.forClass(entityClass);
		pageBean.setDetachedCriteria(detachedCriteria);
		try {
			//通过反射创建model对象
			model = entityClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}

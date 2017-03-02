package cn.itcast.bos.web.action;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.bos.domain.Region;
import cn.itcast.bos.domain.Subarea;
import cn.itcast.bos.service.ISubareaService;
import cn.itcast.bos.utils.FileUtils;
import cn.itcast.bos.web.action.base.BaseAction;

/**
 * 分区管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class SubareaAction extends BaseAction<Subarea>{
	@Autowired
	private ISubareaService subareaService;
	/**
	 * 添加分区
	 */
	public String add(){
		System.out.println(model);
		subareaService.save(model);
		return LIST;
	}
	
	/**
	 * 分页查询
	 */
	public String pageQuery(){
		String addresskey = model.getAddresskey();
		Region region = model.getRegion();
		
		DetachedCriteria dc = pageBean.getDetachedCriteria();
		if(StringUtils.isNotBlank(addresskey)){
			//添加过滤条件---》》根据地址关键字模糊查询
			dc.add(Restrictions.like("addresskey", "%"+addresskey+"%"));
		}
		
		if(region != null){
			String province = region.getProvince();
			String city = region.getCity();
			String district = region.getDistrict();
			dc.createAlias("region", "r");
			if(StringUtils.isNotBlank(province)){
				//进行多表关联查询,使用别名方式
				//添加过滤条件---》》根据省模糊查询
				dc.add(Restrictions.like("r.province", "%"+province+"%"));
			}
			if(StringUtils.isNotBlank(city)){
				//进行多表关联查询,使用别名方式
				//添加过滤条件---》》根据省模糊查询
				dc.add(Restrictions.like("r.city", "%"+city+"%"));
			}
			if(StringUtils.isNotBlank(district)){
				//进行多表关联查询,使用别名方式
				//添加过滤条件---》》根据省模糊查询
				dc.add(Restrictions.like("r.district", "%"+district+"%"));
			}
		}
		
		subareaService.pageQuery(pageBean);
		this.writeObject2Json(pageBean, new String[]{"currentPage","pageSize","detachedCriteria","subareas","decidedzone"});
		return NONE;
	}
	
	/**
	 * 分区数据导出功能
	 * @throws Exception 
	 */
	public String exportXls() throws Exception{
		//查询所有分区数据
		List<Subarea> list = subareaService.findAll();
		//使用POI在内存中创建Excel文件
		HSSFWorkbook workbook = new HSSFWorkbook();
		//创建一个sheet标签页
		HSSFSheet sheet = workbook.createSheet("分区数据");
		//创建标题行
		HSSFRow headRow = sheet.createRow(0);
		headRow.createCell(0).setCellValue("分区编号");
		headRow.createCell(1).setCellValue("分区关键字");
		headRow.createCell(2).setCellValue("详细地址");
		headRow.createCell(3).setCellValue("省市区");
		//遍历list集合,将集合中的数据写入Excel表格
		for (Subarea subarea : list) {
			//创建数据行
			HSSFRow dataRow = sheet.createRow(sheet.getLastRowNum() + 1);
			dataRow.createCell(0).setCellValue(subarea.getId());
			dataRow.createCell(1).setCellValue(subarea.getAddresskey());
			dataRow.createCell(2).setCellValue(subarea.getPosition());
			dataRow.createCell(3).setCellValue(subarea.getRegion().getName());
		}
		String filename = "分区数据.xls";
		String agent = ServletActionContext.getRequest().getHeader("User-Agent");//浏览器类型
		filename = FileUtils.encodeDownloadFilename(filename, agent);
		//使用输出流将内存中的Excel文件写到浏览器,文件下载
		//文件下载：一个流（输出流）、两个头（contentType content-disposition）
		ServletOutputStream out = ServletActionContext.getResponse().getOutputStream();
		String contentType = ServletActionContext.getServletContext().getMimeType(filename);
		ServletActionContext.getResponse().setContentType(contentType);
		ServletActionContext.getResponse().setHeader("content-disposition", "attchment;filename="+filename);
		workbook.write(out);
		return NONE;
	}
	
	/**
	 * 查询未分配到定区的分区，返回json
	 */
	public String listajax(){
		List<Subarea> list = subareaService.findListNotAssociation();
		this.writeList2Json(list, new String[]{"decidedzone","region"});
		return NONE;
	}
}

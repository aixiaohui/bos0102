package cn.itcast.bos.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.sun.org.apache.bcel.internal.generic.ARRAYLENGTH;

import cn.itcast.bos.domain.Region;
import cn.itcast.bos.service.IRegionService;
import cn.itcast.bos.utils.PageBean;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.base.BaseAction;

/**
 * 区域管理
 * @author zhaoqx
 *
 */
@Controller
@Scope("prototype")
public class RegionAction extends BaseAction<Region>{
	private File xlsName;
	@Autowired
	private IRegionService regionService;
	/**
	 * 区域导入方法
	 * @throws Exception 
	 * @throws FileNotFoundException 
	 */
	public String importXls() throws FileNotFoundException, Exception{
		List<Region> list = new ArrayList<Region>();
		String f = "0";
		try{
			//使用POI解析Excel文件
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(xlsName));
			//读取第一个sheet页
			HSSFSheet sheet = workbook.getSheetAt(0);
			for (Row row : sheet) {
				int rowNum = row.getRowNum();//行索引
				if(rowNum == 0){//标题行
					continue;
				}
				String id = row.getCell(0).getStringCellValue();
				String province = row.getCell(1).getStringCellValue();
				String city = row.getCell(2).getStringCellValue();
				String district = row.getCell(3).getStringCellValue();
				String postcode = row.getCell(4).getStringCellValue();
				
				Region region = new Region(id, province, city, district, postcode, null, null, null);
				
				//使用pinyin4J生成简码和城市编码
				province = province.substring(0, province.length() - 1);
				city = city.substring(0, city.length() - 1);
				district = district.substring(0, district.length() - 1);
				
				String info = province + city + district;//河北石家庄桥西--->>HBSJZQX
				String[] headByString = PinYin4jUtils.getHeadByString(info);
				String shortcode = StringUtils.join(headByString, "");
				System.out.println(shortcode);
				
				//城市编码---->>shijiazhuang
				String citycode = PinYin4jUtils.hanziToPinyin(city, "");
				System.out.println(citycode);
				region.setShortcode(shortcode);
				region.setCitycode(citycode);
				list.add(region);
			}
			//批量保存
			regionService.saveBatch(list);
		}catch (Exception e) {
			e.printStackTrace();
			f = "1";
		}
		ServletActionContext.getResponse().setContentType("text/html;charset=UTF-8");
		ServletActionContext.getResponse().getWriter().print(f);
		return NONE;
	}
	public void setXlsName(File xlsName) {
		this.xlsName = xlsName;
	}
	/**
	 * 分页查询
	 * @throws IOException 
	 */
	public String pageQuery() throws IOException{
		regionService.pageQuery(pageBean);
		this.writeObject2Json(pageBean, new String[]{"currentPage","pageSize","detachedCriteria","subareas"});
		return NONE;
	}
	
	private String q;//查询条件
	
	/**
	 * 查询所有的区域数据，返回json
	 */
	public String listajax(){
		List<Region> list = null;
		if(StringUtils.isNotBlank(q)){
			list = regionService.findListByQ(q);//模糊查询
		}else{
			list = regionService.findAll();
		}
		this.writeList2Json(list, new String[]{"subareas"});
		return NONE;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
}

package cn.itcast.bos.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.Test;

public class POITest {
	//使用ＰＯＩ解析Ｅｘｃｅｌ文件
	@Test
	public void test1() throws Exception, IOException{
		//加载D盘下的Excel文件
		HSSFWorkbook workbook =new HSSFWorkbook(new FileInputStream(new File("d:\\区域导入测试数据.xls")));
		//读取文件中第一个sheet页
		HSSFSheet sheet = workbook.getSheetAt(0);
		for (Row row : sheet) {
			System.out.println();
			for (Cell cell : row) {
				String value = cell.getStringCellValue();
				System.out.print(value + " ");
			}
		}
	}
}

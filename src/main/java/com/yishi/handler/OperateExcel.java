package com.yishi.handler;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;






import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yishi.util.AnalysisJson;
import com.yishi.util.CompareMap;
import com.yishi.util.DataToMap;
import com.yishi.util.HttpRequest;

public class OperateExcel {
	XSSFWorkbook workbook;
	String requestResult;
	XSSFSheet sheet;
	@SuppressWarnings("deprecation")
	public void chooseFile(String path,String tester) {
		String methodName = "请求方式";
		String urlName = "url请求地址";
		String paramName = "请求参数";
		String expectedName = "预期结果";
		String actualName = "实际结果";
		String testerName = "测试人";
		String memoName = "备注";
		workbook = initWorkbook(path);
		// XSSFSheet 代表 Excel 文件中的一张表格
		sheet = workbook.getSheetAt(0);
		for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
			// XSSFRow 代表一行数据
			XSSFRow row = sheet.getRow(rowIndex);
			if (row == null) {
				continue;
			}
			// 方法列的值
			XSSFCell methodCell = row.getCell(this.findKey(methodName));
			// url值
			XSSFCell urlCell = row.getCell(this.findKey(urlName));
			// 参数列的值
			XSSFCell paramCell = row.getCell(this.findKey(paramName));
			// 预期结果列的值
			XSSFCell expectedValueCell = row.getCell(this.findKey(expectedName));
			//实际结果
			XSSFCell actualValueCell = row.getCell(this.findKey(actualName));
			//测试人
			XSSFCell testerCell = row.getCell(this.findKey(testerName));
			//备注
			XSSFCell memoNameCell = row.getCell(this.findKey(memoName));
			
			//将测试人写入excel
			testerCell.setCellValue(tester);
			
			// 将预期值转成map值，便于比较
			Map<String, Object> expectedValue = DataToMap.stringtoMap(expectedValueCell.toString());

			// 获取HTTP请求结果
			String requestResult = getHttpResult(methodCell, urlCell, paramCell);
			
			try {
				JSONObject jsonObject = JSONObject.fromObject(requestResult);
				System.out.println("http接收到转换成JSONObject的值："+jsonObject);
				//将返回值填入excel表中
				actualValueCell.setCellValue(jsonObject.toString());
				//比较实际值与期望值是否一致
				boolean isSame=CompareValue(expectedValue, jsonObject);
				 if(isSame){
					 memoNameCell.setCellValue("通过");
			    	 
			       }else{
			    	   memoNameCell.setCellValue("不一致");
			    	   XSSFCellStyle cellStyle = workbook.createCellStyle(); 
			    	   cellStyle.setFillBackgroundColor(HSSFColor.RED.index);		
			    	   memoNameCell.setCellStyle(cellStyle);
			       }
			} catch (Exception e) {
				// 如果返回的结果不能转换成JSON格式，那么说明这个接口是有问题的
				System.out.println("返回值有误");
				memoNameCell.setCellValue("返回值有误");
				actualValueCell.setCellValue(requestResult);
			}
			
		}
		try {
			FileOutputStream os = new FileOutputStream(path);
			os.flush();
			workbook.write(os);
			os.close();
			// 操作完毕后，记得关闭的 XSSFWorkbook 
			workbook.close();

		} catch (Exception e1) {

			e1.printStackTrace();
		}

	}
	

	/*
	 * 
	 * 创建Excel文件对象
	 */
	public XSSFWorkbook initWorkbook(String path) {
		try {
			// 创建 Excel 文件的输入流对象
			FileInputStream excelFileInputStream = new FileInputStream(path);
			// XSSFWorkbook 就代表一个 Excel 文件
			// 创建其对象，就打开这个 Excel 文件
			workbook = new XSSFWorkbook(excelFileInputStream);
			// 输入流使用后，及时关闭！这是文件流操作中极好的一个习惯！
			excelFileInputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workbook;
	}

	/*
	 * 获取第一行的所有列名
	 */
	public ArrayList<XSSFCell> getRowNames(XSSFSheet sheet) {
		XSSFRow row = sheet.getRow(0);
		ArrayList<XSSFCell> firstRowNames = new ArrayList<XSSFCell>();
		for (int colIndex = 0; colIndex < sheet.getRow(0)
				.getPhysicalNumberOfCells(); colIndex++) {
			XSSFCell cellName = row.getCell(colIndex);
			firstRowNames.add(cellName);
		}
		return firstRowNames;
	}



	/*
	 * 通过列名找到所属列的序号
	 */
	public int findKey(String rowName) {
		// 获取第一行所有列名
		ArrayList<XSSFCell> allRowNames = getRowNames(sheet);
		for (int i = 0; i < allRowNames.size(); i++) {
			if (rowName.equals(allRowNames.get(i).toString())) {
				return i;
			}

		}
		System.out.println("未找到匹配的列名，请检查");
		return -1;
	}

	/*
	 * 获取http结果，返回值为String类型
	 */
	public String getHttpResult(XSSFCell methodCell, XSSFCell urlCell,
			XSSFCell paramCell) {
		if (methodCell.toString().equalsIgnoreCase("get")) {
			requestResult = HttpRequest.sendGet(urlCell.toString(),
					paramCell.toString());
		} else if (methodCell.toString().equalsIgnoreCase("post")) {
			requestResult = HttpRequest.sendPost(urlCell.toString(),
					paramCell.toString());
		}
		return requestResult;
	}

	/*
	 * 用预期值与返回值进行比较 如果预期值都能在返回中找到，那就结果一致， 如果找不到，就说明有问题。
	 */
	public boolean CompareValue(Map<String, Object> expectedValue,
			JSONObject actualValue) {
		// 用一个空MAP保存需要查找的key，并将值都置空
		Map<String, Object> m = new HashMap<String, Object>();
		 m.putAll(expectedValue);
		for (String key : m.keySet()) {
			m.put(key, null);
		}
		//调用查找方法。
		AnalysisJson.test2(actualValue, m);
		System.out.println("ExpectedMap"+expectedValue);
		System.out.println("actualMap"+m);
		//此时将两个map值进行比较，查询后的m以及从excel中读取的expectedValue
		return CompareMap.compare(expectedValue, m);
      
	}
	

}

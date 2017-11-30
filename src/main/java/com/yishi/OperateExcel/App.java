package com.yishi.OperateExcel;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.yishi.handler.OperateExcel;
/**
 * Hello world!
 *
 */
public class App 
{

  public static void main(String args[]){
	  String path="files/WeatherInterfaceTest.xlsx";
	  String tester="Rick";
	  OperateExcel oe=new OperateExcel();
	  oe.chooseFile(path,tester);
	  
  } 
    	
}

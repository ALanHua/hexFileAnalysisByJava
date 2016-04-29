package com.nanosic;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
public class FileHexAnalysis {
	//Code line define
	public static final int FIRST_CODE_LINE_SIZES=168*1024/16+2;
	public static final int READ_CODE_LINE_SIZES=88*1024/16;
	public static final int READ_CODE_BASE_LINE_SIZES=8*1024/16;
	public static final int SECOND_CODE_LINE_SIZES=80*1024/16+1;
	public static final int SECOND_CODE_BANKLINE_SIZES=24*1024/16+1;
	public static final int THIRD_CODE_BASE_LINE_SIZES=248*1024/16+3;
	//Address define
	public static final int SECOND_CODE_BASE_ADDR=40*1024;
	public static final int THIRD_CODE_BASE_ADDR=56*1024;
	/**
	 * 文件分析总函数
	 * @param filePath
	 * @throws IOException
	 */
	public boolean fileHexAnalysisMethod(String filePath) throws IOException{
		if((filePath==null)||(readPathToWritePath(filePath)==null))
			return false;
		PrintWriter out=new PrintWriter(new FileWriter(readPathToWritePath(filePath)),true);
		copyFirstCodeFlash(out,filePath);
		copySecondCodeFlash(out,filePath);
		copyThirdCodeFlash(out,filePath);
		out.close();
		return true;
	}
	/*********************************************************************
	 * 应用程序层
	 * @throws IOException 
     **********************************************************************/
	public String readPathToWritePath(String readPath){
		if(readPath.endsWith(".hex")){
			StringBuilder buf=new StringBuilder();
			String str=readPath.substring(0, readPath.length()-4);
			buf.append(str).append("_copy.hex");
			return buf.toString();
		}
		return null;
	}
	/**
	 * bank 行的解析
	 * @param str
	 * @return bank number
	 */
	public  int bankLineAnalysis(String str){
		if(str.startsWith(":02")&&str.substring(7,9).equals("04")){
			return Integer.parseInt(str.substring(12, 13));
		}
		return 0;
	}
	/**
	 * 文件结束检测
	 * @param str
	 * @return
	 */
	public  boolean endOfFileAnalysizs(String str){
		if(str.startsWith(":00")&&str.substring(7,9).equals("01")){
			return true;
		}
		return false;
	}
	/**
	 * 经过两个bank的切换，分别是:020000040001F9和:020000040002F8
	 * @param out
	 * @throws IOException
	 */
	public  void copyFirstCodeFlash(PrintWriter out,String filePath) 
			throws IOException{
		String str;
		BufferedReader in=new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath)));
		int fileHexLine=0;
		while((str=in.readLine())!=null&&fileHexLine<FIRST_CODE_LINE_SIZES){
			fileHexLine++;
			out.println(str);
		}
		in.close();
	}
	/**
	 * 经过一个bank的切换，如:020000040003F7
	 * @param out
	 * @throws IOException
	 */
	public  void copySecondCodeFlash(PrintWriter out,String filePath) 
			throws IOException{
		BufferedReader in=new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath)));
		String str;
		String fileBankStr=":020000040003F7";
		int fileHexLine=0;
		while(fileHexLine<READ_CODE_LINE_SIZES){
			fileHexLine++;//读取总行数限制
			str=in.readLine();
			if((bankLineAnalysis(str)==0)&&(fileHexLine>READ_CODE_BASE_LINE_SIZES)){//去除flash bank flag
				if((fileHexLine-READ_CODE_BASE_LINE_SIZES)<SECOND_CODE_BANKLINE_SIZES){
					out.println(DateAnalysis.getNewDateString(str,((fileHexLine-READ_CODE_BASE_LINE_SIZES-1)*16+SECOND_CODE_BASE_ADDR)));
				}else if((fileHexLine-READ_CODE_BASE_LINE_SIZES)==SECOND_CODE_BANKLINE_SIZES){
					out.println(fileBankStr);
					out.println(DateAnalysis.getNewDateString(str,(fileHexLine-READ_CODE_BASE_LINE_SIZES-SECOND_CODE_BANKLINE_SIZES)*16));
				}else {
					out.println(DateAnalysis.getNewDateString(str,(fileHexLine-READ_CODE_BASE_LINE_SIZES-SECOND_CODE_BANKLINE_SIZES)*16));
				}
			}else if(bankLineAnalysis(str)!=0){
				fileHexLine--;
			}
		}
		in.close();
	}
	/**
	 * 最后一块代码块
	 * @param out
	 * @throws IOException
	 */
	public  void copyThirdCodeFlash(PrintWriter out,String filePath)
			throws IOException{
		BufferedReader in=new BufferedReader(new InputStreamReader(
				new FileInputStream(filePath)));
		String str;
		//String fileEndStr=":00000001FF";
		int fileHexLine=0;
		while((str=in.readLine())!=null){
			fileHexLine++;
			if((fileHexLine>THIRD_CODE_BASE_LINE_SIZES)&&(!endOfFileAnalysizs(str))){
				//System.out.println(fileHexLine-THIRD_CODE_BASE_LINE_SIZES-1);
				out.println(DateAnalysis.getNewDateString(str,((fileHexLine-THIRD_CODE_BASE_LINE_SIZES-1)*16+THIRD_CODE_BASE_ADDR)));
			}else if(endOfFileAnalysizs(str)){
				out.println(str);
				break;
			}
		}
		in.close();
	}
}

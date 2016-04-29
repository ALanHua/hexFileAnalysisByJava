package com.nanosic;

import java.util.Arrays;

public class DateAnalysis {
	public static final int ADDR_BEGININDEX=3;
	public static final int ADDR_ENDINDEX=7;
	public static final int CHECKSUM_BEGININDEX=41;
	public static final int CHECKSUM_ENDINDEX=43;
	public static String getNewDateString(String src,int addr){
		if(src.startsWith(":")){
			//设置地址，将int地址转换成hexString;
			String addrHex=setIntAddrToHexString(addr,4);
			//将该字符串地址替换元字符串中地址串
			String newAddrSrc=replaceDateString(src,ADDR_BEGININDEX,ADDR_ENDINDEX,addrHex);
			//计算新字符串的校验和
			String newCheckSum=getDateCheckSum(newAddrSrc);
			//替换原字符串检验和
			return replaceDateString(newAddrSrc,CHECKSUM_BEGININDEX,CHECKSUM_ENDINDEX,newCheckSum);
		}
		return null;
	}
	public static String replaceDateString(String src,int beginIndex,
			int endIndex,String hex){
		if((hex==null) || (beginIndex<=0) ||(endIndex>src.length())||
				(hex.length()!=(endIndex-beginIndex)))
			return null;
		//得到字串
		StringBuilder bf=new StringBuilder();
		bf.append(src.substring(0, beginIndex));
		bf.append(hex);
		bf.append(src.substring(endIndex));
		return bf.toString();
	}
	public static String setIntAddrToHexString(int address,int size){
		String hex=Integer.toHexString(address).toUpperCase();
		return leftPad(hex,'0',size);
	}
	public static String leftPad(String hex,char c,int size){
		char[] cs=new char[size];
		Arrays.fill(cs, c);
		System.arraycopy(hex.toCharArray(), 0, cs, cs.length-hex.length(), hex.length());
		return new String(cs);
	}
	public static int byteDateToIntDate(byte highbyte,byte lowbyte){
		if((highbyte>='A')&&(highbyte<='F')){
			highbyte=(byte)(highbyte-55);
		}else if((highbyte>='0')&&(highbyte<='9')){
			highbyte=(byte)(highbyte-48);
		}
		if((lowbyte>='A')&&(lowbyte<='F')){
			lowbyte=(byte)(lowbyte-55);
		}else if((lowbyte>='0')&&(lowbyte<='9')){
			lowbyte=(byte)(lowbyte-48);
		}
		return highbyte*16+lowbyte;
	}
	public static String getDateCheckSum(String str){
		byte[] buf=str.getBytes();
		int sum=0;
		if(buf[0]==':'){
			for(int i=1;i<buf.length-2;i=i+2){
				sum+=byteDateToIntDate(buf[i],buf[i+1]);
			}
			//System.out.println(sum);
			sum=(0x100-sum)&0x000000FF;
			return leftPad(Integer.toHexString(sum).toUpperCase(),'0',2);
		}
		return null;
	}
}

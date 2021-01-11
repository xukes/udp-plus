package com.xukeer.udp.plus.utils;

import com.xukeer.udp.plus.common.exception.DataOutException;

public class Utils {
	/**
	 * 将int转成byte数组
	 * */
	public static byte[] intToBytes(int i) {	
	    byte[] targets = new byte[4];  
        targets[3] = (byte) (i & 0xFF);  
        targets[2] = (byte) (i >> 8 & 0xFF);  
        targets[1] = (byte) (i >> 16 & 0xFF);  
        targets[0] = (byte) (i >> 24 & 0xFF);  
        return targets;  
	}
	
	public static String byteArrToHexString(byte [] bArray) {
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	
	/**
	 *从一个数组里面取出一段
	 * @throws DataOutException
	 * */
	public static byte[] cutBytes(byte[] bytes, int start, int length) throws DataOutException {
		if(start < 0 || length < 0) {
			throw new DataOutException("索引的起始位置或长度应该大于0，当前 start: {"+start+"}, length: {"+length+"}" );
		}
		if(bytes.length < start + length) {
			throw new DataOutException("111111111");
		}
		byte [] retArr = new byte[length];
		for(int i = start; i < start + length; i ++) {
			retArr[i-start] = bytes[i];
		}
		return retArr;
	}

	/**
	 * 取两个数相除向上取整的值
	 * @param molecule 分子
	 * @param denominator 分母
	 * */
	public static long roundUpperNumbers(long molecule, int denominator){
		long kk = molecule/denominator;
		if(molecule % denominator == 0) {
			return kk;
		}
		return kk +1;
	}

	public static int getIntRand(){
		return (int) (Math.random() * Integer.MAX_VALUE);
	}

	public static void main(String[] args) {
		for(int i=0;i<1000;i++) {
			System.out.println(getIntRand());
		}
	}
}

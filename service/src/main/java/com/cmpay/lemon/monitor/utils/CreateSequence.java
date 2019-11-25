package com.cmpay.lemon.monitor.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


/**
 * 生成20位序列号(年月日时分秒+x位随机数)
 * @author bailin.qing
 *
 */
public class CreateSequence {
	private  static Integer temp=100001; 
	private static Integer temp5=10001;
	/**
	 * 默认生成20位(年月日时分秒+从100001~999999累加的6位数)
	 * @return sequence 序列号
	 */
	public static synchronized String getSequence(){
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String sequence=date+temp;
		if (temp<999999)temp++;
		else temp=100001;
		return sequence;
	}
	
	/**
	 * 默认生成20位(年月日时分秒+从100001~999999累加的5位数)
	 * @return sequence 序列号
	 */
	public static synchronized String getSequence_5(){
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String sequence=date+temp5;
		if (temp5<99999)temp5++;
		else temp5=10001;
		return sequence;
	}
	
	/**
	 * 生成20位序列号(年月日时分秒+6位随机数)
	 * @return sequence 序列号
	 */
	public static synchronized String getSeqByRandom(){
		String date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		Random random = new Random();
			int temp = Math.abs(random.nextInt());
			String sequence=date+(temp % (999999 - 100000 + 1) + 100000);
			return sequence;
	}

}
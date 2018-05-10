package com.tiansu.eam.test;

import com.tiansu.eam.common.utils.IdGen;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Test {

	public static void main1(String[] args) {
		String t = IdGen.uuid(12);
		System.out.println(t);
		System.out.println(t.length());

		int first = new Random(10).nextInt(8) + 1;
		int hashCodeV = Math.abs(UUID.randomUUID().toString().hashCode());
		// 0 代表前面补充0
		// 4 代表长度为4
		// d 代表参数为正数型
		String t2 = first + String.format("%015d", hashCodeV);
		System.out.println(t2);
		System.out.println(t2.length());

		for(int i = 0;i<100;i++){
//			System.out.println(new Random(12).nextInt(18) + 1);
			System.out.println(IdGen.uuid(12));
		}
	}

	public static void main(String[] args){
		String ss = "123.png";
		System.out.println(ss.substring(ss.lastIndexOf('.',ss.length())));
	}
}

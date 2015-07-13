package com.byhealth.test;

import java.util.UUID;

public class Test {
	public static void main(String[] args) {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		System.out.println(str);
		String newStr = str.replaceAll("-", "");
		System.out.println(newStr);
	}
}

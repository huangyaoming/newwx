package com.byhealth.common.utils;

import com.encryptcore.EnCryptCore;

public final class PasswordUtil {

	private PasswordUtil() {
	}

	private static String key = "fengjx-wechat-!@#";

	public static String encode(String conent) {
		String password = EnCryptCore.encrypt(key, conent);
		return password;

	}

	public static String decode(String conent) {
		String mingwen = EnCryptCore.decrypt(key, conent);
		return mingwen;
	}

	public static void main(String args[]) {
		String content = "BC29CD7FD3DA94D37840C1D88AD2FE35";
		System.out.println(encode(content));
		System.out
				.println(decode("7EFFE6E8456437B39CD11D2C283025EE3C7B4D88F4AAA5A462BC834FEA3C4515ACA3D33315EE60774521598134478BDD"));
	}

}

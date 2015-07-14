package com.byhealth.config;

import java.awt.image.BufferedImage;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class VerifyCodeUtil {

	private VerifyCodeUtil() {}
	
	private static final DefaultKaptcha kaptchaProducer;
	
	static {
		kaptchaProducer = new DefaultKaptcha();
		Prop prop = PropKit.use("kaptcha.properties", "UTF-8");
		Config config = new Config(prop.getProperties());
		kaptchaProducer.setConfig(config);
	}
	
	public static String createText() {
		return kaptchaProducer.createText();
	}
	
	public static BufferedImage createImage(String text) {
		return kaptchaProducer.createImage(text);
	}
}

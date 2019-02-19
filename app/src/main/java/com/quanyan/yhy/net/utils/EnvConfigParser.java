package com.quanyan.yhy.net.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;

public class EnvConfigParser {
    public EnvConfig parse(InputStream is) throws Exception {
    	EnvConfig envConfig = new EnvConfig();
		
		XmlPullParser parser = Xml.newPullParser();	//由android.util.Xml创建一个XmlPullParser实例
    	parser.setInput(is, "UTF-8");				//设置输入流 并指明编码方式
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("apiUrl")) {
						eventType = parser.next();
						envConfig.setApiUrl(parser.getText());
					} else if (parser.getName().equals("httpsApiUrl")) {
						eventType = parser.next();
						envConfig.setHttpsApiUrl(parser.getText());
					} else if (parser.getName().equals("imageUrl")) {
						eventType = parser.next();
						envConfig.setImageUrl(parser.getText());
					} else if (parser.getName().equals("uploadUrl")) {
						eventType = parser.next();
						envConfig.setUploadUrl(parser.getText());
					} else if (parser.getName().equals("appId")) {
						eventType = parser.next();
						envConfig.setAppId(parser.getText());
					} else if (parser.getName().equals("domainId")) {
						eventType = parser.next();
						envConfig.setDomainId(parser.getText());
					} else if (parser.getName().equals("publicKey")) {
						eventType = parser.next();
						envConfig.setPublicKey(parser.getText());
					}else if (parser.getName().equals("vodUrl")) {
						eventType = parser.next();
						envConfig.setVodUrl(parser.getText());
					}else if(parser.getName().equals("logUrl")) {
						eventType = parser.next();
						envConfig.setLogUrl(parser.getText());
					}else if(parser.getName().equals("defaultDomain")) {
						eventType = parser.next();
						envConfig.setDefaultDomain( parser.getText());
					}else if (parser.getName().equals("liveShareUrl")){
						eventType = parser.next();
						envConfig.setLiveShareUrl( parser.getText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
			}
			eventType = parser.next();
		}
		return envConfig;
	}
}

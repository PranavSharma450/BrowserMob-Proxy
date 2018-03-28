package com.maven.browserMob2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class LoadPlaceHolders {

	public static Map<String, String> getProperties() {
		Properties prop = new Properties();
		Map<String, String> propMap = new HashMap<String, String>();
		FileInputStream fis = null;
		try {

			fis = new FileInputStream("./resource/config.properties");
			prop.load(fis);
			// System.out.println("Loading properties content...");
			for (String key : prop.stringPropertyNames()) {
				// System.out.println("Key:"+key +"Value: "+prop.getProperty(key));
				propMap.put(key, prop.getProperty(key));
			}

		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return Collections.unmodifiableMap(propMap);
	}

	public static void main(String[] args) {

		Map<String, String> map = getProperties();

		Iterator<Entry<String, String>> itr = map.entrySet().iterator();
		System.out.println("========================================");
		while (itr.hasNext())
			System.out.println(itr.next());

	}

}

package com.colibri.social_story.config;

import java.io.IOException;
import java.io.InputStream;

public class Properties {
	private static java.util.Properties trProps = null;
		
	//Load static properties from the properties file
	private static void load() {
		try {
            InputStream inputStream =Properties.class.getResourceAsStream("/story.properties");

			trProps = new java.util.Properties();
			trProps.load(inputStream);
			
			inputStream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//Go get the property we want
	public static String getProperty(String property) {
		if(trProps == null)
			load();
				
		return(trProps.getProperty(property));
	}

}

package com.example.junsta.util;

import java.util.Random;

public class FilenameUtil {
	public static String getExtension(String strFileName) {
		int pos = strFileName.lastIndexOf(".");
		String ext = strFileName.substring(pos + 1);
		return ext;
	}
	
	public static String createRandomFileName(int length) {
		StringBuffer temp = new StringBuffer();
		Random rnd = new Random();
		for (int i = 0; i < length; i++) {
		    int rIndex = rnd.nextInt(3);
		    switch (rIndex) {
		    case 0:
		        // a-z
		        temp.append((char) ((int) (rnd.nextInt(26)) + 97));
		        break;
		    case 1:
		        // A-Z
		        temp.append((char) ((int) (rnd.nextInt(26)) + 65));
		        break;
		    case 2:
		        // 0-9
		        temp.append((rnd.nextInt(10)));
		        break;
		    }
		}

		return temp.toString().toLowerCase();
	}
}

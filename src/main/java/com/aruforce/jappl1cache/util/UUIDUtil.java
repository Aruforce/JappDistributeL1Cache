package com.aruforce.jappl1cache.util;

import java.util.UUID;

/**
 * @author Aruforce
 * @since  0.0.1
 */
public class UUIDUtil {
	public static  String getUuid(){
		return UUID.randomUUID().toString().replace("-","");
	}
}

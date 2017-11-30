package com.yishi.util;
import java.util.Map;


public class CompareMap {
	public static boolean compare(Map map1, Map map2) {
	    boolean contain = false;
	    for (Object o : map1.keySet()) {
	        contain = map2.containsKey(o);
	        if (contain) {
	            contain = map1.get(o).equals(map2.get(o));
	        }
	        if (!contain) {
	            return false;
	        }
	    }
	    return true;
	}
}

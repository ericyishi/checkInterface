package com.yishi.util;

import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class AnalysisJson {
	  public static void test2(Object o,Map<String,Object> m)  
	    {  
	        Object result;
			if(o.getClass().equals(JSONObject.class))  
	        {  
	           JSONObject temp= (JSONObject)o;  
	           for(String keyMap:m.keySet())  
	           {  
	               if(m.get(keyMap)==null)  
	               {  
	                    if(temp.containsKey(keyMap))  
	                    {  
	                        System.out.println("find out.....................");  
	                       // System.out.println(temp.get(keyCondition));  
	                        result=temp.get(keyMap);  
	                        m.put(keyMap, temp.get(keyMap));  
	                        //System.out.println(result);  
	                    }  
	                    else  
	                    {  
	                        Iterator it=temp.keySet().iterator();    
	                         while(it.hasNext()){      
	                               String key;      
	  
	                               key=it.next().toString();      
	                               Object v=temp.get(key);   
	                              if(v.getClass().equals(JSONObject.class)||v.getClass().equals(JSONArray.class))  
	                              {  
	                                  test2(v,m);  
	                              }  
	                          }     
	                    }  
	               }  
	           }  
	            
	        }  
	        else if(o.getClass().equals(JSONArray.class))  
	        {  
	            JSONArray tempArray=(JSONArray) o;  
	            for(Object ob:tempArray)  
	            {  
	                if(ob.getClass().equals(JSONObject.class))  
	                {  
	                    JSONObject tempJb=(JSONObject) ob;  
	                     for(String keyMap:m.keySet())  
	                     {  
	                        if(m.get(keyMap)==null)  
	                        {  
	                            if(tempJb.containsKey(keyMap))  
	                            {  
	                                System.out.println("find out....2222.................");  
	                                // System.out.println(tempJb.get(keyCondition));  
	                                 result=tempJb.get(keyMap);  
	                                  m.put(keyMap, tempJb.get(keyMap));  
	                            }  
	                            else  
	                            {  
	                                test2(tempJb,m);  
	                            }  
	                        }  
	                        
	                     }  
	                   
	                }  
	                else if(ob.getClass().equals(JSONArray.class))  
	                {  
	                    test2(ob,m);  
	                }  
	            }  
	        }  
	    }  
}

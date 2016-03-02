package com.hyp.plugin.workday;

import com.fr.json.JSONObject;
import com.fr.script.AbstractFunction;

public class WorkDay extends AbstractFunction {

    @Override
    public Object run(Object[] args) {
    	if(args!=null){
    			return GetWorkDay.getIsWorkDay(args);
    	}
    	System.out.println(args[0]);
    	return JSONObject.create();
    }
//    public static void main (String[] str){
//    	WorkDay work = new WorkDay();
//    	JSONObject o = (JSONObject) work.run(new String[]{"20120111","20160302","20150212"});
//		System.out.println(o);
//    }
}

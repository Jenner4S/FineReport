package com.hyp.plugin.workday;

import com.fr.general.Inter;
import com.fr.json.JSONObject;
import com.fr.plugin.ExtraClassManager;
import com.fr.script.AbstractFunction;
import com.fr.stable.fun.FunctionHelper;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.stable.fun.impl.AbstractFunctionProcessor;

public class WorkDay extends AbstractFunction {
//	public static final FunctionProcessor ONEFUNCTION = new AbstractFunctionProcessor(){
//		@Override
//		public int getId(){
//			int id = FunctionHelper.generateFunctionID("com.hyp.plugin.workday");
//			return id;
//		}
//		@Override
//			public String toString(){
//			return Inter.getLocText("Plugin-WorkDay_workday");
//		}
//	};
	
    @Override
    public Object run(Object[] args) {
    	FunctionProcessor processor=ExtraClassManager.getInstance().getFunctionProcessor();
    	if(processor!=null){
    		processor.recordFunction(IsWorkDay.ONEFUNCTION);
    	}
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

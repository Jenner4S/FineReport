package com.plugin.workday;

import com.fr.plugin.ExtraClassManager;
import com.fr.script.AbstractFunction;
import com.fr.stable.fun.FunctionProcessor;
import com.plugin.workday.tool.WorkDayFunction;

public class WorkDay extends AbstractFunction {
    @Override
    public Object run(Object[] args) {
    	FunctionProcessor processor=ExtraClassManager.getInstance().getFunctionProcessor();
    	if(processor!=null){
    		processor.recordFunction(IsWorkDay.ONEFUNCTION);
    	}
    	
    	try {
			return WorkDayFunction.workDay(args);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
    }
}

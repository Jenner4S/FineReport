package com.hyp.plugin.workday;

import com.fr.general.Inter;
import com.fr.plugin.ExtraClassManager;
import com.fr.script.AbstractFunction;
import com.fr.stable.fun.FunctionHelper;
import com.fr.stable.fun.FunctionProcessor;
import com.fr.stable.fun.impl.AbstractFunctionProcessor;

public class CircleWorkDay  extends AbstractFunction {
	
		
    @Override
    public Object run(Object[] args) {
    	FunctionProcessor processor=ExtraClassManager.getInstance().getFunctionProcessor();
    	if(processor!=null){
    		processor.recordFunction(IsWorkDay.ONEFUNCTION);
    	}
    	
    	if(args!=null){
    			return GetWorkDay.circleWorkDay(args);
    	}
//    	System.out.println(args[0]);
    	return false;
    }
}

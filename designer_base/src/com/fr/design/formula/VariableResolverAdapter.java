package com.fr.design.formula;

import com.fr.base.ConfigManager;
import com.fr.base.Parameter;
import com.fr.design.DesignModelAdapter;
import com.fr.base.ConfigManagerProvider;
import com.fr.script.ScriptConstants;
import com.fr.stable.Constants;
import com.fr.stable.script.CalculatorProvider;

import java.util.ArrayList;
import java.util.List;

public abstract class VariableResolverAdapter implements VariableResolver {
	private static final int TABLE_DATA_PARA = 0;
	private static final int REPORT_DATA_PARA = 1;

    /**
     * 获取模板内置的一些参数
     *
     * @return 内置参数
     */
	public String[] resolveCurReportVariables() {
		return new String[] { ScriptConstants.SUMMARY_TAG + "page_number",
				ScriptConstants.SUMMARY_TAG + "totalPage_number",
				// 下面是权限相关的参数
				ScriptConstants.DETAIL_TAG + Constants.P.PRIVILEGE_USERNAME, ScriptConstants.DETAIL_TAG + Constants.P.PRIVILEGE_AUTHORITY,
				ScriptConstants.DETAIL_TAG + Constants.P.PRIVILEGE_DEPARTMETN_AND_POST,
				// 空值参数
				"NULL", "NOFILTER",
				// request变量
				CalculatorProvider.REPORT_NAME, CalculatorProvider.SERVLET_URL, CalculatorProvider.SERVER_SCHEMA, CalculatorProvider.SERVER_NAME,
				CalculatorProvider.SERVER_PORT, CalculatorProvider.SERVER_URL, CalculatorProvider.CONTEXT_PATH, CalculatorProvider.SESSION_ID
		};
	}
	
	private Parameter[] getCurrentModeParameters(int type) {
		Parameter[] parameters = null;
		if(DesignModelAdapter.getCurrentModelAdapter() == null) {
			parameters = new Parameter[0];
		} else {
			if(type == TABLE_DATA_PARA) {
				parameters = DesignModelAdapter.getCurrentModelAdapter().getTableDataParameters();
			} else if(type == REPORT_DATA_PARA){
				parameters = DesignModelAdapter.getCurrentModelAdapter().getReportParameters();
			}
		}
		return parameters;
	}

    /**
     * 获取数据集参数
     *
     * @return 所有参数
     */
	public String[] resolveTableDataParameterVariables() {
		Parameter[] parameters = getCurrentModeParameters(TABLE_DATA_PARA);
		String[] parameterNames = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterNames[i] = ScriptConstants.DETAIL_TAG + parameters[i].getName();
		}
		return parameterNames;
	}

    /**
     * 获取模板参数
     *
     * @return 所有参数
     */
	public String[] resolveReportParameterVariables() {
		Parameter[] parameters = getCurrentModeParameters(REPORT_DATA_PARA);
		String[] parameterNames = new String[parameters.length];
		for (int i = 0; i < parameters.length; i++) {
			parameterNames[i] = ScriptConstants.DETAIL_TAG + parameters[i].getName();
		}
		return parameterNames;
	}

	/**
	 * 获取全局参数
     *
     * @return 所有参数
	 */
	public String[] resolveGlobalParameterVariables() {
		// 加上全局的参数
        ConfigManagerProvider reportServerManager = ConfigManager.getProviderInstance();
		Parameter[] globalParameters = reportServerManager.getGlobal_Parameters();

		List<String> variablesList = new ArrayList<String>();
		for (int i = 0; i < (globalParameters == null ? 0 : globalParameters.length); i++) {
			variablesList.add(ScriptConstants.DETAIL_TAG + (globalParameters[i]).getName());
		}

		return variablesList.toArray(new String[variablesList.size()]);
	}
}
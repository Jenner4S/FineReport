package com.fr.design.actions.core;

import java.awt.event.KeyEvent;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.KeyStroke;

import com.fr.base.FRContext;
import com.fr.base.Utils;
import com.fr.design.actions.UpdateAction;
import com.fr.design.file.HistoryTemplateListPane;
import com.fr.design.mainframe.JTemplate;
import com.fr.design.selection.QuickEditor;

/**
 * 一些ActionUtils
 */
public class ActionUtils {
	private static Class<UpdateAction>[] actionClasses;
	private static Class<UpdateAction>[] floatActionClasses;

	private ActionUtils() {
	}
	
	private static Map<Class, QuickEditor> floatEditor = new HashMap<Class, QuickEditor>();
	
	private static Class chartCollectionClass = null;
	
	private static Map<Class, QuickEditor> cellEditor = new HashMap<Class, QuickEditor>();
	
	private static UpdateAction chartPreStyleAction = null;

    /**
     * 待说明
     * @param clzz     待说明
     * @param editor   待说明
     */
	public static void registerCellEditor(Class clzz, QuickEditor editor) {
		cellEditor.put(clzz, editor);
	}

    /**
     * 待说明
     * @param clzz     待说明
     * @param editor    待说明
     */
	public static void registerFloatEditor(Class clzz, QuickEditor editor) {
		floatEditor.put(clzz, editor);
	}
	
	/**
	 * 注册图表的 预定义样式. 
	 * @param action 注册的图表预定义样式action
	 */
	public static void registerChartPreStyleAction(UpdateAction action) {
		chartPreStyleAction = action;
	}
	
	/**
	 * 返回 图表预定义样式Action
	 */
	public static UpdateAction getChartPreStyleAction() {
		return chartPreStyleAction;
	}

    /**
     * kunsnat: 图表注册 悬浮元素编辑器 , 因为ChartCollecion和ChartQuickEditor一个在Chart,一个在Designer, 所以分开注册.
     * @param clzz  待说明
     */
	public static void registerChartCollection(Class clzz) {
		chartCollectionClass = clzz;
	}

	public static Class getChartCollectionClass() {
		return chartCollectionClass;
	}
	
	/**
	 * kunsnat: 图表注册 悬浮元素编辑器 , 因为ChartCollecion和ChartQuickEditor一个在Chart,一个在Designer, 所以分开注册.
     * @param editor  待说明
	 */
	public static void registerChartFloatEditorInEditor(QuickEditor editor) {
		if(chartCollectionClass != null) {
			floatEditor.put(chartCollectionClass, editor);
		}
	}

    /**
     * kunsnat: 图表注册 悬浮元素编辑器 , 因为ChartCollecion和ChartQuickEditor一个在Chart,一个在Designer, 所以分开注册.
     * @param editor  待说明
     */
	public static void registerChartCellEditorInEditor(QuickEditor editor) {
		if(chartCollectionClass != null) {
			cellEditor.put(chartCollectionClass, editor);
		}
	}
	
	/**
	 * 返回 悬浮元素选中的Editor
	 */
	public static QuickEditor getFloatEditor(Class clazz) {
		return floatEditor.get(clazz);
	}
	
	public static QuickEditor getCellEditor(Class clazz) {
		return cellEditor.get(clazz);
	}

    /**
     * peter:从Action来产生ToolTipText.
     * @param action   动作
     * @return    字符
     */
	public static String createButtonToolTipText(Action action) {
		StringBuffer buttonToolTipTextBuf = new StringBuffer();

		//peter:把中文后面的(U),alt 快捷键的括号去掉,这个方法是临时的做法.
		String actionName = (String) action.getValue(Action.NAME);
		if (actionName.lastIndexOf("(") != -1) {
			buttonToolTipTextBuf.append(actionName.substring(0, actionName.lastIndexOf("(")));
		} else {
			buttonToolTipTextBuf.append(actionName);
		}

		//peter:产生快捷键的ToolTip.
		KeyStroke keyStroke = (KeyStroke) action.getValue(Action.ACCELERATOR_KEY);
		if (keyStroke != null) {
			buttonToolTipTextBuf.append(" (");
			buttonToolTipTextBuf.append(KeyEvent.getKeyModifiersText(keyStroke.getModifiers()));
			buttonToolTipTextBuf.append('+');
			buttonToolTipTextBuf.append(KeyEvent.getKeyText(keyStroke.getKeyCode()));
			buttonToolTipTextBuf.append(')');
		}

		return Utils.objectToString(buttonToolTipTextBuf);
	}

	/**
	 * 纪录插入元素的种类
	 *
	 * @param cls 类型数组
	 */
	public static void registerCellInsertActionClass(Class<UpdateAction>[] cls) {
		actionClasses = cls;
	}

	/**
	 * 生成单元格插入相关的Action
	 *
	 * @param cls 构造函数参数类型
	 * @param obj 构造函数参数值
	 * @return 相关Action组成的一个数组
	 */
	public static UpdateAction[] createCellInsertAction(Class cls, Object obj) {
        int length = 0;
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        //表单中报表块编辑屏蔽掉   插入子报表
        length = jTemplate.isJWorkBook()? actionClasses.length : actionClasses.length - 1;
		UpdateAction[] actions = new UpdateAction[length];
		for (int i = 0; i < length; i++) {
			try {
				Constructor<UpdateAction> c = actionClasses[i].getConstructor(cls);
				actions[i] = c.newInstance(obj);
			} catch (Exception e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
		}
		return actions;
	}

	/**
	 * 登记悬浮元素插入类型
	 *
	 * @param cls 插入类型数组
	 */
	public static void registerFloatInsertActionClass(Class<UpdateAction>[] cls) {
		floatActionClasses = cls;
	}

	/**
	 * 生成悬浮元素插入相关的Action
	 *
	 * @param cls 构造函数参数类型
	 * @param obj 构造函数参数值
	 * @return 相关Action组成的一个数组
	 */
	public static UpdateAction[] createFloatInsertAction(Class cls, Object obj) {
		UpdateAction[] actions = new UpdateAction[floatActionClasses.length];
		for (int i = 0; i < floatActionClasses.length; i++) {
			try {
				Constructor<UpdateAction> c = floatActionClasses[i].getConstructor(cls);
				actions[i] = c.newInstance(obj);
			} catch (Exception e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
		}
		return actions;
	}
}
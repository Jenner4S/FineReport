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
 * һЩActionUtils
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
     * ��˵��
     * @param clzz     ��˵��
     * @param editor   ��˵��
     */
	public static void registerCellEditor(Class clzz, QuickEditor editor) {
		cellEditor.put(clzz, editor);
	}

    /**
     * ��˵��
     * @param clzz     ��˵��
     * @param editor    ��˵��
     */
	public static void registerFloatEditor(Class clzz, QuickEditor editor) {
		floatEditor.put(clzz, editor);
	}
	
	/**
	 * ע��ͼ��� Ԥ������ʽ. 
	 * @param action ע���ͼ��Ԥ������ʽaction
	 */
	public static void registerChartPreStyleAction(UpdateAction action) {
		chartPreStyleAction = action;
	}
	
	/**
	 * ���� ͼ��Ԥ������ʽAction
	 */
	public static UpdateAction getChartPreStyleAction() {
		return chartPreStyleAction;
	}

    /**
     * kunsnat: ͼ��ע�� ����Ԫ�ر༭�� , ��ΪChartCollecion��ChartQuickEditorһ����Chart,һ����Designer, ���Էֿ�ע��.
     * @param clzz  ��˵��
     */
	public static void registerChartCollection(Class clzz) {
		chartCollectionClass = clzz;
	}

	public static Class getChartCollectionClass() {
		return chartCollectionClass;
	}
	
	/**
	 * kunsnat: ͼ��ע�� ����Ԫ�ر༭�� , ��ΪChartCollecion��ChartQuickEditorһ����Chart,һ����Designer, ���Էֿ�ע��.
     * @param editor  ��˵��
	 */
	public static void registerChartFloatEditorInEditor(QuickEditor editor) {
		if(chartCollectionClass != null) {
			floatEditor.put(chartCollectionClass, editor);
		}
	}

    /**
     * kunsnat: ͼ��ע�� ����Ԫ�ر༭�� , ��ΪChartCollecion��ChartQuickEditorһ����Chart,һ����Designer, ���Էֿ�ע��.
     * @param editor  ��˵��
     */
	public static void registerChartCellEditorInEditor(QuickEditor editor) {
		if(chartCollectionClass != null) {
			cellEditor.put(chartCollectionClass, editor);
		}
	}
	
	/**
	 * ���� ����Ԫ��ѡ�е�Editor
	 */
	public static QuickEditor getFloatEditor(Class clazz) {
		return floatEditor.get(clazz);
	}
	
	public static QuickEditor getCellEditor(Class clazz) {
		return cellEditor.get(clazz);
	}

    /**
     * peter:��Action������ToolTipText.
     * @param action   ����
     * @return    �ַ�
     */
	public static String createButtonToolTipText(Action action) {
		StringBuffer buttonToolTipTextBuf = new StringBuffer();

		//peter:�����ĺ����(U),alt ��ݼ�������ȥ��,�����������ʱ������.
		String actionName = (String) action.getValue(Action.NAME);
		if (actionName.lastIndexOf("(") != -1) {
			buttonToolTipTextBuf.append(actionName.substring(0, actionName.lastIndexOf("(")));
		} else {
			buttonToolTipTextBuf.append(actionName);
		}

		//peter:������ݼ���ToolTip.
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
	 * ��¼����Ԫ�ص�����
	 *
	 * @param cls ��������
	 */
	public static void registerCellInsertActionClass(Class<UpdateAction>[] cls) {
		actionClasses = cls;
	}

	/**
	 * ���ɵ�Ԫ�������ص�Action
	 *
	 * @param cls ���캯����������
	 * @param obj ���캯������ֵ
	 * @return ���Action��ɵ�һ������
	 */
	public static UpdateAction[] createCellInsertAction(Class cls, Object obj) {
        int length = 0;
        JTemplate jTemplate = HistoryTemplateListPane.getInstance().getCurrentEditingTemplate();
        //���б����༭���ε�   �����ӱ���
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
	 * �Ǽ�����Ԫ�ز�������
	 *
	 * @param cls ������������
	 */
	public static void registerFloatInsertActionClass(Class<UpdateAction>[] cls) {
		floatActionClasses = cls;
	}

	/**
	 * ��������Ԫ�ز�����ص�Action
	 *
	 * @param cls ���캯����������
	 * @param obj ���캯������ֵ
	 * @return ���Action��ɵ�һ������
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

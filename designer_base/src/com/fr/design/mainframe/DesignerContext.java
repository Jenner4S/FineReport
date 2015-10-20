/*
 * Copyright(c) 2001-2010, FineReport Inc, All Rights Reserved.
 */
package com.fr.design.mainframe;

import com.fr.base.Style;
import com.fr.design.designer.TargetComponent;
import com.fr.design.dialog.BasicDialog;
import com.fr.general.FRLogger;
import com.fr.stable.StableUtils;

import javax.swing.*;
import java.awt.datatransfer.Clipboard;
import java.lang.reflect.Method;
import java.util.Hashtable;

public class DesignerContext {

	//��ʽˢ������״̬
	public static final int FORMAT_STATE_NULL = 0;
	public static final int FORMAT_STATE_ONCE = 1;
	public static final int FORMAT_STATE_MORE = 2;

	private static Clipboard clipboard = null; //��ǰ�ļ�����.
	private static int formatState = FORMAT_STATE_NULL;
	private static Style[][] referencedStyle = null;
	private static TargetComponent referencedElementCasePane;
	private static int referencedIndex = 0;
    private static ThreadLocal<BasicDialog> reportWriteThread = new ThreadLocal<BasicDialog>();

	public DesignerContext() {

	}

	// to hold some env properties values.
	private static Hashtable<String, DesignerFrame> prop = new Hashtable<String, DesignerFrame>();

	private static Hashtable<String, DesignerBean> beans = new Hashtable<String, DesignerBean>();
	;

	/**
	 * Return the main design frame from context
	 */
	public static DesignerFrame getDesignerFrame() {
		return prop.get("DesignerFrame");
	}

	/**
	 * Set the main design frame to context.
	 */
	public static void setDesignerFrame(DesignerFrame designerFrame) {
		prop.put("DesignerFrame", designerFrame);
	}

	public static DesignerBean getDesignerBean(String name) {
		return beans.get(name) == null ? DesignerBean.NULL : beans.get(name);
	}

	public static void setDesignerBean(String name, DesignerBean bean) {
		beans.put(name, bean);
	}

	/**
	 * Gets the Clipboard.
	 */
	public static Clipboard getClipboard(JComponent comp) {
		if (DesignerContext.clipboard == null) {
			try {
				Action transferAction = TransferHandler.getCutAction();
				Method clipMethod = StableUtils.getDeclaredMethod(transferAction.getClass(), "getClipboard", new Class[]{JComponent.class});
				clipMethod.setAccessible(true);

				return (Clipboard) clipMethod.invoke(transferAction, new Object[]{comp});
			} catch (Exception securityException) {
				FRLogger.getLogger().error(securityException.getMessage(), securityException);
				//�÷�����ƣ����TransferHandler��getClipboard
				//�������Ա�֤��TextFieldֱ�ӵ�copy paste
				try {
					//�ؼ���Clipboard.
					DesignerContext.clipboard = comp.getToolkit().getSystemClipboard();
				} catch (Exception exp) {
					FRLogger.getLogger().error(exp.getMessage(), exp);
					DesignerContext.clipboard = new Clipboard("FR");
				}
			}
		}

		return DesignerContext.clipboard;
	}


	public static void setFormatState(int setformatState) {
		formatState = setformatState;
	}

	public static int getFormatState() {
		return formatState;
	}


	public static void setReferencedStyle(Style[][] styles) {
		referencedStyle = styles;
	}

	public static Style[][] getReferencedStyle() {
		return referencedStyle;
	}

	public static void setReferencedElementCasePane(TargetComponent t) {
		referencedElementCasePane = t;
	}

	public static TargetComponent getReferencedElementCasePane() {
		return referencedElementCasePane;
	}

	public static void setReferencedIndex(int index) {
		referencedIndex = index;
	}

	public static int getReferencedIndex() {
		return referencedIndex;
	}

    /**
     * �õ���ǰʵ��
     * @return ʵ����һ��һ��ֻ�ܴ�һ��������������
     */
    public static BasicDialog getReportWritePane(){
        return reportWriteThread.get();
    }

    /**
     * ��¼��ǰ�����������壨�����ʵ����
     */
    public static void setReportWritePane(BasicDialog dlg){
        reportWriteThread.set(dlg);
    }
}

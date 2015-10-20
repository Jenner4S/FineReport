package com.fr.design.selection;

/**
 * 
 * @author zhou
 * @since 2012-7-26����10:20:32
 */
public interface Selectedable<S extends SelectableElement> {

	public S getSelection();

	public void setSelection(S selectElement);

	/**
	 * Adds a <code>ChangeListener</code> to the listener list.
	 */
	public void addSelectionChangeListener(SelectionListener selectionListener);

	/**
	 * removes a <code>ChangeListener</code> from the listener list.
	 */
	public void removeSelectionChangeListener(SelectionListener selectionListener);

	// august:����Ͳ�Ҫ��fireSelectionChangeListener�����ˡ���Ϊ�������һ��Ҫ�����˽�еģ���Ȼ�ⲿ�漴�ĵ��ã�
}

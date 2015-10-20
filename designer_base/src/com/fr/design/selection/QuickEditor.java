package com.fr.design.selection;

import javax.swing.JComponent;

import com.fr.design.designer.TargetComponent;

/**
 * ���ٱ༭����
 * 
 * @author zhou
 * @since 2012-7-12����2:48:20
 */
@SuppressWarnings("rawtypes")
public abstract class QuickEditor<T extends TargetComponent> extends JComponent {
	private static final long serialVersionUID = 5434472104640676832L;

	protected T tc;

	protected boolean isEditing = false;

	public QuickEditor() {

	}

	public void populate(T tc) {
		isEditing = false;
		this.tc = tc;
		refresh();
		isEditing = true;
	}

	/**
	 * ��������һ��Ҫ�����
	 */
	protected void fireTargetModified() {
		if(!isEditing) {
			return;
		}
		tc.fireTargetModified();
	}

	protected abstract void refresh();

	public static QuickEditor DEFAULT_EDITOR = new QuickEditor() {

		@Override
		protected void refresh() {

		}

	};

}

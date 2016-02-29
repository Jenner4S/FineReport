package com.fr.design.selection;

import javax.swing.JComponent;

import com.fr.design.designer.TargetComponent;

/**
 * 快速编辑区域
 * 
 * @author zhou
 * @since 2012-7-12下午2:48:20
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
	 * 触发保存一定要用这个
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
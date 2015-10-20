package com.fr.design.mainframe;


import com.fr.base.BaseUtils;
import com.fr.design.roleAuthority.RolesAlreadyEditedPane;
import com.fr.general.Inter;

import javax.swing.*;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

/*
 * alex: UndoManager�б����Edit
 * richer: �����Ǳ������Ǳ�������Ҫ�õ����࣬���Էŵ�com.fr.design��
 */
public class UndoStateEdit extends AbstractUndoableEdit {
    /**
     *
     */
    private static final long serialVersionUID = 3608945320435502119L;
    private BaseUndoState<?> old;
    private BaseUndoState<?> cur;

    public UndoStateEdit(BaseUndoState<?> old, BaseUndoState<?> cur) {
        this.old = old;
        this.cur = cur;
        if (this.old == null || this.cur == null) {
            return;
        }
        if (old.getAuthorityType() == BaseUndoState.NORMAL_STATE && cur.getAuthorityType() == BaseUndoState.AUTHORITY_STATE) {
            this.old.setAuthorityType(BaseUndoState.STATE_BEFORE_AUTHORITY);
        }
        if (old.getFormReportType() == BaseUndoState.NORMAL_STATE && cur.getFormReportType() == BaseUndoState.STATE_FORM_REPORT){
            this.cur.setFormReportType(BaseUndoState.STATE_BEFORE_FORM_REPORT);
        }
    }

    /**
     * ����
     * @throws CannotUndoException   ������Ϣ
     */
    public void undo() throws CannotUndoException {
        super.undo();
        if (cur.getAuthorityType() == BaseUndoState.AUTHORITY_STATE) {
            int returnVal = JOptionPane.showConfirmDialog(DesignerContext.getDesignerFrame(), Inter.getLocText("FR-Designer-Undo_All_Authority_Operations") + "?",
                    Inter.getLocText("FR-Designer_Undo"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (returnVal == JOptionPane.NO_OPTION) {
                return;
            }
        }
        if (this.old != null) {
            this.old.applyState();
        }
        if (BaseUtils.isAuthorityEditing()) {
            RolesAlreadyEditedPane.getInstance().refreshDockingView();
        }
    }

    /**
     * ����
     * @throws CannotUndoException   ������Ϣ
     */
    public void redo() throws CannotRedoException {
        super.redo();

        if (this.cur != null) {
            this.cur.applyState();
        }
        if (BaseUtils.isAuthorityEditing()) {
            RolesAlreadyEditedPane.getInstance().refreshDockingView();
        }
    }
}

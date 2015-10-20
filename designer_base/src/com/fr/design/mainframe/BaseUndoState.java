package com.fr.design.mainframe;

public abstract class BaseUndoState<T> {
    //�������������������༭״̬�µ�undostate�����ԣ�
    public static final int NORMAL_STATE = 0;
    //ȫ�߱༭��state
    public static final int AUTHORITY_STATE = 1;
    //Ȩ�ޱ༭״̬ǰ��һ��state
    public static final int STATE_BEFORE_AUTHORITY = 2;
    //�����༭��state
    public static final int STATE_FORM_REPORT = 3;
    //�����༭״̬ǰ��һ��state
    public static final int STATE_BEFORE_FORM_REPORT = 4;
    private T applyTarget;
    private int isAuthorityType = NORMAL_STATE;
    private int isFormReportType = NORMAL_STATE;

    public BaseUndoState(T t) {
        this.applyTarget = t;
    }

    public T getApplyTarget() {
        return this.applyTarget;
    }

    public void setAuthorityType(int isAuthoritytype) {
        this.isAuthorityType = isAuthoritytype;
    }

    public int getAuthorityType() {
        return isAuthorityType;
    }

    public void setFormReportType(int isFormReportType) {
        this.isFormReportType = isFormReportType;
    }

    public int getFormReportType() {
        return isFormReportType;
    }
    /**
     * Ӧ��״̬
     */
    public abstract void applyState();
}

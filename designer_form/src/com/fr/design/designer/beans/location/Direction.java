package com.fr.design.designer.beans.location;

import com.fr.design.mainframe.FormDesigner;

public interface Direction {
	
	/**
	 * ��ק���
	 * @param dx ˮƽ����λ��
	 * @param dy ��ֱ����λ��
	 * @param designer �����
	 */
	void drag(int dx, int dy, FormDesigner designer);

	/**
	 * ���������ʽ
	 * @param formEditor �� �����
	 */
    void updateCursor(FormDesigner formEditor);
    
	/**
	 * Direction��λ�ñ�ʾ��top = 1,bottom = 2��
	 */
    int getActual();

    /**
     * ��קǰ�ȱ���ԭʼλ�ã���ק���������ڱȽ�λ�Ƹ�ԭʼλ�ôӶ�ȷ����λ�ô�С
     * @param formEditor   �����
     */
    void backupBounds(FormDesigner formEditor);
    
    public static final int TOP = 1;
    public static final int BOTTOM = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;
    public static final int LEFT_TOP = 5;
    public static final int LEFT_BOTTOM = 6;
    public static final int RIGHT_TOP = 7;
    public static final int RIGHT_BOTTOM = 8;
    public static final int INNER = 0;
    public static final int OUTER = -1;
    
    public static final int[] ALL = new int[]{TOP, BOTTOM, LEFT, RIGHT, LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM, INNER};
    public static final int[] TOP_BOTTOM_LEFT_RIGHT= new int[]{TOP, BOTTOM, LEFT, RIGHT};
}

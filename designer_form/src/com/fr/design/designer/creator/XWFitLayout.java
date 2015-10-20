package com.fr.design.designer.creator;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ContainerEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fr.design.designer.beans.LayoutAdapter;
import com.fr.design.designer.beans.adapters.layout.FRFitLayoutAdapter;
import com.fr.design.designer.beans.location.Direction;
import com.fr.design.designer.creator.cardlayout.XWTabFitLayout;
import com.fr.design.form.layout.FRFitLayout;
import com.fr.design.mainframe.FormArea;
import com.fr.design.utils.gui.LayoutUtils;
import com.fr.form.ui.PaddingMargin;
import com.fr.form.ui.Widget;
import com.fr.form.ui.container.WAbsoluteLayout.BoundsWidget;
import com.fr.form.ui.container.WFitLayout;
import com.fr.form.ui.container.WLayout;
import com.fr.general.FRScreen;
import com.fr.stable.ArrayUtils;

/**
 * @author jim
 * @date 2014-6-23
 */
public class XWFitLayout extends XLayoutContainer {
	
	private static final long serialVersionUID = 8112908607102660176L;
	
	//������Ļ�ֱ��ʲ�ͬ�������ϵ�������С���ܲ���Ĭ�ϵ�100%����ʱ�������ʱ������Ĵ�С����100%ʱ�ļ���
	protected double containerPercent = 1.0;
	// ������С��ʱ�򣬿�����С��ߣ��������Ҳ��ײ�߿�Ŀؼ���С��ﵽ��С���ߣ���ʱ������С΢����
	private int needAddWidth = 0;
	private int needAddHeight = 0;
	private int minWidth = WLayout.MIN_WIDTH;
	private int minHeight = WLayout.MIN_HEIGHT;
	private int backupGap = 0;
	protected boolean hasCalGap = false;


	public XWFitLayout(){
		this(new WFitLayout(), new Dimension());
	}
	
	public XWFitLayout(WFitLayout widget, Dimension initSize) {
		super(widget, initSize);
		
		initPercent();
	}
	
	//������Ļ��С��ȷ����ʾ�İٷֱ�, 1440*900Ĭ��100%, 1366*768����90%
	private void initPercent(){
    	Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension scrnsize = toolkit.getScreenSize();
        double screenValue = FRScreen.getByDimension(scrnsize).getValue();
        if(screenValue != FormArea.DEFAULT_SLIDER){
        	this.setContainerPercent(screenValue / FormArea.DEFAULT_SLIDER);
        }
	}
	
	@Override
	public LayoutAdapter getLayoutAdapter() {
		return new FRFitLayoutAdapter(this);
	}

	@Override
	protected void initLayoutManager() {
		this.setLayout(new FRFitLayout());
	}

    @Override
	protected String getIconName() {
		return "layout_absolute.png";
	}
    
    /**
     * �������Ҳ�ؼ���΢�����
     * @return ΢�����
     */
	public int getNeedAddWidth() {
		return needAddWidth;
	}

	public void setNeedAddWidth(int needAddWidth) {
		this.needAddWidth = needAddWidth;
	}

    /**
     * �������Ҳ�ؼ���΢���߶�
     * @return ΢�����
     */
	public int getNeedAddHeight() {
		return needAddHeight;
	}

	public void setNeedAddHeight(int needAddHeight) {
		this.needAddHeight = needAddHeight;
	}
    
	/**
	 * ���������backupBound
	 *  �϶�����ı�������С���ı���ǽ�����ʾ��С������bound���ٴ����������߿��õ�
	 */
	private void updateCreatorsBackupBound() {
		for (int i=0,size=this.getComponentCount(); i<size; i++) {
			Component comp = this.getComponent(i);
			XCreator creator = (XCreator) comp;
			creator.setBackupBound(comp.getBounds());
		}
	}
	
	/**
	 * ֱ���϶������ı��������ش�Сʱ�����ÿ��ǿؼ�����С�߶ȿ�ȣ��ڲ����ȫ��һ����С�Ŵ�
	 * ֻ�ǽ�����ʾ��С�ı䣬���ı��Ӧ��BoundsWidget��С
	 * @param percent ���ŵİٷ�ֵ
	 */
	public void adjustCreatorsWhileSlide(double percent) {
		int count = this.getComponentCount();
		if (count == 0) {
			Dimension size = new Dimension(this.getSize());
			size.width += size.width*percent;
			size.height += size.height*percent;
			this.setSize(size);
			return;
		}
		// ��ʼ��ʱ��δ�Ӽ��
		if (hasCalGap) {
			moveContainerMargin();
			moveCompInterval(backupGap);
			LayoutUtils.layoutContainer(this);
		}
		int containerW = 0;
		int containerH = 0;
		int[] hors = getHors(false);
		int[] veris = getVeris(false);
		PaddingMargin margin = toData().getMargin();
		for (int i=0; i<count; i++) {
			XCreator creator = getXCreator(i);
			// �ٷֱȺ�updateBoundsWidgetʱ���������С
			// ������ǿ�ʱ�����������С�� �������ٴδ򿪱������ᰴ��Ļ�ٷֱȵ�������ʱ���뿼���ڱ߾�
			Rectangle rec = modifyCreatorPoint(creator.getBounds(), percent, hors, veris);
			if (rec.x == margin.getLeft()) {
				containerH += rec.height;
			}
			if (rec.y ==  margin.getTop()) {
				containerW += rec.width;
			}
			creator.setBounds(rec);
			creator.updateChildBound(getActualMinHeight());
		}
		// �����ڲ�����Ŵ���С�󣬶��ǳ��԰ٷֱȺ�ȡ�������ܻ������϶���˴���������
		this.setSize(containerW + margin.getLeft() + margin.getRight(), containerH + margin.getTop() + margin.getBottom());
		updateCreatorsBackupBound();
		// ���Ҳ����ʾ��С��������
		if (!hasCalGap) {
			moveContainerMargin();
			addCompInterval(getAcualInterval());
		}
		LayoutUtils.layoutContainer(this);
	}
	
	/**
	 * �����ؼ���point��size,�����϶�������ֿ�϶
	 */
	private Rectangle modifyCreatorPoint(Rectangle rec, double percent, int[] hors, int[] veris) {
		int xIndex = 0, yIndex = 0;
		PaddingMargin margin = toData().getMargin();
		Rectangle bound = new Rectangle(rec);
		if (rec.x > margin.getLeft()) {
			for (int i=1, len=hors.length; i<len; i++) {
				rec.x += (hors[i] - hors[i-1]) *percent;
				if (bound.x == hors[i]) {
					xIndex = i;
					break ;
				}
			}
		}
		for (int i=xIndex,len=hors.length; i<len-1; i++) {
			rec.width +=  (hors[i+1]-hors[i])*percent;
			if (hors[i+1]-hors[xIndex] == bound.width) {
				break;
			}
		}
		if (rec.y >  margin.getTop()	) {
			for (int j=1, num=veris.length; j<num; j++) {
				rec.y += (veris[j] - veris[j-1]) *percent;
				if (bound.y == veris[j]) {
					yIndex = j;
					break ;
				}
			}
		}
		for (int j=yIndex,num=veris.length; j<num-1; j++) {
			rec.height +=  (veris[j+1]-veris[j])*percent;
			if (veris[j+1]-veris[yIndex] == bound.height) {
				break;
			}
		}
		return rec;
	}
	/**
	 * ��ȡ�ڲ�����������ֵ
	 * @return int[] ����������
	 */
	
	public int[] getHors(){
		return getHors(false);
	}
	
	/**
	 * ��ȡ�ڲ����������ֵ
	 * @return int[] ����������
	 * 
	 */
	public int[] getVeris(){
		return getVeris(false);
	}
	
	/**
	 * ��ȡ�ڲ�����������ֵ
	 * @param isActualSize ʵ�ʴ�С
	 * @return int[] ����������
	 */
	public int[] getHors(boolean isActualSize) {
		double perc = isActualSize ? containerPercent : 1.0;
		List<Integer> posX = new ArrayList<Integer>();
		// ����ʵ�ʴ�Сʱ,�����С�Ѿ�ȥ���ڱ߾࣬�˴�Ҳ�ж���
		PaddingMargin margin = isActualSize ? new PaddingMargin(0,0,0,0) : toData().getMargin();
		posX.add(margin.getLeft());
		int width = this.getWidth() - margin.getLeft() - margin.getRight();
		int containW = (int) (width / perc);
		posX.add(containW);
		for (int i=0, len=this.getComponentCount(); i < len; i++) {
			int x = this.getComponent(i).getX();
			int finalX = (int) (x / perc);
			if (!posX.contains(finalX)) {
				posX.add(finalX);
			}
		}
		Collections.sort(posX);
		return ArrayUtils.toPrimitive(posX.toArray(new Integer[]{posX.size()}));
	}
	
	/**
	 * ��ȡ�ڲ����������ֵ
	 * @param isActualSize ʵ�ʴ�С
	 * @return int[] ����������
	 * 
	 */
	public int[] getVeris(boolean isActualSize) {
		double perc = isActualSize ? containerPercent : 1.0;
		List<Integer> posY = new ArrayList<Integer>();
		// ����ʵ�ʴ�Сʱ,�����С�Ѿ�ȥ���ڱ߾࣬�˴�Ҳ�ж���
		PaddingMargin margin = isActualSize ? new PaddingMargin(0,0,0,0) : toData().getMargin();
		posY.add(margin.getTop());
		int height = this.getHeight() - margin.getTop() - margin.getBottom();
		int containH = (int) (height / perc);
		posY.add(containH);
		for (int i=0, len=this.getComponentCount(); i < len; i++) {
			int y = this.getComponent(i).getY();
			int finalY = (int) (y / perc);
			if (!posY.contains(finalY)) {
				posY.add(finalY);
			}
		}
		Collections.sort(posY);
		return ArrayUtils.toPrimitive(posY.toArray(new Integer[]{posY.size()}));
	}
	
	/**
	 * �Ƿ�����С
	 * @param percent �ٷֱ�
	 * @return ���򷵻�true
	 */
	public boolean canReduce(double percent) {
		boolean canReduceSize = true;
		if (percent < 0 && hasCalGap) {
			// ��Сʱ���Ǽ���Ĵ����Ƿ�����С
			canReduceSize = canReduceSize(percent);
		}
		return canReduceSize;
	}
	
	/**
	 * ���հٷֱ������ڲ�������
	 * 
	 * @param percent ��ȱ仯�İٷֱ�
	 */
	public void adjustCreatorsWidth(double percent) {
		if (this.getComponentCount()==0) {
			// ��ʼ��û������ؼ�ʱ��ʵ�ʿ����Ȼ����
			this.toData().setContainerWidth(this.getWidth());
			return;
		}
		updateWidgetBackupBounds();
		int gap = toData().getCompInterval();
		if (gap >0 && hasCalGap) {
			moveCompInterval(getAcualInterval());
		}
		layoutWidthResize(percent); 
		if (percent < 0 && needAddWidth > 0) {
			this.setSize(this.getWidth()+needAddWidth, this.getHeight());
			modifyEdgemostCreator(true);
		}
		addCompInterval(getAcualInterval());
		this.toData().setContainerWidth(this.getWidth());
		updateWidgetBackupBounds();
		LayoutUtils.layoutContainer(this);
	}
	
	// �ֶ��޸Ŀ��ʱ��ȫ���õ�ʵ�ʴ�С���㣬������Сֵ���ðٷֱȼ�����
	protected void layoutWidthResize(double percent) {
		int[] hroValues= toData().getHorComps();
		int num=hroValues.length;
		int x=0;
		int dw = 0;
		int nextX = 0;
		for (int i=0; i<num-1; i++) {
			x = hroValues[i];
			nextX = hroValues[i+1];
			dw = (int) ((nextX-x)*percent);
			//����Ӧ������ؼ�����С���Ϊ36
			if (nextX-x < MIN_WIDTH-dw) {
				dw = MIN_WIDTH +x - nextX;
			} 
			caculateWidth(x, dw);
		}
	}
	
	/**
	 * xλ�õ������Ȱ�dw���е���
	 */
	private void caculateWidth(int x, int dw) {
		List<Component> comps = getCompsAtX(x);
		if (comps.isEmpty()) {
			return;
		}
		for (int i=0, size=comps.size(); i<size; i++) {
			XCreator creator = (XCreator) comps.get(i);
			BoundsWidget widget = toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBounds();
			Rectangle backRec = widget.getBackupBounds();
			if (backRec.x<x) {
				if (notHasRightCreator(backRec)) {
					continue;
				}
				creator.setSize(rec.width+dw, rec.height);
				toData().setBounds(creator.toData(), creator.getBounds());
				continue;
			}
			calculateCreatorWidth(creator, rec, dw, x);
		}
	}
	
	private void calculateCreatorWidth(XCreator creator, Rectangle rec, int dw, int x) {
		if (x == 0) {
			int width = notHasRightCreator(rec) ? this.getWidth() : rec.width+dw;
			creator.setBounds(0, rec.y, width, rec.height);
			creator.recalculateChildWidth(width);
		} else {
			XCreator leftCreator = getCreatorAt(rec.x-1, rec.y);
			int posX = getPosX(leftCreator);
			int width = notHasRightCreator(rec) ? this.getWidth()-posX : rec.width+dw;
			int tempWidth = width;
			if(width < MIN_WIDTH){
				tempWidth = MIN_WIDTH;
			}
			creator.setBounds(posX, rec.y, tempWidth, rec.height);
			// ��С��С��minwidthֻ�����Ҳ�ʱ��ǰ��ȫ������С����Сֵ
			if (width<MIN_WIDTH) {
				needAddWidth = Math.max(needAddWidth, MIN_WIDTH-width);
			}
		}
		toData().setBounds(creator.toData(), creator.getBounds());
	}
	
	/**
	 * �Ƿ��ڲ������Ҳ�
	 */
	private boolean notHasRightCreator(Rectangle rec) {
		if ( rec.x+rec.width==this.getBackupBound().width) {
			return true;
		}
		return false;
	}
	
	/**
	 * �Ƿ��ڲ������²�
	 */
	private boolean notHasBottomCreator(Rectangle rec) {
		if ( rec.y+rec.height == this.getBackupBound().height) {
			return true;
		}
		return false;
	}
	
	/**
	 * �������Ҳ���²��пؼ�����Сʱ�ﵽ��С��ߣ���΢����
	 */
	protected void modifyEdgemostCreator(boolean isHor) {
		for (int i=0, size=this.getComponentCount(); i<size; i++) {
			XCreator creator = (XCreator) this.getComponent(i);
			BoundsWidget widget = toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBackupBounds();
			if (isHor && notHasRightCreator(rec)) {
				creator.setSize(creator.getWidth()+needAddWidth, creator.getHeight());
			} else if (!isHor && notHasBottomCreator(rec)) {
				creator.setSize(creator.getWidth(), creator.getHeight()+needAddHeight);
			}
		}
	}
	
	/**
	 * ���������߶��ֶ��޸�ʱ��
	 * ͬʱ���������ڵ������,��Сʱ��Ҫ�����е�����߶Ȳ�������С�߶�
	 * @param percent �߶ȱ仯�İٷֱ�
	 */
	public void adjustCreatorsHeight(double percent) {
		if (this.getComponentCount()==0) {
			//�����߶Ⱥ�wlayout�Ǳ߼�¼��
			this.toData().setContainerHeight(this.getHeight());
			return;
		}
		updateWidgetBackupBounds();
		int gap = toData().getCompInterval();
		if (gap >0 && hasCalGap) {
			moveCompInterval(getAcualInterval());
		}
		layoutHeightResize(percent);
		if (percent < 0 && needAddHeight > 0) {
			this.setSize(this.getWidth(), this.getHeight()+needAddHeight);
			modifyEdgemostCreator(false);
		}
		addCompInterval(getAcualInterval());
		this.toData().setContainerHeight(this.getHeight());
		updateWidgetBackupBounds();
		LayoutUtils.layoutContainer(this);
	}
	
	protected void layoutHeightResize(double percent) {
		int[] vertiValues= toData().getVertiComps();
		int num=vertiValues.length;
		int y=0;
		int dh = 0;
		int nextY = 0;
		for (int i=0; i<num-1; i++) {
			y = vertiValues[i];
			nextY = vertiValues[i+1];
			dh = (int) ((nextY-y)*percent);
			if (nextY-y < MIN_HEIGHT-dh) {
				dh = MIN_HEIGHT + y - nextY;
			} 

			calculateHeight(y, dh);
		}
	}
	
	/**
	 * yλ�õ������dh���е���
	 */
	private void calculateHeight(int y, int dh) {
		List<Component> comps = getCompsAtY(y);
		if (comps.isEmpty()) {
			return;
		}
		for (int i=0, size=comps.size(); i<size; i++) {
			XCreator creator = (XCreator) comps.get(i);
			BoundsWidget widget = toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBounds();
			Rectangle backRec = widget.getBackupBounds();
			if (backRec.y < y) {
				if (notHasBottomCreator(backRec)) {
					continue;
				}
				creator.setSize(rec.width, rec.height+dh);
				toData().setBounds(creator.toData(), creator.getBounds());
				continue;
			} 
			calculateCreatorHeight(creator, rec, dh, y);
		}
	}
	
	private void calculateCreatorHeight(XCreator creator, Rectangle rec, int dh, int y) {
		if (y==0) {
			int height = notHasBottomCreator(rec) ? this.getHeight() : rec.height+dh;
			creator.setBounds(rec.x, 0, rec.width, height);
		} else {
			XCreator topCreator = getCreatorAt(rec.x, rec.y-1);
			int posY = getPosY(topCreator);
			int h = notHasBottomCreator(rec) ? this.getHeight()-posY : rec.height+dh;
			creator.setBounds(rec.x, posY, rec.width, h);
			if (h<MIN_HEIGHT) {
				// ��С��С��minheightֻ�����²�ʱ��ǰ��ȫ������С����Сֵ
				needAddHeight = Math.max(needAddHeight, MIN_HEIGHT-h);
			}
		}
		toData().setBounds(creator.toData(), creator.getBounds());
	}
	
	private List<Component> getCompsAtX(int x) {
		List<Component> comps = new ArrayList<Component>();
		int size = toData().getWidgetCount();
		for (int i=0; i<size; i++) {
			Component comp = this.getComponent(i);
			XCreator creator = (XCreator) comp;
			BoundsWidget widget = toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBackupBounds();
			//rec.xС��x���Ҳ����x
			boolean isLowX = rec.x<x && x<rec.x+rec.width;
			if (isLowX || rec.x==x) {
				comps.add(comp);
			}
		}
		return comps;
	}
	
	private List<Component> getCompsAtY(int y) {
		List<Component> comps = new ArrayList<Component>();
		for (int i=0,size=this.getComponentCount(); i<size; i++) {
			Component comp = this.getComponent(i);
			XCreator creator = (XCreator) comp;
			BoundsWidget widget = toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBackupBounds();
			boolean isLowY = rec.y<y && y<rec.y+rec.height;
			if (isLowY || rec.y==y) {
				comps.add(comp);
			}
		}
		return comps;
	}
	
	private int getPosX(XCreator creator) {
		if (creator == null) {
			return 0;
		} else {
			return creator.getX()+creator.getWidth();
		}
	}
	
	/**
	 * �����ϲ�������µ�λ��
	 */
	private int getPosY(XCreator creator) {
		if (creator == null) {
			return 0;
		} else {
			return creator.getY()+creator.getHeight();
		}
	}
	
	/**
	 * ���� x��y ������������
	 * 
	 * @param x ����x
	 * @param y ����y
	 * @return ָ����������
	 */
	public XCreator getCreatorAt(int x, int y) {
		for (int i=0,size=this.getComponentCount(); i<size; i++) {
			XCreator creator = (XCreator) this.getComponent(i);
			BoundsWidget widget = toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBackupBounds();
			boolean isCurrent = rec.x<=x && x<rec.x+rec.width && rec.y<=y && y<rec.y+rec.height;
			if (isCurrent) {
				return creator;
			}
		}
		return null;
	}
	
	/**
	 * ����boundsWidget��backupBound
	 */
	protected void updateWidgetBackupBounds() {
		for (int i=0, size=this.getComponentCount(); i<size; i++) {
			Component comp = this.getComponent(i);
			XCreator creator = (XCreator) comp;
			BoundsWidget widget = toData().getBoundsWidget(creator.toData());
			widget.setBackupBounds(widget.getBounds());
		}
	}
	
	
	/**
	 * �����ڲ��������С�߶�
	 * @param comps �������
	 * @return ��С�߶�
	 */
	public int getMinHeight(List<Component> comps) {
		//�����߶�����ʱ�������ڲ��������С�߶�
		if (comps.isEmpty()) {
    		return 0;
    	}
    	int minH =this.getWidth();
    	for (int i=0, size=comps.size(); i<size; i++) {
    		minH = minH>comps.get(i).getHeight() ? comps.get(i).getHeight() : minH;
    	}
    	return minH;
	}
	
	/**
	 * ��ʼ�������С
	 * @return Ĭ�ϴ�С
	 */
	@Override
	public Dimension initEditorSize() {
		return new Dimension(0, 0);
	}
	
	/**
	 * f����Ĭ�����name
	 * @return ������
	 */
	public String createDefaultName() {
		return "fit";
	}
	
	/**
	 * ����������Ӧ��wlayout
	 * @return ͬ��
	 */
	@Override
    public WFitLayout toData() {
        return (WFitLayout) data;
    }
	
	/**
	 * ��ǰ���zorderλ���滻�µĿؼ�
	 * @param widget �ؼ�
	 * @param  oldcreator �����
	 * @return ���
	 */
	@Override
	public XCreator replace(Widget widget, XCreator oldcreator) {
		int i = this.getComponentZOrder(oldcreator);
		if (i != -1) {
			this.toData().replace(new BoundsWidget(widget, oldcreator.getBounds()),
					new BoundsWidget(oldcreator.toData(), oldcreator.getBounds()));
			this.convert();
			return (XCreator) this.getComponent(i);
		}
		return null;
	}
	
	@Override
	public Dimension getMinimumSize() {
		return toData().getMinDesignSize();
	}
	
	/**
     * ��WLayoutת��ΪXLayoutContainer
     */
    public void convert() {
        isRefreshing = true;
        WFitLayout layout = this.toData();
        this.removeAll();
        for (int i=0, num=layout.getWidgetCount(); i<num ; i++) {
            BoundsWidget bw = (BoundsWidget)layout.getWidget(i);
            if (bw != null) {
            	Rectangle bounds = bw.getBounds();
            	bw.setBackupBounds(bounds);
                XWidgetCreator comp = (XWidgetCreator) XCreatorUtils.createXCreator(bw.getWidget());
                comp.setBounds(bounds);
                this.add(comp, bw.getWidget().getWidgetName(), true);
                comp.setBackupParent(this);
            }
        }
        isRefreshing = false;
    }
    
    /**
     * �������
     * @param e �����¼�
     */
    public void componentAdded(ContainerEvent e) {
    	if (isRefreshing) {
            return;
        }
    	LayoutUtils.layoutContainer(this);
    	WFitLayout layout = this.toData();
    	//����Ӧ���������ؼ��������ؼ�λ��Ҳ���
		XWidgetCreator creator = (XWidgetCreator) e.getChild();
		Rectangle rec = creator.getBounds();
		layout.addWidget(new BoundsWidget(creator.toData(), rec));
		creator.setBackupParent(this);
    }
    
    /**
     * ����������С����Ĭ�ϵ�ʱ������ؼ���BoundsWidget���ұ�����ֿ�϶
     */
    private Rectangle dealWidgetBound(Rectangle rec) {
    	if (containerPercent == 1.0) {
    		return rec;
    	}
    	rec.x = (int) (rec.x/containerPercent);
    	rec.y = (int) (rec.y/containerPercent);
    	rec.width = (int) (rec.width/containerPercent);
    	rec.height = (int) (rec.height/containerPercent);
    	return rec;
    }
    
    /**
     * ����ɾ����������ÿ�������BoundsWidget
     */
    public void updateBoundsWidget() {
    	WFitLayout layout = this.toData();
    	if (this.getComponentCount() == 0) {
    		// ɾ�����һ�����ʱ�Ͳ�����Ҫ������
    		return;
    	}
    	// ʲô����� �߾డ��ȥ��
    	moveContainerMargin();
    	moveCompInterval(getAcualInterval());
    	int[] hors = getHors(true);
    	int[] veris = getVeris(true);
    	int containerWidth = 0;
    	int containerHeight = 0;
    	for (int index=0, n=this.getComponentCount(); index<n; index++) {
    		XCreator creator = (XCreator) this.getComponent(index);
    		BoundsWidget wgt = layout.getBoundsWidget(creator.toData());
    		// �õ�ǰ����ʾ��С������������λ��
    		Rectangle wgtBound = dealWidgetBound(creator.getBounds());
    		Rectangle rec = recalculateWidgetBounds(wgtBound, hors, veris);
    		wgt.setBounds(rec);
    		creator.toData().updateChildBounds(rec);
    		if (rec.x == 0) {
    			containerHeight += rec.height;
    		}
    		if (rec.y == 0) {
    			containerWidth += rec.width;
    		}
    		// ��������ʱtab���֣���tab�����ڲ��������wigetҲҪ���£����򱣴�����´򿪴�С����
    		ArrayList<?> childrenList = creator.getTargetChildrenList();
    		if(!childrenList.isEmpty()){
    			for(int i=0; i<childrenList.size(); i++){
    				XWTabFitLayout tabLayout = (XWTabFitLayout) childrenList.get(i);
    				tabLayout.updateBoundsWidget();
    			}
    		}
    	}
    	layout.setContainerHeight(containerHeight);
    	layout.setContainerWidth(containerWidth);
    	addCompInterval(getAcualInterval());
    }
    
    private Rectangle recalculateWidgetBounds(Rectangle rec, int[] hors, int[] veris) {
    	int xIndex = 0, yIndex = 0;
		Rectangle bound = new Rectangle();
		if (rec.x > 0) {
			for (int i=1, len=hors.length; i<len; i++) {
				bound.x += (hors[i] - hors[i-1]);
				if (rec.x == hors[i]) {
					xIndex = i;
					break ;
				}
			}
		}
		for (int i=xIndex,len=hors.length; i<len-1; i++) {
			bound.width +=  (hors[i+1]-hors[i]);
			// ��һ��xֵ��ȥ��ǰxֵ���ܻ��ʵ�ʿ�ȴ�1
			if (hors[i+1]-hors[xIndex] >= rec.width) {
				break;
			}
		}
		if (rec.y > 0) {
			for (int j=1, num=veris.length; j<num; j++) {
				bound.y += (veris[j] - veris[j-1]);
				if (rec.y == veris[j]) {
					yIndex = j;
					break ;
				}
			}
		}
		for (int j=yIndex,num=veris.length; j<num-1; j++) {
			bound.height +=  (veris[j+1]-veris[j]) ;
			if (veris[j+1]-veris[yIndex] >= rec.height) {
				break;
			}
		}
		return bound;
    }
    
    /**
     * ���ɾ��
     * @param e �����¼�
     */
    public void componentRemoved(ContainerEvent e) {
        if (isRefreshing) {
            return;
        }
        WFitLayout wlayout = this.toData();
        XWidgetCreator xwc = ((XWidgetCreator) e.getChild());
        Widget wgt = xwc.toData();
        BoundsWidget bw = wlayout.getBoundsWidget(wgt);
        wlayout.removeWidget(bw);
        updateBoundsWidget();
    }
    
    /**
     *  ����ӵ�ʱ����Ҫ�ѿ�����ķ���ȷ����������д��add����
     *  @param comp ���
     *  @param constraints ����
     */
    public void add(Component comp, Object constraints) {
    	if (comp == null) {
    		return;
    	}
    	super.add(comp, constraints);
        XCreator creator = (XCreator) comp;
        dealDirections(creator, false);
    }
    
    private void add(Component comp, Object constraints, boolean isInit) {
    	super.add(comp, constraints);
        XCreator creator = (XCreator) comp;
        dealDirections(creator, isInit);
    }
    
    /**
     * ��������Ӧ���ֵ�directions
     * @param creator ���
     */
    private void dealDirections(XCreator xcreator, boolean isInit) {
    	if (xcreator == null) {
            return;
        }
    	// ���´�ģ��ʱ��������û��ʼ������С����Ϊ0
    	int containerWidth = isInit ? toData().getContainerWidth() : this.getWidth();
    	int containerHeight = isInit ? toData().getContainerHeight() : this.getHeight();
    	PaddingMargin margin = isInit ? new PaddingMargin(0,0,0,0) : toData().getMargin();
    	// �ٴδ�ʱ�ͳ�ʼ���ʱ��������û������ڱ߾��û����Ļ�ֱ��ʵ�����С
    	for (int i=0; i<this.getXCreatorCount(); i++) {
    		XCreator creator = this.getXCreator(i);
    		int x = creator.getX();
    		int y = creator.getY();
    		int w = creator.getWidth();
    		int h = creator.getHeight();
    		List<Integer> directions = new ArrayList<Integer>();
    		// ֻҪ����߿�û�к�container���ŵģ�����������
    		if (x > margin.getLeft()) {
    			directions.add(Direction.LEFT);
    		}
    		if (x+w < containerWidth - margin.getRight()) {
    			directions.add(Direction.RIGHT);
    		}
    		if (y > margin.getTop()) {
    			directions.add(Direction.TOP);
    		}
    		if (y+h < containerHeight - margin.getBottom()) {
    			directions.add(Direction.BOTTOM);
    		}
    		if (directions.isEmpty()) {
    			creator.setDirections(null);
    		}else  {
    			creator.setDirections(ArrayUtils.toPrimitive(directions.toArray(new Integer[directions.size()])));
    		}
    	}
    	
    }
    
    
    /**
     * �������0ʱ�����洦���ϼ��
     * ����ļ���������ʾ��ʵ�ʱ���Ĵ�С���ܼ��Ӱ��
     * ps:�ı䲼�ִ�С�������롢ɾ�������춼Ҫ���¿��Ǽ��
     * @param gap ���
     */
    public void addCompInterval(int gap) {
    	if (gap == 0) {
    		return;
    	}
    	int val = gap/2;
    	for (int i=0, len=this.getComponentCount(); i<len; i++) {
    		Component comp = this.getComponent(i);
    		Rectangle rec = comp.getBounds();
    		Rectangle bound = new Rectangle(rec);
    		if (rec.x > 0) {
    			bound.x += val;
    			bound.width -= val;
    		}
    		if (rec.width+rec.x < this.getWidth()) {
    			bound.width  -= val;
    		}
    		if (rec.y > 0) {
    			bound.y += val;
    			bound.height -= val;
    		}
    		if (rec.height+rec.y < this.getHeight()) {
    			bound.height -= val;
    		}
    		comp.setBounds(bound);
    	}
    	this.hasCalGap = true;
    }
    
    /**
     * ȥ��ԭ�еļ��
     * @param gap ���
     */
    public void moveCompInterval(int gap) {
    	if (gap == 0) {
    		return;
    	}
    	int val = gap/2;
    	for (int i=0, len=this.getComponentCount(); i<len; i++) {
    		Component comp = this.getComponent(i);
    		Rectangle rec = comp.getBounds();
    		Rectangle bound = new Rectangle(rec);
    		if (rec.x > 0) {
    			bound.x -= val;
    			bound.width += val;
    		}
    		if (rec.width+rec.x < this.getWidth()) {
    			bound.width  += val;
    		}
    		if (rec.y > 0) {
    			bound.y -= val;
    			bound.height += val;
    		}
    		if (rec.height+rec.y < this.getHeight()) {
    			bound.height += val;
    		}
    		comp.setBounds(bound);
    	}
    	this.hasCalGap = false;
    }
    
    /**
     * �Ƿ���Լ��뵱ǰ���
     * @param interval ���
     * @return Ĭ�Ϸ���true
     */
    public boolean canAddInterval(int interval) {
    	int val = interval/2;
    	for (int i=0, len=this.getComponentCount(); i<len; i++) {
    		Component comp = this.getComponent(i);
    		Rectangle rec = comp.getBounds();
    		Dimension d = new Dimension(this.getWidth(), this.getHeight());
    		Rectangle bound = dealBound(rec, d, val, 0);
    		if (bound.width < minWidth || bound.height< minHeight) {
    			return false;
    		}
    	}
    	return true;
    }
    
    private Rectangle dealBound(Rectangle rec, Dimension d, int val, double perc) {
    	Rectangle bound = reSetBound(rec, perc);
    	if (rec.x > 0) {
			bound.width -= val;
		}
		if (rec.width+rec.x < d.width) {
			bound.width -= val;
		}
		if (rec.y > 0) {
			bound.height -= val;
		}
		if (rec.height+rec.y < d.height) {
			bound.height -= val;
		}
		return new Rectangle(bound);
    }
    
    /**
     * ��С��Ȼ��߸߶�ʱ  ���ڼ���Ļ��Ƿ�֧����С
     */
    private boolean canReduceSize(double percent) {
    	int val = toData().getCompInterval()/2;
    	for (int i=0, len=this.getComponentCount(); i<len; i++) {
    		XCreator creator = (XCreator) this.getComponent(i);
			BoundsWidget widget = toData().getBoundsWidget(creator.toData());
			Rectangle rec = widget.getBounds();
    		Dimension d = new Dimension(this.getBackupBound().width, this.getBackupBound().height);
    		Rectangle bound = dealBound(rec, d, val, percent);
    		// ��С��߶���ʵ�ʴ�С�ж�
    		if (bound.width < MIN_WIDTH || bound.height< MIN_HEIGHT) {
    			return false;
    		}
    	}
    	return true;
    }
    
    private Rectangle reSetBound(Rectangle rec, double percent) {
    	Rectangle b = new Rectangle(rec);
    	b.x += b.x*percent;
    	b.y += b.y*percent;
    	b.width += b.width*percent;
    	b.height += b.height*percent;
    	return new Rectangle(b);
    }
    
    /**
     * ȥ���ڱ߾�
     * ������ʱ������ʵ�ʴ�Сʱ��Ҫ��ȥ���ڱ߾�
     */
    public void moveContainerMargin() {
    	PaddingMargin margin = toData().getMargin();
    	int num = this.getComponentCount();
    	int maxW = this.getWidth() - margin.getRight();
    	int maxH = this.getHeight() - margin.getBottom();
    	for (int i=0; i<num; i++) {
    		Component comp = this.getComponent(i);
    		Rectangle rec = comp.getBounds();
    		if (rec.x == margin.getLeft()) {
    			rec.x = 0;
    			rec.width += margin.getLeft();
    		}
    		if (rec.y == margin.getTop()) {
    			rec.y = 0;
    			rec.height += margin.getTop();
    		}
    		if (rec.x +rec.width == maxW) {
    			rec.width += margin.getRight();
    		}
    		if (rec.y + rec.height == maxH) {
    			rec.height += margin.getBottom();
    		}
    		comp.setBounds(rec);
    	}
    }
    
    public Component getTopComp(int x, int y) {
    	int val = getAcualInterval();
		return this.getComponentAt(x, y-default_Length-val);
	}
	
	public Component getLeftComp(int x, int y) {
		int val = getAcualInterval();
		return this.getComponentAt(x-default_Length-val, y);
	}
	
	public Component getRightComp(int x, int y, int w) {
		int val = getAcualInterval();
		return this.getComponentAt(x+w+default_Length+val, y);
	}
	
	public Component getBottomComp(int x, int y, int h) {
		int val = getAcualInterval();
		return this.getComponentAt(x, y+h+default_Length+val);
	}
	
	public Component getRightTopComp(int x, int y, int w) {
		int val = getAcualInterval();
		return this.getComponentAt(x+w-default_Length, y-default_Length-val);
	}
	
	public Component getBottomLeftComp(int x, int y, int h) {
		int val = getAcualInterval();
		return this.getComponentAt(x-default_Length-val, y+h-default_Length);
	}
	
	public Component getBottomRightComp(int x, int y, int h, int w) {
		int val = getAcualInterval();
		return this.getComponentAt(x+w+default_Length+val, y+h-default_Length);
	}
	
	public Component getRightBottomComp(int x, int y, int h, int w) {
		int val = getAcualInterval();
		return this.getComponentAt(x+w-default_Length, y+h+default_Length+val);
	}
    
	/**
	 * ����������С�İٷֱ�
	 * @return the containerPercent
	 */
	public double getContainerPercent() {
		return containerPercent;
	}

	/**
	 * ����������С�İٷֱ�
	 * @param containerPercent the containerPercent to set
	 */
	public void setContainerPercent(double containerPercent) {
		this.containerPercent = containerPercent;
		minWidth = (int) (XWFitLayout.MIN_WIDTH*containerPercent);
		minHeight = (int) (XWFitLayout.MIN_HEIGHT*containerPercent);
	}

    /**
     * ������Ƿ������ק(���в�����������Ӧ���ֲ�������ק)
     * @return ���򷵻�true
     */
    public boolean isSupportDrag(){
        return false;
    }
    
    /**
     * ���ؽ��洦���ݰٷֱȵ��������С���
     * @return ��С���
     */
    public int getActualMinWidth() {
		return this.minWidth;
	}
    
    /**
     * ���ؽ��洦���ݰٷֱȵ��������С�߶�
     * @return ��С�߶�
     */
    public int getActualMinHeight() {
		return this.minHeight;
	}
    
    /**
     * ���ؽ��洦���ݰٷֱȵ�����ļ����С����Ϊż����
     * @return ���
     */
    public int getAcualInterval() {
    	// adapter�Ǳ߽������ȷ֡�ɾ����Ҫ�ж��Ƿ���룬���Լ��תΪż��
    	int interval = (int) (toData().getCompInterval()*containerPercent);
    	int val = interval/2;
    	return val*2;
    }

	/**
	 * �����Ƿ��Ѿ����ϼ��
	 * @return the hasCalGap �Ƿ�
	 */
	public boolean isHasCalGap() {
		return hasCalGap;
	}

	/**
	 * ���õ�ǰ�ļ��
	 * @param hasCalGap the hasCalGap to set ���
	 */
	public void setHasCalGap(boolean hasCalGap) {
		this.hasCalGap = hasCalGap;
	}
	
	/**
	 * �����ϴεļ��
	 * @param backupPercent �ϴεİٷֱ�
	 */
	public void setBackupGap(double backupPercent) {
		int value = (int) (toData().getCompInterval()*backupPercent);
		int val = value/2;
		this.backupGap = val*2;
	}
	
    /**
	 * Ѱ�������Ϊ����Ӧ���ֵĸ�����
	 * 
	 * @return ��������
	 * 
	 *
	 * @date 2014-12-30-����3:15:28
	 * 
	 */
	public XLayoutContainer findNearestFit() {
		return this;
	}
	
}

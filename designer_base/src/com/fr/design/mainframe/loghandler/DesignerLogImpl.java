package com.fr.design.mainframe.loghandler;

import com.fr.stable.fun.impl.AbstractLogProvider;
import com.fr.stable.xml.LogRecordTimeProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/11 0011.
 */
public class DesignerLogImpl extends AbstractLogProvider{

    private static DesignerLogImpl instance = new DesignerLogImpl();

    public static DesignerLogImpl getInstance(){
        return instance;
    }

    private DesignerLogImpl(){

    }

    private List<LogRecordTimeProvider> records = new ArrayList<LogRecordTimeProvider>();

    /**
     * ����ڴ��е���־��¼
     */
    public void clear(){
        records.clear();
    }

    /**
     * ��ȡ������־��Ϣ
     *
     * @return ��־��Ϣ
     */
    public LogRecordTimeProvider[] getRecorders(){
        return records.toArray(new LogRecordTimeProvider[records.size()]);
    }

    @Override
    public void record(LogRecordTimeProvider logRecordTime) {
        records.add(logRecordTime);
    }
}

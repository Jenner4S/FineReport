/**
 * 
 */
package com.fr.design.report.share;

import java.util.HashMap;

import com.fr.data.impl.EmbeddedTableData;
import com.fr.general.GeneralUtils;
import com.fr.stable.ArrayUtils;
import com.fr.stable.StringUtils;

/**
 * ���������tabledata
 * 
 * @author neil
 *
 * @date: 2015-3-10-����10:45:41
 */
public class ConfuseTabledataAction {

	/**
	 * ����ָ�����������ݼ�
	 * 
	 * @param info ������ص���Ϣ
	 * @param tabledata ��Ҫ���������ݼ�
	 * 
	 */
    public void confuse(ConfusionInfo info, EmbeddedTableData tabledata){
		int rowCount = tabledata.getRowCount();
		String[] keys = info.getConfusionKeys();
		for (int j = 0, len = ArrayUtils.getLength(keys); j < len; j++) {
			if(StringUtils.isEmpty(keys[j])){
				continue;
			}
			
			//�������Ѿ�����������, ������ͬ��ԭʼ����, ���������Ľ����һ�µ�.
			HashMap<Object, Object> cachedValue = new HashMap<Object, Object>();
			for (int k = 0; k < rowCount; k++) {
				Object oriValue = tabledata.getValueAt(k, j);
				Object newValue;
				if(cachedValue.containsKey(oriValue)){
					newValue = cachedValue.get(oriValue);
				}else{
					newValue = confusionValue(info, j, keys[j], cachedValue, oriValue);
					cachedValue.put(oriValue, newValue);
				}
				
				tabledata.setValueAt(newValue, k, j);
			}
		}
    }
    
    //����ÿһ�����ӵ�����
    private Object confusionValue(ConfusionInfo info, int colIndex, String key, HashMap<Object, Object> cachedValue, Object oriValue){
		if (info.isNumberColumn(colIndex)){
			//��������ָ�ʽ��, ��ô�����˷�, eg: 3 * 3, 8 *3.....
			Number keyValue = GeneralUtils.objectToNumber(key, false);
			Number oriNumber = GeneralUtils.objectToNumber(oriValue, false);
			return oriNumber.doubleValue() * keyValue.doubleValue();
		}
		
		String oriStrValue = GeneralUtils.objectToString(oriValue);
		if(StringUtils.isEmpty(oriStrValue)){
			//����ǿ��ֶ�, ��Ĭ�ϲ�����. ��Ϊ�еĿͻ����������˿��ֶ�����һЩ����, ��������֮���.
			return oriStrValue;
		}
		
		//Ĭ��������ʽ�ľ����ӷ�, eg: ����1, ����2......
		return key + cachedValue.size();
    }

}

package com.fr.design.formula;

import com.fr.base.FRContext;
import com.fr.file.FunctionManager;
import com.fr.file.FunctionManagerProvider;
import com.fr.function.*;
import com.fr.general.ComparatorUtils;
import com.fr.general.Inter;
import com.fr.plugin.ExtraClassManager;
import com.fr.stable.EncodeConstants;
import com.fr.stable.OperatingSystem;
import com.fr.stable.StableUtils;
import com.fr.stable.StringUtils;
import com.fr.stable.script.Function;
import com.fr.stable.script.FunctionDef;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public abstract class FunctionConstants {

    public static FunctionGroup PLUGIN = new FunctionGroup() {
        @Override
        public String getGroupName() {
            return Inter.getLocText("FR-Base_Formula_Plugin");
        }

        @Override
        public NameAndDescription[] getDescriptions() {
            FunctionDef[] fs = ExtraClassManager.getInstance().getFunctionDef();
            int count = fs.length;
            FunctionDefNAD[] nads = new FunctionDefNAD[count];
            for (int i = 0; i < count; i ++) {
                nads[i] = new FunctionDefNAD(fs[i]);
            }
            return nads;
        }
    };

	public static FunctionGroup CUSTOM = new FunctionGroup() {
		@Override
		public String getGroupName() {
			return Inter.getLocText("FormulaD-Custom_Function");
		}
		
		@Override
		public NameAndDescription[] getDescriptions() {
            FunctionManagerProvider funtionManager = FunctionManager.getProviderInstance();
			if (funtionManager != null) {
				int functionDefCount = funtionManager.getFunctionDefCount();

				FunctionDefNAD[] nads = new FunctionDefNAD[functionDefCount];
				for (int i = 0; i < functionDefCount; i++) {
					nads[i] = new FunctionDefNAD(funtionManager.getFunctionDef(i));
				}
				
				return nads;
			}
			
			return new NameAndDescription[0];
		}
	};
	
	public static NameAndFunctionList COMMON = new NameAndFunctionList(Inter.getLocText("FormulaD-Most_Recently_Used"), new Function[] {
		new SUM(), new COUNT(), new AVERAGE(), new CHAR(), new DATE(), new MAX(), new MIN(), new TIME(), new RANGE()
	});
	
	public static NameAndTypeAndFunctionList[] EMBFUNCTIONS = new NameAndTypeAndFunctionList[] {
		new NameAndTypeAndFunctionList(Inter.getLocText("FormulaD-Math_&_Trig"), Function.MATH), 
		new NameAndTypeAndFunctionList(Inter.getLocText("FR-Designer_FormulaD-Text"), Function.TEXT),
		new NameAndTypeAndFunctionList(Inter.getLocText("FormulaD-Date_&_Time"), Function.DATETIME),
		new NameAndTypeAndFunctionList(Inter.getLocText("FR-Designer_FormulaD-Logical"), Function.LOGIC),
		new NameAndTypeAndFunctionList(Inter.getLocText("FR-Designer_FormulaD-Array"), Function.ARRAY),
		new NameAndTypeAndFunctionList(Inter.getLocText("FR-Designer_FormulaD-Report"), Function.REPORT),
		new NameAndTypeAndFunctionList(Inter.getLocText("FR-Designer_FormulaD-Other"), Function.OTHER),
		new NameAndTypeAndFunctionList(Inter.getLocText(new String[]{"Level_coordinates", "FormulaD-Functions"}), Function.HA)
	};
	
	public static FunctionGroup ALL = new FunctionGroup() {
		@Override
		public String getGroupName() {
			return Inter.getLocText("FR-Designer_FormulaD-All");
		}
		
		@Override
		public NameAndDescription[] getDescriptions() {
			List<NameAndDescription> all = new ArrayList<NameAndDescription>();
			for (int i = 0; i < EMBFUNCTIONS.length; i++) {
				all.addAll(Arrays.asList(EMBFUNCTIONS[i].getDescriptions()));
			}
            Collections.addAll(all, PLUGIN.getDescriptions());
            Collections.addAll(all, CUSTOM.getDescriptions());
			
			java.util.Collections.sort(all, NameAndDescriptionComparator);
			
			return all.toArray(new NameAndDescription[all.size()]);
		}
	};


	
	private static Comparator<NameAndDescription> NameAndDescriptionComparator = new Comparator<NameAndDescription>() {
		@Override
		public int compare(NameAndDescription o1, NameAndDescription o2) {
			return ComparatorUtils.compare(o1.getName(), o2.getName());
		}
	};
	
	private static String[] findClassNamesUnderFilePath(String filePath) {
		java.util.List<String> classNameList = new ArrayList<String>();
		/*
		 * alex:�����jar���е�class�ļ�
		 * file:/D:/opt/FineReport6.5/WebReport/WEB-INF/lib/fr-server-6.5.jar!/com/fr/rpt/script/function
		 */
		if (filePath.indexOf("!/") >= 0) {
			String[] arr = filePath.split("!/");
			String jarPath = arr[0].substring(6); // alex:substring(6)ȥ��ǰ���file:/�������ַ�
			String classPath = arr[1];
            if (OperatingSystem.isMacOS()){
                //windows��substring����d:\123\456, mac��substring����Application/123/456
                jarPath = StringUtils.perfectStart(jarPath, "/");
            }
			
			ZipFile zip;
			try {
				zip = new ZipFile(jarPath);
				Enumeration entries = zip.entries();
				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) entries.nextElement();
					if (entry.isDirectory()) {
						continue;
					}
					
					String entryName = entry.getName();
					if (entryName.indexOf(classPath) < 0 || !entryName.endsWith(".class")) {
						continue;
					}
					
					classNameList.add(entryName.substring(classPath.length() + 1));
				}
			} catch (IOException e) {
				FRContext.getLogger().error(e.getMessage(), e);
			}
		} else {
			File dir = new File(filePath);
			
			for (File f : dir.listFiles()) {
				String fileName = f.getName();
				if (fileName.endsWith(".class")) {
					classNameList.add(fileName);
				}
			}
		}
		
		return classNameList.toArray(new String[classNameList.size()]);
	}
	
	// alex:��ȡcom.fr.script.function���������е�Function��
	static {
		String pkgName = "com.fr.function";
		Class<Function> iface = Function.class;
		ClassLoader classloader = iface.getClassLoader();
		URL url = classloader.getResource(pkgName.replace('.', '/'));
		String classFilePath = url.getFile();
		
		/*
		 * alex:url.getFile��ȡ�ĵ�ַ��,����пո�����ĻᱻURLEncoder.encode����
		 * ����%20����%��ͷ�Ķ���,����new File��ʱ��%20���޷������ɿո�,�����ڴ���Ҫ��URLDecoder.decode����
		 */
		try {
			classFilePath = URLDecoder.decode(classFilePath, EncodeConstants.ENCODING_UTF_8);
		} catch (UnsupportedEncodingException e1) {
			FRContext.getLogger().error(e1.getMessage(), e1);
		}
		FRContext.getLogger().info("ClassFilePath:" + classFilePath);
		/*
		 * alex:�����jar���е�class�ļ�
		 * file:/D:/opt/FineReport6.5/WebReport/WEB-INF/lib/fr-server-6.5.jar!/com/fr/rpt/script/function
		 */
		for (String fileName : findClassNamesUnderFilePath(classFilePath)) {
			try {
				Class<?> cls = Class.forName(pkgName + "." + fileName.substring(0, fileName.length() - 6));
				if (StableUtils.classInstanceOf(cls, iface)) {
					Function inst;
					inst = (Function)cls.newInstance();
					for (int fi = 0; fi < EMBFUNCTIONS.length; fi++) {
						if (EMBFUNCTIONS[fi].test(inst)) {
							break;
						}
					}
					
				}
			} catch (ClassNotFoundException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			}
		}
	}
}

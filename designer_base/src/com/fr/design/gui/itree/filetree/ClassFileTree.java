package com.fr.design.gui.itree.filetree;

import java.io.File;

import com.fr.file.filetree.FileNode;
import com.fr.file.filetree.IOFileNodeFilter;
import com.fr.stable.project.ProjectConstants;

/*
 * ��ʾEnv�µ�classesĿ¼���������class�ļ�
 */
public class ClassFileTree extends EnvFileTree {
	public ClassFileTree() {
		super(ProjectConstants.CLASSES_NAME, null, new IOFileNodeFilter(new String[]{"class"}));
	}
	
	/*
	 * ѡ��class
	 */
	public void setSelectedClassPath(String classPath) {
		if(classPath == null || !classPath.endsWith(".class")) {
			return;
		}
		
		String[] dirs = classPath.split("\\.");
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < dirs.length; i++) {
			if(i == dirs.length - 1) {
				sb.append('.');
			} else if (i > 0) {
				sb.append(File.separatorChar);
			}
			
			sb.append(dirs[i]);
		}
		
		this.selectPath(sb.toString());
	}
	
	/*
	 * ����ѡ�е�Class��·��
	 */
	public String getSelectedClassPath() {
		FileNode fn = this.getSelectedFileNode();
		if(fn != null && !fn.isDirectory()) {
			String envPath = fn.getEnvPath();
			
			if(envPath.startsWith(ProjectConstants.CLASSES_NAME)) {
				String resPath = envPath.substring(ProjectConstants.CLASSES_NAME.length() + 1);
				
				resPath = resPath.replaceAll("[/\\\\]", ".");
				//marks:ȥ��.class��׺
                if (resPath.toLowerCase().endsWith(".class")) {
                	resPath = resPath.substring(0,resPath.length() - ".class".length());
                }
                return resPath;
			}
		}
		
		return null;
	}
}

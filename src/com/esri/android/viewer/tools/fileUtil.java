package com.esri.android.viewer.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class fileUtil {
	 /**
	 *��ȡĳ·���������ļ����������ļ��б� 
	 *@param type �ļ����� Ĭ��Ϊall Ϊ��������,folderΪ�ļ���
	 *@param filePath �ļ���·��
	 */
	 public List<file> getFileDir(String filePath,String type) { 
		 List<file> result =null;
         try{  
            
             File f = new File(filePath);  
             File[] files = f.listFiles();// �г������ļ�   
             // �������ļ�����list��  
             if(files != null){  
                 int count = files.length;// �ļ�����  
                 result =  new ArrayList<file>();
                 for (int i = 0; i < count; i++) {  
                     File file = files[i];  
                    file file_t = new file();
                    file_t.item = file.getName();
                    file_t.path = file.getPath();
                    if (type=="all") {
						result.add(file_t);
					}else if(type =="folder"){
						String str = file_t.item;
						if(str.indexOf(".")==-1){//ֻ�����ļ���
							result.add(file_t);
						}else{
							continue;
						}
					}else{
						String str = file_t.item;
						if(str.indexOf(type)!=-1){//ֻ����ָ����������
							result.add(file_t);
						}else{
							continue;
						}
					}
                 }  
             }  
         }catch(Exception ex){  
             ex.printStackTrace();  
         } 
         return result;
	 }
	 
	public  class file
	 {
		 public  String item;//�ļ�����
		 public  String path;//�ļ�·��
	 }
}

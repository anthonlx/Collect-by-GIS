package com.esri.android.viewer.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class fileTools {

	 private static String urlNull = "ԭ�ļ�·��������";
	 private static String isFile = "ԭ�ļ������ļ�";
	 private static String canRead = "ԭ�ļ����ܶ�";
	 //private static String notWrite = "�����ļ�����д��";
	 private static String message = "OK";
	 private static String cFromFile = "����ԭ�ļ�����:";
	 private static String ctoFile = "���������ļ�����:";
	
	 //ϵͳ�ļ�������
	 private static String mainFile = SystemVariables.mainDirectory; //ϵͳ��Ŀ¼
	 private static String baseMapFile =SystemVariables.baseMapDirectory;//������ͼ�ļ���
	 private static String achievePackageFile =SystemVariables.achievePackageDirectory;//�ɹ����ļ���
	 private static String taskPackageFile = SystemVariables.taskPackageDirectory;//������ļ���
	 private static String systemConfigFile =SystemVariables.systemConfigDirectory;//ϵͳ�����ļ���
	 private static String tempFile = SystemVariables.tempDirectory;//ϵͳ��ʱ�ļ���
	 private static String samplepointFile= SystemVariables.SamplePointDirectory;//�������ļ���
	 
	 //�����Ŀ¼�ṹ
	 private static String packageFiles = SystemVariables.packageDirectory;//�����
	 private static String PicturesFiles = SystemVariables.PicturesDirectory ;//��Ƭ�ļ���
	 private static String VideosFiles = SystemVariables.VideosDirectory;//�����ļ���
	 private static String VoicesFiles = SystemVariables.VoicesDirectory;//��Ƶ�ļ���
	 private static String DraftsFiles = SystemVariables.DraftsDirectory;//��ͼ�ļ���
	 
	 public static filePath filepath = new filePath();//��ʼ��ϵͳĿ¼·��;
	 
	 
	 public fileTools()
	 {	
	 }
	 
	 /**
	  * ��ʼ��ϵͳ�ļ��ṹ
	  * @return
	  */
 	 public static boolean initFilesDir(String sdpath){
	     String path=sdpath+"/" +mainFile ;
	    
	     try {
			File _pathMain = new File(path); // ��Ŀ¼
			if (!_pathMain.exists()) {
				//�������ڣ�����Ŀ¼
				_pathMain.mkdirs();
			} else {
			}
			filepath.mainFilePath = _pathMain.getPath();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	     
		try {
			File _baseMapPath = new File(path + "/" + baseMapFile);//�������ߵ�ͼĿ¼
			if (!_baseMapPath.exists()) {
				//�������ڣ�����Ŀ¼ 
				_baseMapPath.mkdirs();
			} else {
			}
			filepath.baseMapFilePath = _baseMapPath.getPath();
			
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
//		try {
//			File _achievePackagePath = new File(path + "/" + achievePackageFile);//�ɹ����ļ���Ŀ¼
//			if (!_achievePackagePath.exists()) {
//				//�������ڣ�����Ŀ¼ 
//				_achievePackagePath.mkdirs();		
//			} else {
//			}
//			filepath.achievePackageFilePath = _achievePackagePath.getPath();
//		} catch (Exception e) {
//			// TODO: handle exception
//			return false;
//		}
				
		try {
			File _taskPackagePath = new File(path + "/" + taskPackageFile);//�������ļ���Ŀ¼
			if (!_taskPackagePath.exists()) {
				//�������ڣ�����Ŀ¼ 
				_taskPackagePath.mkdirs();
			} else {
			}
			filepath.taskPackageFilePath = _taskPackagePath.getPath();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
				
		try {
			File _systemConfigPath = new File(path + "/" + systemConfigFile);//ϵͳ�����ļ���Ŀ¼
			if (!_systemConfigPath.exists()) {
				//�������ڣ�����Ŀ¼ 
				_systemConfigPath.mkdirs();
			} else {
			}
			filepath.systemConfigFilePath = _systemConfigPath.getPath();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
			
//		try {
//			File _tempPath = new File(path + "/" + tempFile);//��ʱ�ļ���Ŀ¼
//			if (!_tempPath.exists()) {
//				//�������ڣ�����Ŀ¼ 
//				_tempPath.mkdirs();
//			} else {
//			}
//			filepath.tempFilePath = _tempPath.getPath();
//		} catch (Exception e) {
//			// TODO: handle exception
//			return false;
//		}
		
		try {
			File _samplepointPath = new File(path + "/" + samplepointFile);//�������ļ���Ŀ¼
			if (!_samplepointPath.exists()) {
				//�������ڣ�����Ŀ¼ 
				_samplepointPath.mkdirs();
			} else {
			}
			filepath.samplepointPath = _samplepointPath.getPath();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
		return true;
	 }

 	 /**
 	  * ��ʼ����չSD���е�ͼ�ļ�Ŀ¼
 	  * @param path
 	  * @return
 	  */
 	 public static boolean intiExtBaseMapDir(String extsdpath){
 		 String path=extsdpath+"/" +SystemVariables.ExtbaseMapDirectory ;
	     try {
			File _pathext = new File(path); // ��Ŀ¼
			if (!_pathext.exists()) {
				//�������ڣ�����Ŀ¼
				_pathext.mkdirs();
			} else {
			}
			filepath.extsdcardbaseMapFilePath = _pathext.getPath();
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	     
 	 }
 	 
 	 /**
 	  * ��ʼ�����������Ŀ¼
 	  * @param path ������������ĸ�Ŀ¼
 	  * @param name  �����Ŀ¼����
 	  * @return
 	  */
  	 public static void initPackageDir(String path,String name)
 	 {
 		String mainPath = path+"/"+name;
    	File pathMain= new File(mainPath);
    	 if (!pathMain.exists()) {
 	        //�������ڣ�����Ŀ¼
 	        pathMain.mkdirs(); 
 	        
 	        //ͼƬ�ļ���
 	        String pic_childPath = mainPath+"/"+PicturesFiles;
 	    	File pic_path= new File(pic_childPath);
 	    	 if (!pic_path.exists()) {
 	 	        //�������ڣ�����Ŀ¼
 	    		pic_path.mkdirs();  	        
 	         }	 
 	    	 
  	        //��Ƶ�ļ���
  	        String video_childPath = mainPath+"/"+VideosFiles;
  	    	File video_path= new File(video_childPath);
  	    	 if (!video_path.exists()) {
  	 	        //�������ڣ�����Ŀ¼
  	    		video_path.mkdirs();  	        
  	         }	 
 	    	 
  	    	//�����ļ���
  	        String voice_childPath = mainPath+"/"+VoicesFiles;
   	    	File voice_path= new File(voice_childPath);
   	    	 if (!voice_path.exists()) {
   	 	        //�������ڣ�����Ŀ¼
   	    		voice_path.mkdirs();  	        
   	         }
   	    	 
   	    	//��ͼ�ļ���
   	        String drafts_childPath = mainPath+"/"+DraftsFiles;
    	    	File drafts_path= new File(drafts_childPath);
    	    	 if (!drafts_path.exists()) {
    	 	        //�������ڣ�����Ŀ¼
    	    		drafts_path.mkdirs(); 
    	         }
 	    	 
         }	 
 	 }
 	 
	 /**
	  * ��ȡfileTools���ļ���·������
	  */
	 public static filePath GetFileTools()
	 {
		 return filepath;
	 }
	  
    /**
     * ��ָ���ļ����´������ļ���
     * @param path �ļ���·��
     * @param name ���ļ���·��
     * @return
     */
    public static boolean createChildFilesDir(String path,String name)
    {
    	String childPath = path+"/"+name;
    	File pathMain= new File(childPath);
    	 if (!pathMain.exists()) {
 	        //�������ڣ�����Ŀ¼
 	        pathMain.mkdirs(); 
         }	 
    	return true;
    }

    /**
     * ɾ���ļ��м��ļ�������������
     *@param file �ļ���·��
     *@return �����Ƿ�ɾ���ɹ�
     */
    public  static boolean deleteFiles(String path) {
        File file = new File(path);
    	try {
			if (file.exists()) { // �ж��ļ��Ƿ����
				if (file.isFile()) { // �ж��Ƿ����ļ�
					file.delete(); // delete()����
				} else if (file.isDirectory()) { // �����������һ��Ŀ¼
					File files[] = file.listFiles(); // ����Ŀ¼�����е��ļ� files[];
					for (int i = 0; i < files.length; i++) { // ����Ŀ¼�����е��ļ�
						deleteFiles(files[i].getPath()); // ��ÿ���ļ� ������������е���
					}
					file.delete();//ɾ��Ŀ¼
				}
				//file.delete();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			// TODO: �����쳣����
			return false;
		}
    }
    
    /**
     * ��ȡTXT�ļ�����
     * @param filePath �ļ�·��+����
     * @return TXT�ļ��е����� String
     */
	public static String openTxt(String filePath)
    {    	  	
    	  File file = new File(filePath);
    	  String result = "";
    	  if (!file.exists()) {
    		  //�ж��ļ��Ƿ���ڣ���������ڣ��򴴽��ļ�
    		  try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} 		 
    	  }  	  
    	  try {
				//#���ļ�attribute.txt�ж�������
				//���ڴ��п���һ�λ�����
				byte Buffer[] = new byte[1024];
				//�õ��ļ�������
				@SuppressWarnings("resource")
				FileInputStream in = new FileInputStream(file);
				//���������������ȷ��뻺����������֮����д���ַ��������
				int len = in.read(Buffer);
				//����һ���ֽ����������
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				outputStream.write(Buffer, 0, len);
				//���ֽ������תString
				result =  new String(outputStream.toByteArray());
			} catch (Exception e) {
				// TODO: handle exception
			}   		  
    	 return result;
    }
  
	/**
	 *��·��filePath�´����ļ�
	 *@param filePath �ļ���ַ+���ƣ� 
	 *@param Content ���ݣ�
	 *@return �����Ƿ񴴽��ɹ� 
	 */
	public static boolean saveTxt(String filePath,String Content)
	{
		  File file = new File(filePath);
    	  if (!file.exists()) {
    		  //�ж��ļ��Ƿ���ڣ���������ڣ��򴴽��ļ�
    		  try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} 		 
    	  }
        try {
			//#д���ݵ��ļ�XXX.txt
			//����һ���ļ������
			FileOutputStream out = new FileOutputStream(file, false);//true��ʾ���ļ�ĩβ���
			out.write(Content.getBytes("UTF-8"));
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}

	/**
	  * @param fromFile ���ļ���ַ������         
	  * @param toFile ���ļ���ַ������      
	  * @return ���ر����ļ�����Ϣ��ok�ǳɹ����������Ǵ���
	  */
	 public static String copyFile(String fromFileUrl, String toFileUrl) {
		  File fromFile = null;
		  File toFile = null;
		  try {
		   fromFile = new File(fromFileUrl);
		  } catch (Exception e) {
		   return cFromFile + e.getMessage();
		  }
		
		  try {
		   toFile = new File(toFileUrl);
		  } catch (Exception e) {
		   return ctoFile + e.getMessage();
		  }
		
		  if (!fromFile.exists()) {
		   return urlNull;
		  }
		  if (!fromFile.isFile()) {
		      return isFile;
		  }
		  if (!fromFile.canRead()) {
		   return canRead;
		  }
		
		  // ���Ƶ���·����������ھʹ���
		  if (!toFile.getParentFile().exists()) {
		   toFile.getParentFile().mkdirs();
		  }
		
		  if (toFile.exists()) {
		   toFile.delete();
		  }
		
		  if (!toFile.canWrite()) {
		   //return notWrite;
		  }
		  
		  try {
		   java.io.FileInputStream fosfrom = new java.io.FileInputStream(
		     fromFile);
		   java.io.FileOutputStream fosto = new FileOutputStream(toFile);
		   byte bt[] = new byte[1024];
		   int c;
		
		   while ((c = fosfrom.read(bt)) > 0) {
		    fosto.write(bt, 0, c); // ������д�����ļ�����
		   }
		   //�ر�������
		   fosfrom.close();
		   fosto.close();
		
		  } catch (Exception e) {
		   e.printStackTrace();
		   message = "����ʧ��!";		   
		  }		
		  return message;
		 }
	
	 public static boolean isExist(String filePath)
	 {
		  File file = new File(filePath);
		  return  file.exists();
	 }
	 
	 public static int getFilesNum(String filedirPath)
	 {
		 int result = 0;
		 fileUtil fu = new fileUtil();
		 result = fu.getFileDir(filedirPath, "all").size();
		 return result;
	 }
	 	 
	 //ϵͳ�ļ���Ŀ¼��
	 public static class filePath{
		public String mainFilePath =null;
		 public String baseMapFilePath = null;
		 public String achievePackageFilePath = null;
		 public  String taskPackageFilePath = null;
		 public  String systemConfigFilePath =null;
		 public  String tempFilePath = null;
		 public String samplepointPath=null;
		 public String extsdcardbaseMapFilePath=null;
	 }
	 	 
}


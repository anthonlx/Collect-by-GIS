package com.esri.android.viewer.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.esri.android.viewer.ViewerApp;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class sysTools {
   
	/**
    * ϵͳ����Ŀ¼��ʼ��
    */
	public static void intiWorkspaceDir(Context context){
		 boolean sdhave = isHasSdcard();
		 if(sdhave){
			 File Dir =Environment.getExternalStorageDirectory();
			 //�õ�һ��·�����������ڲ�sdcard���ļ���·��������
			String sdpath =Dir.getPath();
			boolean isInit = com.esri.android.viewer.tools.fileTools.initFilesDir(sdpath);
			String extsdpah = "/storage/extSdCard";
			boolean iscreat = com.esri.android.viewer.tools.fileTools.intiExtBaseMapDir(extsdpah);
		       if(isInit){
		    	   //Toast.makeText(BaseViewerActivity.this,"ϵͳ����Ŀ¼��ʼ���ɹ���",Toast.LENGTH_SHORT).show();
		    	   ViewerApp appState = ((ViewerApp)context.getApplicationContext());  //����ϵͳȫ�������������activity����ʹ��
		    	   appState.SetFilePaths(com.esri.android.viewer.tools.fileTools.GetFileTools());   
		    	   
		    	   //����ϵͳ���ݿ�
		           final com.esri.android.viewer.tools.fileTools.filePath  WorkSpacePath = appState.getFilePaths();
		           com.esri.android.viewer.tools.sqliteHelper.createConfigDB(WorkSpacePath.systemConfigFilePath);
		           //Toast.makeText(BaseViewerActivity.this,"ϵͳ���ݿ��ʼ���ɹ���",Toast.LENGTH_SHORT).show();              
		       }
		 }
		 
		 
	}
	
	/**
	 * ��ʼ���������ļ���
	 * @param samplepointpath �������Ŀ¼
	 */
	public  static void  intiSamplepoint(String samplepointpath,String sign) {
	     String path=samplepointpath+"/" +com.esri.android.viewer.tools.SystemVariables.PointPackageDirectory +sign;
	     try {
			File _pathMain = new File(path); 
			if (!_pathMain.exists()) {
				//�������ڣ�����Ŀ¼
				_pathMain.mkdirs();
			} else {
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	     
	     String photopath = path +"/" + com.esri.android.viewer.tools.SystemVariables.PicturesDirectory;
	     try {
				File _pathMain = new File(photopath); 
				if (!_pathMain.exists()) {
					//�������ڣ�����Ŀ¼
					_pathMain.mkdirs();
				} else {
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		     
	     createSamplePointDB(path);
	}
	

	/**
	  * �������������ݿ�
	  * @param path ���ݿ�洢·��
	  * @return
	  */
   private static boolean createSamplePointDB(String path)
   {
	   SQLiteDatabase mDb;
	   	String dbPath=path; //+"/database";
	   	File dbp=new File(dbPath);
	   	File dbf=new File(dbPath+"/"+com.esri.android.viewer.tools.SystemVariables.PointDB);			                   
	   	 if(!dbp.exists())//�ж�Ŀ¼�Ƿ���ڣ�������������½�Ŀ¼
	   	 {
	   			dbp.mkdir();
	   	}   
	   	//���ݿ��ļ��Ƿ񴴽��ɹ�
		     boolean isFileCreateSuccess=false;                 
		      if(!dbf.exists())
		      {
		    	  	try {
			    		   //�������ݿ��ļ�
						isFileCreateSuccess=dbf.createNewFile();
					} catch (IOException e) {
						// TODO �Զ����ɵ� catch ��
						e.printStackTrace();
					}    	   
		      } else
		      {    
		             isFileCreateSuccess=true;
		      }
		       if(isFileCreateSuccess)
		       {
		    	   //������ݿⴴ���ɹ��򴴽����ݱ�
		    	   mDb = SQLiteDatabase.openOrCreateDatabase(dbf, null);
		    	   try {
					String sqlStr = "CREATE TABLE PHOTO (" +
							"PHID TEXT PRIMARY KEY," +
							"FILE TEXT,PHTM TEXT," +
							"LONG TEXT,LAT TEXT," +
							"DOP TEXT," +
							"ALT TEXT," +
							"MMODE TEXT," +
							"SAT TEXT," +
							"AZIM TEXT," +
							"AZIMR TEXT," +
							"AZIMP TEXT," +
							"DIST TEXT," +
							"TILT TEXT," +
							"ROLL TEXT," +
							"CC TEXT," +
							"REMARK TEXT," +
							"CREATOR TEXT," +
							"FOCAL TEXT);";
					mDb.execSQL(sqlStr );
				} catch (Exception e) {
					Log.d("�����㽨���쳣", e.toString());				
				}
				return true;
		       }else
		       {	    	   
		    	   return false;
		       }
   }
   
	
	/**
     * �ж�SD���Ƿ���װ��
     */
    public static boolean isHasSdcard()
    {	
    	String status = Environment.getExternalStorageState();
    	  if (status.equals(Environment.MEDIA_MOUNTED)) {
    	   return true;
    	  } else {
    	   return false;
    	  }
    }
    
	/**
	 * �ж����磨3G��GPRS���Ƿ���ͨ
	 * @param <context>
	 */
    public static  boolean isConnected(Context context)
    {
    	final ConnectivityManager connMgr =
    			(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    	final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    	if (wifi.isConnected() || mobile.isConnected())
    			return true;
    	else
    			return false;
    }
    
    /**
     * �ж�Wifi�Ƿ���ͨ
     */
    
    /**
     * �ж�GPS�Ƿ�ʼ
     */
    
    /**
     * ��ȡָ���ļ���С
     * @throws Exception 
     */
    public static String getFileSize(String filePath) {
    	String result = "";
    	File f = new File(filePath);  
    	DecimalFormat df = new DecimalFormat();  //��ʽ��
    	df.applyPattern("###0.00;-###0.00");  
    	long l=0;
    	if(filePath.contains(".")){
    		l =f.length();
    	}else{
    		try {
    			l = getFileSize(f);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}	
    	double size =0;
    	if(l>1024){
    		size=l/1024.0;//ת��ΪKB
    		if(size>1024){
    			size = size/1024;//ת��ΪMB
    			if(size>1024){
    				size = size/1024;//ת��ΪGB
    				result = String.valueOf(df.format(size)) + "GB";
    			}else{
    				result = String.valueOf(df.format(size)) + "MB";
    			}		
    		}else{
    			result = String.valueOf(df.format(size)) + "KB";
    		}
    	}else{
    		result = String.valueOf(l) + "byte";
    	}
    	return result; 
    }
    
    /**
	 * @Methods: getFileSize
	 * @Description: ��ȡ�ļ��еĴ�С���������ļ���Ҳ����
	 * @param f  File ʵ��
	 * @return �ļ��д�С����λ���ֽ�
	 * @throws Exception
	 * @throws
	 */
	private static long getFileSize(File f) throws Exception {
		long size = 0;
		File flist[] = f.listFiles();
		for (int i = 0; i < flist.length; i++) {
			if (flist[i].isDirectory()) {
				size = size + getFileSize(flist[i]);
			} else {
				size = size + flist[i].length();
			}
		}
		return size;
	}
    
	/**
	 * ��ȡ��ǰϵͳʱ��
	 * @return ��ǰʱ���
	 */
	public static  String getTimeNow() {
		//��ȡ��ǰϵͳʱ�䲢��ϵͳʱ����Ϊ�ļ���������Ƭ�ļ�
		SimpleDateFormat  formatter  =   new  SimpleDateFormat("yyyyMMddHHmmss");       
		Date curDate= new Date(System.currentTimeMillis());//��ȡ��ǰʱ��       
		String str = formatter.format(curDate);
		return str;
	}
	
	/**
	 * ��ȡ��ǰϵͳʱ��--��׼��ʽ
	 * @return ��ǰʱ���
	 */
	public static  String getTimeNow2() {
		//��ȡ��ǰϵͳʱ�䲢��ϵͳʱ����Ϊ�ļ���������Ƭ�ļ�
		SimpleDateFormat  formatter  =   new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date curDate= new Date(System.currentTimeMillis());//��ȡ��ǰʱ��       
		String str = formatter.format(curDate);
		return str;
	}
	
	/**
	 * �ַ���ת����
	 * @return
	 * @throws ParseException 
	 * @throws Exception 
	 */
	public static Date getDateFromString(String str) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");    
		Date date= formatter.parse(str);//��ȡ��ǰʱ��       
		return date;
	}
	
	/**
	 * ����ת�ַ���
	 * @param curDate
	 * @return
	 */
	public static String getDateString(Date curDate){
		SimpleDateFormat  formatter  =   new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = formatter.format(curDate);
		return str;
	}
	
    
	/**
     * ��ȡ�ֻ���ʣ�����ROM
     */
    public static long getAvailableMem(Context context){
        ActivityManager  am  = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        MemoryInfo outInfo = new MemoryInfo();
        am.getMemoryInfo(outInfo);
        long availMem = outInfo.availMem;
        return availMem;
    }
	
    /*
  	 * MD5����
  	 */
      public static String getMD5Str(String str) {     
          MessageDigest messageDigest = null;          
          try {     
              messageDigest = MessageDigest.getInstance("MD5");     
              messageDigest.reset();     
              messageDigest.update(str.getBytes("UTF-8"));     
          } catch (NoSuchAlgorithmException e) {     
              System.out.println("NoSuchAlgorithmException caught!");     
              System.exit(-1);     
          } catch (UnsupportedEncodingException e) {     
              e.printStackTrace();     
          }     
       
          byte[] byteArray = messageDigest.digest();     
       
          StringBuffer md5StrBuff = new StringBuffer();     
          
          for (int i = 0; i < byteArray.length; i++) {                 
              if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)     
                  md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));     
              else     
                  md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));     
          }     
          //16λ���ܣ��ӵ�9λ��25λ
          return md5StrBuff.substring(8, 24).toString().toUpperCase();    
      }  
    
      /**
       * ��ȡ�豸mac��ַ
       * @return
       */
      private static String MAC =null;
      public static String getLocalMacAddress(Context context) {
	  	  final WifiManager wifi=(WifiManager)context.getSystemService(Context.WIFI_SERVICE);
		  if(wifi==null) return "";
		 
		  WifiInfo info=wifi.getConnectionInfo();
		 MAC=info.getMacAddress();
		                 
		  if(MAC==null&& !wifi.isWifiEnabled()) {
		    new Thread() {
		      @Override
		      public void run() {
		        wifi.setWifiEnabled(true);
		        for(int i=0;i<10;i++) {
		          WifiInfo _info=wifi.getConnectionInfo();
		          MAC=_info.getMacAddress();
		          if(MAC!=null) {
		        	  break;
		          }
		          try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        }
		        wifi.setWifiEnabled(false);
		      }
		    }.start();
		  }
		  return MAC.trim().toLowerCase();//Ĭ�Ϸ���Сд
	  	}
}

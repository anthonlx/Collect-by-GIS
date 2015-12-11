package com.esri.android.viewer.tools;

import java.io.File;
import java.io.IOException;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class sqliteHelper {

	static SQLiteDatabase mDb;
	static String DATABASE_NAME = com.esri.android.viewer.tools.SystemVariables.ConfigSqliteDB;//����ϵͳ���ݿ�����
	
	//�����ַ���
	static String sqlStrBookMark = "CREATE TABLE BOOKMARK (" +
			"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
			" NAME TEXT," +
			" EXTENT TEXT)";
	static String sqlStrRoutePath = "CREATE TABLE ROUTEPATH (" +
			"ID INTEGER PRIMARY KEY AUTOINCREMENT," +
			" NAME TEXT," +
			" ADDRESS TEXT, " +
			"TYPE TEXT)";
	static String sqlStrSyslog = "CREATE TABLE SYS_LOGS (" +
			"ID  INTEGER PRIMARY KEY AUTOINCREMENT," +
			"F_USERID  INTEGER NOT NULL," +
			"F_TIME  Date NOT NULL," +
			"F_ACTION  TEXT NOT NULL)";
	static String sqlStrBizLocation = "CREATE TABLE BIZ_LOCATIONS (" +
			"ID  INTEGER PRIMARY KEY AUTOINCREMENT," +
			"F_USERID  INTEGER NOT NULL," +
			"F_TIME  DATE," +
			"F_LONGITUDE  Number(9,6)," +
			"F_LATITUDE  Number(8,6)," +
			"F_ALTITUDE  Number(7,3)," +
			"F_AZIMUTH  Number(6,3)," +
			"F_SPEED  Number(6,3)," +
			"F_TASKID  Number," +
			"F_DEVICEID  Number,"+
			"F_STATE  Number"+")";
	static String sqlStrLocal_USERS = "CREATE TABLE Local_USERS (" +
			"ID  INTEGER PRIMARY KEY AUTOINCREMENT," +
			"F_USERID integer,F_USERNAME text,F_PASSWORD text,F_DEPARTMENT text,F_TELEPHONE text)";
	static String sqlStr_TaskDownload = "CREATE TABLE TASKDOWNLOAD (" +
			"F_TASKPACKAGENAME text PRIMARY KEY," +
			"F_USERID text ," +
			"F_TASKPACKAGEURL text,F_TASKPACKAGELOCALPATH text)";
	
	
	/**
	  * ����ϵͳ�������ݿ�
	  * @param path ���ݿ�洢·��
	  * @return
	  */
    public static boolean createConfigDB(String path)
    {
    	String dbPath=path; //+"/database";
    	File dbp=new File(dbPath);
    	File dbf=new File(dbPath+"/"+DATABASE_NAME);			                   
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
				mDb.execSQL(sqlStrBookMark);//������ǩ��
				mDb.execSQL(sqlStrRoutePath);//����·����
//				mDb.execSQL(sqlStrSyslog);//����ϵͳ��־   �޸� 2015-12-11  by David.Ocean 
				mDb.execSQL(sqlStrBizLocation);//����λ����Ϣ��
				mDb.execSQL(sqlStrLocal_USERS);//���������û���
				mDb.execSQL(sqlStr_TaskDownload);//����������Ϣ��
			} catch (Exception e) {
				Log.d("�����쳣", e.toString());				
			}
			return true;
	       }else
	       {	    	   
	    	   return false;
	       }
    }
    
    //�ж����ݿ��Ƿ����
    public static boolean IsDBCreate(String path)
    {
    	//�ж��ļ����Ƿ����,����������򴴽��ļ���
    	File file = new File(path);
    	return  file.exists();
    	
    }
	
}

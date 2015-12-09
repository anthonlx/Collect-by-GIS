package com.esri.android.viewer.tools;

import java.util.ArrayList;
import java.util.List;

import com.esri.android.viewer.ViewerActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.esri.android.viewer.widget.draw.WorkLogNode;

public class taskSqliteHelper {

	private static SQLiteDatabase mDb;
	private String dbPAth;
	private static String TABLE_NAME="TASK_LOCATIONS";
	static String sqlStrBizLocation = "CREATE TABLE "+TABLE_NAME+" (" +
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
	
	private static String WORKLOG_TABLE_NAME="TASK_WORKLOG";
	static String sqlStrWorkLog = "CREATE TABLE " + WORKLOG_TABLE_NAME + " ("+
			"ID  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"+
			"ADDTIME  TEXT,"+
			"LASTCHECKTIME  TEXT,"+
			"TABLENAME  TEXT,"+
			"TABLETYPE  TEXT,"+
			"LAYERITEMNAME  TEXT,"+
			"WORKEXTENT  TEXT,"+
			"LAYERITEMINDEX  TEXT,"+
			"WORKSTATE  TEXT,"+
			"REMARK  TEXT"+")";
	
	public taskSqliteHelper(String path){
		this.dbPAth = path;
	}
	
	/**
	 * ��ʼ��·����
	 */
	public void initLocationTable(){
	 	   try {
			mDb = SQLiteDatabase.openDatabase(this.dbPAth, null, 0);
		} catch (Exception e) {
			Log.d("�������ʧ��", e.toString());				
			return;
		}
 	   
		String sqlStr = "SELECT * FROM sqlite_master WHERE type='table' AND name='"+TABLE_NAME+"'";
	 	   Cursor cursor = mDb.rawQuery(sqlStr, null);
			while (cursor.moveToFirst()) {
				int num = cursor.getColumnCount();
				if(num>0) return;
			}
		
	 	 try {
				mDb.execSQL(sqlStrBizLocation);//����·����
	 	   } catch (Exception e) {
				Log.d("�����·�������쳣", e.toString());				
			}
	 	  mDb.close();
		}
	
	/**
	 * ��ʼ����������״̬��
	 */
	public void initWorkLogTable(){
	 	   try {
			mDb = SQLiteDatabase.openDatabase(this.dbPAth, null, 0);
		} catch (Exception e) {
			Log.d("�������ʧ��", e.toString());				
			return;
		}
 	   
		String sqlStr = "SELECT * FROM sqlite_master WHERE type='table' AND name='"+WORKLOG_TABLE_NAME+"'";
	 	   Cursor cursor = mDb.rawQuery(sqlStr, null);
			while (cursor.moveToFirst()) {
				int num = cursor.getColumnCount();
				if(num>0) return;
			}
		
	 	 try {
				mDb.execSQL(sqlStrWorkLog);//����������־��
	 	   } catch (Exception e) {
				Log.d("�������������־�����쳣", e.toString());				
			}
	 	  mDb.close();
		}
	
	

	/**
	 * �������������������־
	 * @param addtime
	 * @param latchecktime
	 * @param tablename
	 * @param tabletype
	 * @param layername
	 * @param layerindex
	 * @param workexist
	 * @param workstate
	 */
	public void insertWorkLogData(String addtime,String latchecktime,String tablename,String tabletype,String layername,String layerindex,String workexist,String workstate){
		String sqlStr = "INSERT INTO "+WORKLOG_TABLE_NAME+"(ADDTIME,LASTCHECKTIME,TABLENAME,TABLETYPE,LAYERITEMNAME,LAYERITEMINDEX,WORKEXTENT,WORKSTATE)"
				+ " VALUES ('"
				+ addtime
				+ "', '"
				+ latchecktime
				+ "', '"
				+ tablename
				+ "', '"
				+ tabletype
				+ "', '"
				+ layername
				+ "','"
				+ layerindex
				+ "','"
				+ workexist
				+ "','"
				+ workstate
				+ "')";
		  try {
				mDb = SQLiteDatabase.openDatabase(this.dbPAth, null, 0);
			} catch (Exception e) {
				Log.d("�������ʧ��", e.toString());				
				return;
			}
		 try {
				mDb.execSQL(sqlStr);//��������
	 	   } catch (Exception e) {
				Log.d("���������������־:���ݲ���ʧ��", e.toString());				
			}
	 	  mDb.close();
	}
	

	/**
	 * ����λ������
	 * @param id
	 * @param mac
	 * @param taskid
	 * @param time
	 * @param lng
	 * @param lat
	 * @param alt
	 * @param bear
	 * @param speed
	 * @param fState
	 */
 	public void insertLocationData(int id,String mac,int taskid,String time,double lng,double lat,double alt,double bear ,double speed,int fState){
		String sqlStr = "INSERT INTO "+TABLE_NAME+"(F_USERID,F_DEVICEID,F_TASKID,F_TIME,F_LONGITUDE,F_LATITUDE,F_ALTITUDE,F_AZIMUTH,F_SPEED,F_STATE)"
				+ " VALUES ('"
				+ id
				+ "', '"
				+ mac
				+ "', '"
				+ ViewerActivity.taskid
				+ "', '"
				+ time
				+ "','"
				+ lng
				+ "','"
				+ lat
				+ "','"
				+ alt
				+ "','"
				+ bear
				+ "','"
				+ speed 
				+ "','"
				+ fState
				+ "')";
		  try {
				mDb = SQLiteDatabase.openDatabase(this.dbPAth, null, 0);
			} catch (Exception e) {
				Log.d("�������ʧ��", e.toString());				
				return;
			}
		 try {
				mDb.execSQL(sqlStr);//��������
	 	   } catch (Exception e) {
				Log.d("�����λ�����ݱ����ݲ���ʧ��", e.toString());				
			}
	 	  mDb.close();
	}
 	
 	
 
 	/**
 	 * ��ȡ���������־�б�
 	 * @return
 	 */
 	public ArrayList<WorkLogNode> getWorkLogList(){
 		
 		ArrayList<WorkLogNode> items = new ArrayList<WorkLogNode>();
 		String sqlstr = "select * FROM TASK_WORKLOG ORDER BY ADDTIME DESC";
 		  try {
				mDb = SQLiteDatabase.openDatabase(this.dbPAth, null, 0);
			} catch (Exception e) {
				Log.d("�������ʧ��", e.toString());				
				return null;
			}
		 try {
			 Cursor cursor= mDb.rawQuery(sqlstr, null);
			 while (cursor.moveToNext()) {
     			int id  = cursor.getInt(cursor.getColumnIndex("ID"));
     			String addtime = cursor.getString(cursor.getColumnIndex("ADDTIME"));
     			String lastchecktime = cursor.getString(cursor.getColumnIndex("LASTCHECKTIME"));
     			String tablename = cursor.getString(cursor.getColumnIndex("TABLENAME"));
     			String tabletype = cursor.getString(cursor.getColumnIndex("TABLETYPE"));
     			String layername = cursor.getString(cursor.getColumnIndex("LAYERITEMNAME"));
     			String layerindex = cursor.getString(cursor.getColumnIndex("LAYERITEMINDEX"));
     			String extent = cursor.getString(cursor.getColumnIndex("WORKEXTENT"));
     			String statue = cursor.getString(cursor.getColumnIndex("WORKSTATE"));
     			String remark = cursor.getString(cursor.getColumnIndex("REMARK"));
     			
     			WorkLogNode node = new WorkLogNode();
     			node.id = id;
     			node.addTime = addtime;
     			node.lastCheckTime = lastchecktime;
     			node.tableName = tablename;
     			node.tableType = tabletype;
     			node.layerItemName = layername;
     			node.layerItemIndex = layerindex;
     			node.workExtent = extent;
     			node.workStatue = statue;
     			node.remark = remark;	
     			
     			items.add(node);
			}
				
	 	   } catch (Exception e) {
				Log.d("���������������־:�����б��ȡʧ��", e.toString());				
			}
	 	  mDb.close();
 		
 		
		return items;
 		
 	}
	
 	
 	/**
 	 * ɾ�����������־
 	 */
 	public boolean delWorkLogByID(int id){
 		
 		  try {
 				mDb = SQLiteDatabase.openDatabase(this.dbPAth, null, 0);
 			} catch (Exception e) {
 				Log.d("�������ʧ��", e.toString());
 			}
 			String sqlStr = "DELETE FROM "+WORKLOG_TABLE_NAME+" WHERE ID = "+id;
 		 	 try {
 					mDb.execSQL(sqlStr);//����������־��
 		 	   } catch (Exception e) {
 					Log.d("�������������־��ɾ���쳣", e.toString());	
 					return false;
 				}
 		 	  mDb.close();
		return true;
 	}
 	
 	/**
 	 * �������������־ֵ
 	 */
 	public boolean updateWorkLogByID(int id,String key,String value){

		  try {
				mDb = SQLiteDatabase.openDatabase(this.dbPAth, null, 0);
			} catch (Exception e) {
				Log.d("�������ʧ��", e.toString());
			}
			String sqlStr = "UPDATE "+WORKLOG_TABLE_NAME+" SET "+key+"='"+value+"' WHERE ID = "+id;
		 	 try {
					mDb.execSQL(sqlStr);//����������־��
		 	   } catch (Exception e) {
					Log.d("�������������־����³ɹ�", e.toString());	
					return false;
				}
		 	  mDb.close();
		return true;
 	}
}

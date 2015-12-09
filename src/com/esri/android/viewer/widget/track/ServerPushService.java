package com.esri.android.viewer.widget.track;

import java.util.Date;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

public class ServerPushService extends Service {

	private static String dbpath; 
	private static String webService; 
	
	@Override  
    public IBinder onBind(Intent intent) {  
        return null;  
    }  
    @Override  
    public int onStartCommand(Intent intent, int flags, int startId) {   
    	//��ȡ��ֵ
    	dbpath=intent.getStringExtra("dbpath");
    	webService = intent.getStringExtra("service");
    	//�����߳�  
        pushThread thread = new pushThread();  
        thread.start();  
        return super.onStartCommand(intent, flags, startId);  
    }  
  
    //����״̬  
    public boolean isCanRunning = true;  
    
    /*** 
     * �ӷ���˻�ȡ��Ϣ 
     */  
    class pushThread extends Thread{  
        @Override  
        public void run() {  
            while(isCanRunning){  
                try {  
                    //��Ϣ10��  
                    Thread.sleep(10000);  
                    updateData();//�ϴ�����
                    //Log.v("�ϴ�����", "test");
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }     
        }  
    }  
   

    /**
	 * �ϴ����ݵ�������
	 */
	private void updateData() {
         //��������Ƿ�ͨ��-->��ȡ�����б�-->�ϴ����ݿ�������-->ɾ�����ϴ���������
		boolean isonline =com.esri.android.viewer.tools.sysTools.isConnected(getApplicationContext());
		if(isonline){
			SQLiteDatabase mDb = SQLiteDatabase.openDatabase(dbpath, null, 0);
			Cursor cursor = mDb.rawQuery("select * from "+"BIZ_LOCATIONS", null);
			if(cursor.getCount()==0) return;
			while (cursor.moveToNext()) {
				int id = cursor.getInt(cursor.getColumnIndex("ID"));
				String deviceid = cursor.getString(cursor.getColumnIndex("F_DEVICEID"));
				String time = cursor.getString(cursor.getColumnIndex("F_TIME"));
				int userid = cursor.getInt(cursor.getColumnIndex("F_USERID"));
				int taskid = cursor.getInt(cursor.getColumnIndex("F_TASKID"));
				double lon= cursor.getDouble(cursor.getColumnIndex("F_LONGITUDE"));
				double lat= cursor.getDouble(cursor.getColumnIndex("F_LATITUDE"));
				double alt= cursor.getDouble(cursor.getColumnIndex("F_ALTITUDE"));
				double azi= cursor.getDouble(cursor.getColumnIndex("F_AZIMUTH"));
				double speed= cursor.getDouble(cursor.getColumnIndex("F_SPEED"));
				int state = cursor.getInt(cursor.getColumnIndex("F_STATE"));
				boolean is =pushToService(userid,taskid,deviceid,time,lon,lat,alt,azi,speed,state);
				if(is) mDb.execSQL("DELETE FROM "+"BIZ_LOCATIONS" +" WHERE ID="+id);
			}
			mDb.close();			
		}
     }
	
	private boolean pushToService(int userid,int taskid,String deviceid, String time,double lon, double lat,
            double alt, double azi, double speed, int state) {
	    try{
	    	String NameSpace="http://tempuri.org/";//�����ռ�
	    	String MethodName="UploadLocation";//Ҫ���õ�webService����
	    	String soapAction=NameSpace+MethodName;
			SoapObject request=new SoapObject(NameSpace,MethodName);//NameSpace
			request.addProperty("userId",userid);
			request.addProperty("taskId",taskid);
			request.addProperty("deviceId",deviceid);
//			Date d = com.esri.android.viewer.tools.sysTools.getDateFromString(time);
//			String t = com.esri.android.viewer.tools.sysTools.getDateString(d);
			request.addProperty("time",time);
			request.addProperty("longitude",String.valueOf(lon));
			request.addProperty("latitude", String.valueOf(lat));
			request.addProperty("altitude", String.valueOf(alt));
			request.addProperty("azimuth", String.valueOf(azi));
			request.addProperty("speed", String.valueOf(speed));	  
			request.addProperty("state", state);	  
			SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.dotNet=true;//����.NET��webservice
			envelope.setOutputSoapObject(request);
			HttpTransportSE ht=new HttpTransportSE(webService);
			ht.call(soapAction, envelope);//����call����������webservice
			if(envelope.getResponse()!=null){
				SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
				//���Ҫ���ض��󼯺ϣ��ڷ���˿��Խ�����򼯺����л���json�ַ������أ�����ٷ����л��ɶ���򼯺�
				Log.v("λ���ϴ��������󷵻سɹ�", response.toString());
				if("true".equals(response.toString())) {
					return true;	
				}   				
			}
		}catch(Exception e){
			//Toast.makeText(getApplicationContext(),"λ���ϴ�ʧ�ܣ�"+e.getMessage(), Toast.LENGTH_SHORT).show();	    	
			Log.v("λ���ϴ����ݴ����쳣", e.toString());
//         String msg = "λ���ϴ�ʧ�ܣ��������缰λ�÷����ַ���ã�" + e.toString();							 
//		       	Intent it =new Intent(ServerPushService.this,ServiceDialogActivity.class);
//				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				Bundle b=new Bundle(); 
//	            b.putString("msg", msg);  
//	            it.putExtras(b); 
//				startActivity(it);
		}	
		return false;
	}
    
}

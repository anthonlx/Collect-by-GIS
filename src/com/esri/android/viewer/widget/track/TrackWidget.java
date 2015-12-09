package com.esri.android.viewer.widget.track;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.esri.android.viewer.BaseWidget;
import com.esri.android.viewer.Log;
import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerActivity;
import com.esri.android.viewer.ViewerApp;
import com.esri.android.viewer.tools.taskSqliteHelper;
import com.esri.android.viewer.widget.GPSWidget;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

public class TrackWidget extends BaseWidget {
	private static final String[] timerls={"1��","3��","5��","10��","30��","1����","5����","10����"};
	protected static final int F_STATE = 0;  //λ�õ�״̬��Ĭ��Ϊ·��
	private static int timer = 10;//Ĭ��Ƶ��Ϊ10s
	private View mToolbarView;
	private Spinner timerspinner;//�ɼ�Ƶ��
	private  Switch locswitch; //λ�òɼ�����
	private static  EditText txtInfo;//��Ϣ�������
	
   public static  LocationManager loctionManager;//����LocationManager����
   public static String provider =null;//λ���ṩ��
    
	private static String ConfigDBPath="";// ����ϵͳ���ݿ�·��
	private static String MAC =null;
	
	private static String TaskDBPath = "";
	
	@Override
	public void active() {
		super.showToolbar(mToolbarView);
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		super.setAutoInactive(false);
		MAC = com.esri.android.viewer.tools.sysTools.getLocalMacAddress(TrackWidget.super.context);
		//TrackWidget.this.showMessageBox(MAC);
		//��ȡϵͳ���ݿ�·��
		ViewerApp appState = ((ViewerApp)super.context.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		ConfigDBPath = path.systemConfigFilePath.toString()+"/"+ com.esri.android.viewer.tools.SystemVariables.ConfigSqliteDB;
		
		LayoutInflater inflater = LayoutInflater.from(super.context);
		mToolbarView = inflater.inflate(R.layout.esri_androidviewer_track,null);
	
		timerspinner =(Spinner)mToolbarView.findViewById(R.id.esri_androidviewer_track_timerspinner);
		//����ѡ������ArrayAdapter��������  
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(TrackWidget.this.context,android.R.layout.simple_spinner_item,timerls);         
		//���������б�ķ��  
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);            
		//��adapter ��ӵ�spinner��  
		timerspinner.setAdapter(adapter);  
		//����¼�Spinner�¼�����    
		timerspinner.setOnItemSelectedListener(new SpinnerSelectedListener());            
		//����Ĭ��ֵ  
		timerspinner.setVisibility(View.VISIBLE);  
		timerspinner.setSelection(3);//����Ĭ��10s�ɼ�һ��
		
		txtInfo =  (EditText)mToolbarView.findViewById(R.id.esri_androidviewer_track_infoText);
		//txtInfo.setText("���ڶ�λ��ǰλ��...");
		
		locswitch = (Switch)mToolbarView.findViewById(R.id.esri_androidviewer_track_locswitch);
		locswitch.setOnCheckedChangeListener(new locOnCheckedChangeListener());
		locswitch.setChecked(true);
			
		super.create();
		
	    intiLocationManager(timer);//��ʼ��loctionManager

//		//����λ�÷����̨����
//		Intent intent = new Intent(TrackWidget.this.context,ServerPushService.class);
//		intent.putExtra("dbpath", ConfigDBPath);
//		intent.putExtra("service", getTrackServiceUrl());
//		//����startService����һֱ�ں�̨���в��۳����Ƿ�ر�
//		TrackWidget.this.context.startService(intent);		
		
		TaskDBPath = ViewerActivity.taskpath+"/"+ViewerActivity.taskname;
	}
	   
	/**
	 * ��ʼ��loctionManager
	 */
	private void intiLocationManager(int t) {
		try {
			String contextService = Context.LOCATION_SERVICE;
			//ͨ��ϵͳ����ȡ��LocationManager����
			loctionManager = (LocationManager) TrackWidget.this.context
					.getSystemService(contextService);
			//ʹ�ñ�׼���ϣ���ϵͳ�Զ�ѡ����õ����λ���ṩ�����ṩλ��
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);//�߾���
			criteria.setAltitudeRequired(true);//Ҫ�󺣰�
			criteria.setBearingRequired(true);//Ҫ��λ
			criteria.setCostAllowed(true);//�����л���
			criteria.setSpeedRequired(true);
			criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);//���� 
			//�ӿ��õ�λ���ṩ���У�ƥ�����ϱ�׼������ṩ��
			provider = loctionManager.getBestProvider(criteria, true);
			loctionManager.requestLocationUpdates(provider, t * 1000, 0,
					mListener);
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}
	
	/**
	 * �ر�λ�ü��
	 */
	public static void delLocationManager(){
		try {
			loctionManager.removeUpdates(mListener);
			loctionManager = null;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public final static LocationListener mListener = new LocationListener()
	{
		public void onLocationChanged(Location location) {
			   try {				 				   
				    locaDate loc =null;
				    if(location!=null){
				    	loc=new locaDate();
				    	loc.lat=location.getLatitude();
						loc.lng=location.getLongitude();
						loc.alt = location.getAltitude();
						loc.bear = location.getBearing();			
						loc.speed = location.getSpeed();
						Date lo_time = new java.util.Date(location.getTime());
					    loc.time = com.esri.android.viewer.tools.sysTools.getDateString(lo_time);
					    loc.mac =MAC;
				   }
					
					if (loc != null) {
						String latLongString = "ʱ�䣺" + loc.time 
								+ "\nγ�ȣ�" + loc.lat 
								+ "\n���ȣ�"+ loc.lng 
								+ "\n�̣߳�" + loc.alt 
								+ "\n��λ��"+ loc.bear 
								+ "\n�ٶȣ�" + loc.speed;
						// TrackWidget.super.showMessageBox(latLongString);
						txtInfo.setText(latLongString);
						String sqlStr = "INSERT INTO BIZ_LOCATIONS(F_USERID,F_DEVICEID,F_TASKID,F_TIME,F_LONGITUDE,F_LATITUDE,F_ALTITUDE,F_AZIMUTH,F_SPEED,F_STATE)"
								+ " VALUES ('"
								+ ViewerActivity.userid
								+ "', '"
								+ loc.mac
								+ "', '"
								+ ViewerActivity.taskid
								+ "', '"
								+ loc.time
								+ "','"
								+ loc.lng
								+ "','"
								+ loc.lat
								+ "','"
								+ loc.alt
								+ "','"
								+ loc.bear
								+ "','"
								+ loc.speed 
								+ "','"
								+ F_STATE
								+ "')";
						
						//����һ�ݵ��������
						SaveLocToTaskDB(ViewerActivity.userid, loc.mac,ViewerActivity.taskid, loc.time, loc.lng, loc.lat, loc.alt,loc.bear,loc.speed ,F_STATE);
						
						try {
							SQLiteDatabase mDb = SQLiteDatabase.openDatabase(ConfigDBPath, null, 0);
							mDb.execSQL(sqlStr);
							mDb.close();
						} catch (Exception e) {
							//TODO Auto-generated catch block
							txtInfo.setText(e.toString());
						}
					}
				} catch (Exception e) {
					// TODO: GPS�쳣
					txtInfo.setText("���ݲɼ��쳣,����GPS���ü��������ӣ�"+e.toString());
				}
		}
		
		public void onProviderDisabled(String arg0) {
			// TODO Auto-generated method stub
			Log.d("onProviderDisabled", "come in");
		}

		public void onProviderEnabled(String arg0) {
			// TODO Auto-generated method stub
			Log.d("onProviderEnabled", "come in");
		}

		public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
			// TODO Auto-generated method stub
			
		}
		
	    class locaDate {
	  	  private double lat ;
	  	  private double lng;
	  	  private double alt;
	  	  private double bear;
	  	  private double speed;
	  	  private String time;
	  	  private String mac;	  
	  	}
		
	};
	
	/**
	 * ��ȡλ�÷����ַ
	 * @return
	 */
	private String getTrackServiceUrl() {
		String result ="";
		try {  
			 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();  
			 DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();  
			 Document doc = docBuilder.parse(getFileInputStream());
			 Element root = doc.getDocumentElement();  
			 NodeList nodeList2 = root.getElementsByTagName("trackservice");
			 result = nodeList2.item(0).getTextContent();//�������ļ���ȡ�����ַ
			 } catch (Exception e) { 
				 Toast.makeText(TrackWidget.this.context,"λ�÷����ַ��ȡ����"+e.toString(), Toast.LENGTH_SHORT).show();
			 }
		return result;
	}

	/**
	 * ���걣�浽�������ݿ�һ��
	 * @param userid
	 * @param mac2
	 * @param taskid
	 * @param time
	 * @param lng
	 * @param lat
	 * @param alt
	 * @param bear
	 * @param speed
	 * @param fState
	 */
 	protected static void SaveLocToTaskDB(int userid, String mac, int taskid,
			String time, double lng, double lat, double alt, double bear,
			double speed, int fState) {
		// TODO Auto-generated method stub
	    taskSqliteHelper helper = new taskSqliteHelper(TaskDBPath);    
	    helper.insertLocationData(userid, mac, taskid, time, lng, lat, alt, bear, speed, F_STATE);
	    
	}

	/**
 	 * ��ȡxml�ļ�������
 	 * @return
 	 * @throws IOException 
 	 */
	private InputStream getFileInputStream() throws IOException
	{
		ViewerApp appState = ((ViewerApp)TrackWidget.this.context.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		String sysFilePath= path.systemConfigFilePath+"/" +"sys.xml";
		InputStream is=null;
		//is = this.getAssets().open("sys.xml");
		is = new FileInputStream(sysFilePath);
		return is;
	}

	public class locOnCheckedChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// TODO �Զ����ɵķ������
			if(isChecked){
				timerspinner.setEnabled(false);//������
				txtInfo.setText("���ڶ�λ��ǰλ��...");
				intiLocationManager(timer);//��ʼ��LocationManager		
			}else{
				timerspinner.setEnabled(true);
				txtInfo.setText("");
				delLocationManager();//����LocationManager
			}
		}
	}
	
	class SpinnerSelectedListener implements OnItemSelectedListener{   
	    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {  
	    		switch(arg2)//����ʱ��Ƶ��
	    		{
		    		case 0:
		    			timer =1;
		    			break;
		    		case 1:
		    			timer =3;
		    			break;
		    		case 2:
		    			timer =5;
		    			break;
		    		case 3:
		    			timer =10;
		    			break;
		    		case 4:
		    			timer = 30;
		    			break;
		    		case 5:
		    			timer = 60;
		    			break;
		    		case 6:
		    			timer = 300;
		    			break;
		    		case 7:
		    			timer = 600;
		    			break;
	    		}
	        }  
	  
	        public void onNothingSelected(AdapterView<?> arg0) {  
	        	
	        }  
	  } 
		
}

 

package com.esri.android.viewer.widget.samplepoint;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.esri.android.viewer.Log;
import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerApp;
import com.esri.android.viewer.widget.draw.localspatialReference;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.view.Menu;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public  class SamplePointCameraActivity extends Activity {

	protected static final String TAG = "SamplePointCameraActivity";
	private CameraPreview preview;
	private Camera camera;
	private Activity act;
	private Context ctx;
	private TextView txtloc ;//��γ��
	private TextView txtori ;//�ⷽλԪ��
	private TextView txttime;//ϵͳʱ��
	private ImageButton imgbtnCamera;//����
	private static String picPath;//��Ƭ·��
    public static  LocationManager loctionManager;//����LocationManager����
    public static String provider =null;//λ���ṩ��
    public double mAccuracy = -1;//λ��ƽ��ˮƽ����ˮƽ
    private double mLongitude = -1;//����
	private double mLatitude = -1;//γ��
	private String mLocTime = null;//��λʱ��
	private double mElevation = -1;//�߳�
	private int mSateNum = 0;//��������
	private double mAzim =-1 ;//��λ��
	private double mAzimp  =-1;//��λ��׼ȷ�̶�
	private String mProvider =null;//λ���ṩ��
	private double mTILT = -1;//������
	private double mROLL=-1;//�����
	public static Location syslocation = null;//ϵͳ��ǰλ��
	final Handler handler = new Handler(){  
	      public void handleMessage(Message msg) {  
	          switch (msg.what) {      
	              case 1:    
	            	  String str = com.esri.android.viewer.tools.sysTools.getTimeNow2();
	            	  txttime.setText(str);
	                  break;      
	              }      
	              super.handleMessage(msg);  
	         }    
	     };  
	TimerTask task = new TimerTask(){  
	     public void run() {  
	         Message message = new Message();      
	         message.what = 1;      
	         handler.sendMessage(message);    
	      }  
	   };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewerApp appState = ((ViewerApp)this.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		picPath= path.samplepointPath+"/"+com.esri.android.viewer.tools.SystemVariables.PointPackageDirectory+"/"
				+com.esri.android.viewer.tools.SystemVariables.PicturesDirectory;
		
		ctx = this;
		act = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);// ��������// ����ȫ�� 
    	// ���ú��� 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); 
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_sample_point_camera);
		
		txtloc =(TextView)findViewById(R.id.activity_sample_point_camera_txtlocation);
		txtloc.setTextSize(10);
		txtori =(TextView)findViewById(R.id.activity_sample_point_camera_txtorientation);
		txtori.setText(Orientation.getinfo());
		txtori.setTextSize(10);
		Orientation.intiOrientation(ctx,txtori);//��ʼ���ⷽλԪ��	
		
		txttime =(TextView)findViewById(R.id.activity_sample_point_camera_timeinfo);
		txttime.setTextSize(10);
		Timer timer = new Timer(true);
		timer.schedule(task,1000, 1000); //��ʱ1000ms��ִ�У�1000msִ��һ��
		//timer.cancel(); //�˳���ʱ��
		
		preview = new CameraPreview(this, (SurfaceView)findViewById(R.id.surfaceView));
		preview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		((FrameLayout) findViewById(R.id.preview)).addView(preview);
		preview.setKeepScreenOn(true);
		
		imgbtnCamera = (ImageButton)findViewById(R.id.activity_sample_point_camera_btnCamera);
		imgbtnCamera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				camera.takePicture(shutterCallback, rawCallback, jpegCallback);	
			}
		});
		imgbtnCamera.setOnLongClickListener(new OnLongClickListener(){
			@Override
			public boolean onLongClick(View arg0) {
				camera.autoFocus(new AutoFocusCallback(){
				@Override
				public void onAutoFocus(boolean arg0, Camera arg1) {
					camera.takePicture(shutterCallback, rawCallback, jpegCallback);
				}
			});
				return true;
			}
		});
		imgbtnCamera.setEnabled(false);//Ĭ�ϲ�����
		intiLocationManager(1);//��ʼ����λ
		Toast.makeText(this,"��λ�ɹ��������չ��ܣ�", Toast.LENGTH_SHORT).show();
	}
    
	/**
	 * ��ʼ��loctionManager
	 */
	private void intiLocationManager(int t) {
		String contextService=Context.LOCATION_SERVICE;
	    //ͨ��ϵͳ����ȡ��LocationManager����
	    loctionManager=(LocationManager) this.getSystemService(contextService);  
	    //ʹ�ñ�׼���ϣ���ϵͳ�Զ�ѡ����õ����λ���ṩ�����ṩλ��
	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);//�߾���
	    criteria.setAltitudeRequired(true);//Ҫ�󺣰�
	    criteria.setBearingRequired(true);//Ҫ��λ
	    criteria.setCostAllowed(true);//�����л���	
	    criteria.setPowerRequirement(Criteria.POWER_MEDIUM);//����   
	     //�ӿ��õ�λ���ṩ���У�ƥ�����ϱ�׼������ṩ��
	     provider = loctionManager.getBestProvider(criteria, true);
		loctionManager.requestLocationUpdates(provider, t*1000, 0, mListener);
		loctionManager.addGpsStatusListener(statusListener);

	}
	
	/**
	 * �ر�λ�ü��
	 */
	public  void delLocationManager(){
		try {
			loctionManager.removeUpdates(mListener);
			loctionManager = null;
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public  LocationListener mListener = new LocationListener()
	{
		public void onLocationChanged(Location location) {
			if(location != null){		
				try
				{
					syslocation = location;
					Date lo_time = new java.util.Date(syslocation.getTime());
					mLocTime = com.esri.android.viewer.tools.sysTools.getDateString(lo_time);
					mLatitude = syslocation.getLatitude();
					mLongitude = syslocation.getLongitude();
					mElevation = syslocation.getAltitude();
					mAzim = syslocation.getBearing();
					mProvider =  syslocation.getProvider();
					mAccuracy = syslocation.getAccuracy();
					DecimalFormat df = new DecimalFormat( "0.000000");  
					DecimalFormat df2 = new DecimalFormat( "0.00");  
					String str ="ʱ�䣺"+  mLocTime
									+"\n���ȣ�"+ df.format(mLongitude) 
									+"\nγ�ȣ�"+df.format(mLatitude)
									+"\n�̣߳�"+df.format(mElevation)
									//+"\n��λ�ǣ�"+df.format(mAzim)
									+"\n����������"+mSateNum
									+"\nλ���ṩ����"+mProvider
									+"\n����ˮƽ��"+df2.format(mAccuracy);
					txtloc.setText(str);				
					imgbtnCamera.setEnabled(true);//��λ�ɹ������
				}
				catch(Exception e)
				{
					mLatitude = 0;
					mLongitude = 0;
					syslocation = null;
					imgbtnCamera.setEnabled(false);//��λ�ɹ������
					e.printStackTrace();			
				}		
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
	};

	/**
	 * ����״̬������
	 */
	private List<GpsSatellite> numSatelliteList = new ArrayList<GpsSatellite>(); // �����ź�
	
	private  GpsStatus.Listener statusListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) { // GPS״̬�仯ʱ�Ļص�����������
			GpsStatus status = loctionManager.getGpsStatus(null); //ȡ��ǰ״̬
			mSateNum = updateGpsStatus(event, status);
		}
		
		private int updateGpsStatus(int event, GpsStatus status) {
			int num = 0;
			if (status == null) {
				num = 0;
			} else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
				int maxSatellites = status.getMaxSatellites();
				Iterator<GpsSatellite> it = status.getSatellites().iterator();
				numSatelliteList.clear();
				int count = 0;
				while (it.hasNext() && count <= maxSatellites) {
					GpsSatellite s = it.next();
					numSatelliteList.add(s);
					count++;
				}
				num =  numSatelliteList.size();
			}
			return num;
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		//      preview.camera = Camera.open();
		camera = Camera.open();
		camera.startPreview();
		preview.setCamera(camera);
	}

	@Override
	protected void onPause() {
		if(camera != null) {
			camera.stopPreview();
			preview.setCamera(null);
			camera.release();
			camera = null;
		}
		Orientation.unregListener();
		super.onPause();
	}

	private void resetCam() {
		camera.startPreview();
		preview.setCamera(camera);
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera c) {
			FileOutputStream outStream = null;
			try {
				mAzim = Orientation.getAzim();
				mTILT = Orientation.getTILT();
				mROLL = Orientation.getROLL();
						
				Date curDate= new Date(System.currentTimeMillis());//��ǰʱ�� 
				String time = com.esri.android.viewer.tools.sysTools.getDateString(curDate);
				String name = CommonTools.createFileName(mLongitude,mLatitude,mAzim,curDate);
				String fileName = picPath +"/"+name+".jpg";

				Intent intent = new Intent(SamplePointCameraActivity.this, SamplePointCameraEndActivity.class);    
				Bundle bundle = new Bundle();  
				bundle.putString("picURL", fileName);//ͼƬ��ַ
				bundle.putString("PHID", name);//ͼƬID
				bundle.putString("FILE", name);//�ļ�ID
				bundle.putString("PHTM", time);//����ʱ��
				bundle.putString("LONG", String.valueOf(mLongitude));//����
				bundle.putString("LAT", String.valueOf(mLatitude));//γ��
				bundle.putString("MMODE", mProvider);//��λ����
				bundle.putString("ALT", String.valueOf(mElevation));//�����߳�	
				bundle.putString("DOP", String.valueOf(mAccuracy));//λ�ö�λˮƽ����ˮƽ
				bundle.putString("SAT", String.valueOf(mSateNum));//��λʱ�۲⵽����������
				bundle.putString("AZIM", String.valueOf(mAzim));//��Ƭ��λ��
				bundle.putString("AZIMR", "G");//��Ƭ��λ�ǵĲο�����-�ű�
				bundle.putString("TILT", String.valueOf(mTILT));//���������
				bundle.putString("ROLL", String.valueOf(mROLL));//��������
				bundle.putString("FOCAL", name);//35m��Ч����		--------------------------
		   
			    outStream = new FileOutputStream(fileName);
				outStream.write(data);
				outStream.close();
				intent.putExtras(bundle);
			    SamplePointCameraActivity.this.startActivity(intent);	
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			Log.d(TAG, "onPictureTaken - jpegд��ɹ�");
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sample_point, menu);
		return false;
	}

}

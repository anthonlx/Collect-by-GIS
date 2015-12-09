package com.esri.android.viewer.widget.samplepoint;

import java.text.DecimalFormat;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

public class Orientation {
	
	static  String Orientation ="";
	private static  double ori_x ;
	private static double ori_y ;
	private static double ori_z ;
	private  static SensorManager sensorMgr;
	private  static  SensorEventListener lsn;
	
	/**
	 * ��ȡ�ֻ���ǰ״̬��Ϣ����ǣ������ǣ�������ֵ��Orientation
	 */
	 public static void intiOrientation(Context context,final TextView txtori)
		{
			   sensorMgr = (SensorManager) context.getSystemService(context.SENSOR_SERVICE); 
			 	Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		        /*TYPEע��
		        		int TYPE_ACCELEROMETER ���ٶ� 
		                int TYPE_ALL �������ͣ�NexusOneĬ��Ϊ ���ٶ� 
		                int TYPE_GYROSCOPE ��ת��(�����̫��) 
		                int TYPE_LIGHT ���߸�Ӧ
		                int TYPE_MAGNETIC_FIELD �ų� 
		                int TYPE_ORIENTATION ����ָ���룩�ͽǶ� 
		                int TYPE_PRESSUR ѹ���� 
		                int TYPE_PROXIMITY ���룿��̫�� 
		                int TYPE_TEMPERATURE �¶���
		        */
		        lsn = new SensorEventListener() {
		            public void onSensorChanged(SensorEvent e) {
		                 double   x = e.values[SensorManager.AXIS_X-1];  //��λ��
		                 double   y = e.values[SensorManager.AXIS_Y-1];  //��б��
		                 double   z = e.values[SensorManager.AXIS_Z-1]; //��ת��	               
//		                 x ��������ֻ���ˮƽ������Ϊ��
//		                 y ��������ֻ���ˮƽ��ֱ����ǰΪ��
//		                 z ��������ֻ��Ŀռ䴹ֱ������յķ���Ϊ��������ķ���Ϊ��
//		                 ����ԭ�����ֻ���Ļ�����½š� 
//		                 ����ǵĶ������ֻ�y�� ˮƽ���ϵ�ͶӰ �� ��������ļнǡ� ��ֵ�÷�Χ�� 0 ~ 359 ����0=North, 90=East, 180=South, 270=West��
//		                 ��б�ǵĶ������ֻ�y�� ��ˮƽ��ļн� ���ֻ�z����y�᷽���ƶ�Ϊ�� ,ֵ�÷�Χ�� -180 ~ 180��
//		                 ��ת�ǵĶ������ֻ�x�� ��ˮƽ��ļн� ���ֻ�x���뿪z�᷽��Ϊ���� ֵ�÷�Χ�� -90 ~ 90��
		                 DecimalFormat df = new DecimalFormat( "0.000000");  
		                 ori_x = Double.valueOf(df.format(x));
		                 ori_y = Double.valueOf(df.format(y));
		                 ori_z = Double.valueOf(df.format(z));
		                 String  orientStr ="��λ�ǣ�"+ori_x+"\n"+"�����ǣ�"+ori_z+"\n"+"����ǣ�"+ori_y;
		                 Orientation = orientStr;     
		                 txtori.setText(Orientation);//�����ⷽλԪ��ֵ
		            }
		            public void onAccuracyChanged(Sensor s, int accuracy) {
		            }
		        };
		        //ע��listener�������������Ǽ���������
		        sensorMgr.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		    
		        /*
		        SENSOR_DELAY_FASTEST �����������Ȼ������
		        SENSOR_DELAY_GAME ��Ϸ��ʱ�������������һ��������͹��ˣ�����һ�����ѿ�������
		        SENSOR_DELAY_NORMAL �Ƚ�����
		        SENSOR_DELAY_UI �����ģ��������Ǻ���ݵ�����
		        */	
		}
	 
	 /**
	  * ȡ������
	  */
	 public static void unregListener(){
		 sensorMgr.unregisterListener(lsn);
	 }
	
	 public static String  getinfo(){
		 return Orientation;
	 }
	 
	 /**
	  * ��ȡ�����λ��
	  * @return
	  */
	public static double getAzim() {
		// TODO Auto-generated method stub
		return ori_x;
	}
	 
	 /**
	  * ��ȡ���������
	  * @return
	  */
	 public static double getTILT(){
		 return ori_z;//Ĭ�Ϻ�����yz�ߵ�
	 }
	 
	 /**
	  * ��ȡ��������
	  * @return
	  */
	 public static double getROLL(){
		 return ori_y;//Ĭ�Ϻ�����yz�ߵ�
	 }
}

package com.esri.android.viewer.widget;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import com.esri.android.map.GraphicsLayer;

import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.event.OnStatusChangedListener.STATUS;
import com.esri.android.viewer.BaseWidget;
import com.esri.android.viewer.Log;
import com.esri.android.viewer.R;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.symbol.Symbol;

public class GPSWidget extends BaseWidget 
{
	protected static final double SEARCH_RADIUS = 5;
	private double mLongitude = 0.0;
	private double mLatitude = 0.0;
	private boolean mHasGPS = false;
	private GraphicsLayer mGraphicsLayer = new GraphicsLayer();
	private LocationManager mLocationManager;
	@Override
	public void active() 
	{
		if(!isOpenGPSsetting()){
			super.showMessageBox("���鶨λ�����Ƿ���!");
			//alertMessageBox("���鶨λ�����Ƿ���!");
			openGPSSettings();
		}
		else
		{
			//super.showMessageBox("����豸ӵ��GPSģ�飡");	
			locator();
		}
	
		
	}
	private void locator()
	{
		Criteria cri = new Criteria();
		cri.setAccuracy(Criteria.ACCURACY_FINE);//�߾���
		cri.setAltitudeRequired(false);   
		cri.setBearingRequired(false);   
	    cri.setSpeedRequired(true);
		cri.setCostAllowed(true);   
		cri.setPowerRequirement(Criteria.NO_REQUIREMENT);
		super.showMessageBox("��λ��...");
		//alertMessageBox("��λ��...");
		try
		{
			if(mLocationManager == null)
				mLocationManager = (LocationManager)super.context.getSystemService(Context.LOCATION_SERVICE);
			String best = mLocationManager.getBestProvider(cri, true);
			mLocationManager.requestLocationUpdates(best, 1000, 2, mListener);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			super.showMessageBox("��λʧ�ܣ�");
			//alertMessageBox("��λʧ�ܣ�");	
		}
	}
	private void openGPS(boolean open)
	{
		try
		{
			mLocationManager.setTestProviderEnabled("gps",open);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		boolean ishasgps =true;//Ĭ��gpsһֱ����
		if(ishasgps)
		{
			mHasGPS = true;
			super.mapView.addLayer(mGraphicsLayer);
			mLocationManager = (LocationManager)super.context.getSystemService(Context.LOCATION_SERVICE);
			openGPS(true);
		}
		else
		{
			mHasGPS = false;
			super.setAutoInactive(true);
		}
		super.create();
	}
	
	@Override
	public void inactive() {
		if(mHasGPS)
		{
			mLocationManager.removeUpdates(mListener);
			mLocationManager = null;
			mGraphicsLayer.removeAll();
			openGPS(false);
		}
		super.inactive();
	}	
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		try
		{
			if(mLocationManager != null) 
				mLocationManager.setTestProviderEnabled("gps",false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		super.finalize();
	}
	
	private boolean isOpenGPSsetting()
	{
		try
		{
		    final LocationManager mgr = (LocationManager)super.context.getSystemService(Context.LOCATION_SERVICE);
		    // ͨ��GPS���Ƕ�λ����λ������Ծ�ȷ���֣�ͨ��24�����Ƕ�λ��������Ϳտ��ĵط���λ׼ȷ���ٶȿ죩 
		    boolean gps = mgr.isProviderEnabled(LocationManager.GPS_PROVIDER); 
		    // ͨ��WLAN���ƶ�����(3G/2G)ȷ����λ�ã�Ҳ����AGPS������GPS��λ����Ҫ���������ڻ��ڸ������Ⱥ��ï�ܵ����ֵȣ��ܼ��ĵط���λ�� 
		    boolean network = mgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER); 
		     if (gps || network) { 
		                return true; 
		            } 
		   return false; 
		}
		catch(Exception e)
		{
		}
		return false;
	}
	
	private void openGPSSettings()
	{
		LocationManager alm =
		(LocationManager)super.context.getSystemService(Context.LOCATION_SERVICE );
//		Log.d("","GPS_PROVIDER="+alm.isProviderEnabled(LocationManager.GPS_PROVIDER));
//		Log.d("","NETWORK_PROVIDER="+alm.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
		if( !alm.isProviderEnabled(LocationManager.GPS_PROVIDER))
			showDialog();
		else{
			locator();
		}
	} 
	private void showDialog()
    {
		Dialog dialog = new AlertDialog.Builder(super.context)
        .setIcon(android.R.drawable.ic_dialog_map)
        .setTitle("GPS����")
        .setMessage("�Ƿ��GPS?")
                .setPositiveButton(R.string.esri_androidviewer_strings_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS );
                    	GPSWidget.super.context.startActivity(myIntent);
                    }
                })
                .setNegativeButton(R.string.esri_androidviewer_strings_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	locator(); 
                    	}
                })
                .create();
		dialog.show();
    }
	
	public final LocationListener mListener = new LocationListener()
	{
		public void onLocationChanged(Location loc) {
			// TODO Auto-generated method stub
			Log.d("onLocationChanged", "come in");
			Log.d("","getLongitude="+loc.getLongitude());
			Log.d("","getLatitude="+loc.getLatitude());
			try
			{
				mLatitude = loc.getLatitude();
				mLongitude = loc.getLongitude();
			}
			catch(Exception e)
			{
				mLatitude = 0;
				mLongitude = 0;
				e.printStackTrace();
			}
			Point ptMap = getPoint(mLongitude,mLatitude);
			//Symbol symbol = new SimpleMarkerSymbol(Color.RED,10,STYLE.CIRCLE);//������ʽ
			PictureMarkerSymbol symbol = new PictureMarkerSymbol(GPSWidget.this.context.getResources().getDrawable(R.drawable.icon_localation));  
			Graphic g = new Graphic(ptMap,symbol);
			GPSWidget.super.mapView.centerAt(ptMap,false);
			mGraphicsLayer.removeAll();
			mGraphicsLayer.addGraphic(g);
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
	private Point getPoint(double lo, double la)
	{
		Point p = new Point(lo,la);
		SpatialReference sr = SpatialReference.create(4326);//��ȡ��ǰ��γ����Ϣ
		Point ptMap = (Point)GeometryEngine.project(p, sr,super.mapView.getSpatialReference());//ת����ϵͳ��������ϵ
		return ptMap;
	}

}

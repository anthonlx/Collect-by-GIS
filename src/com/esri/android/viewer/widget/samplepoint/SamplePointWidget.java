package com.esri.android.viewer.widget.samplepoint;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.esri.android.login.UserLoginActivity;
import com.esri.android.login.UserRegisterActivity;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.viewer.BaseWidget;
import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerApp;
import com.esri.android.viewer.tools.fileTools.filePath;
import com.esri.android.viewer.widget.draw.DrawWidget;
import com.esri.android.viewer.widget.draw.FeatureSymbol;
import com.esri.android.viewer.widget.draw.mapViewOnLongPressListener;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;

public class SamplePointWidget extends BaseWidget{
	
	private View mToolbarView;
	private static String samplepointpath = "";//�������Ŀ¼
	OnLongPressListener  drawlistener=null;
	private static GraphicsLayer samplePointGraphicsLayerTmp=null;
	@Override
	public void active() {
		// TODO Auto-generated method stub
		super.showToolbar(mToolbarView);
		//���ó���Ҫ���¼�
		drawlistener=mapView.getOnLongPressListener();
		mapView.setOnLongPressListener(new samplePointMapViewOnLongPressListener(this,samplePointGraphicsLayer));
	}

	@Override
	public void create() {
		super.setAutoInactive(false);
		ViewerApp appState = ((ViewerApp)SamplePointWidget.this.context.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		samplepointpath= path.samplepointPath;
		LayoutInflater inflater = LayoutInflater.from(super.context);
		mToolbarView = inflater.inflate(R.layout.esri_androidviewer_simplepoint,null);
		Button collectBtn=  (Button)mToolbarView.findViewById(R.id.esri_androidviewer_samplepoint_btncollect);
		collectBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				   Intent intent = new Intent(SamplePointWidget.this.context, SamplePointCameraActivity.class);    	      
				   SamplePointWidget.this.context.startActivity(intent);			
			}});
		Button collectView=  (Button)mToolbarView.findViewById(R.id.esri_androidviewer_samplepoint_btnView);
		collectView.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//AleartBox("�����������...");
				String dbpath = samplepointpath+"/"+com.esri.android.viewer.tools.SystemVariables.PointPackageDirectory+"/"
										+com.esri.android.viewer.tools.SystemVariables.PointDB;
				SQLiteDatabase mDb = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
				Cursor cursor = mDb.query("PHOTO", new String[] {
						"PHID", "LONG","LAT" }, null, null,
						null, null, null);
				samplePointGraphicsLayer.removeAll();
				int num=0;//��¼���������
				while (cursor.moveToNext()) {
					String PHID= cursor.getString(cursor.getColumnIndex("PHID"));
					double LONG = cursor.getDouble(cursor.getColumnIndex("LONG"));
					double LAT= cursor.getDouble(cursor.getColumnIndex("LAT"));
					AddGraphicsToMapView(PHID,LONG,LAT);
					num++;
				}
				String str = "���Ƽ���������"+String.valueOf(num)+"��";
				AleartBox(str);
			}});
		Button collectClear=  (Button)mToolbarView.findViewById(R.id.esri_androidviewer_samplepoint_btnClear);
		collectClear.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				samplePointGraphicsLayer.removeAll();
				AleartBox("����������ɹ�");
			}});	
		super.create();
		//����������ļ��в���ʼ��
		com.esri.android.viewer.tools.sysTools.intiSamplepoint(samplepointpath,"");
		samplePointGraphicsLayerTmp = samplePointGraphicsLayer;
	}
	
	protected void AleartBox(String str) {
		// TODO Auto-generated method stub
		 Toast.makeText(this.context,str,Toast.LENGTH_SHORT).show();
	}

	/**
	 * ���Ҫ������ͼ
	 * @param pHID
	 * @param lONG
	 * @param lAT
	 */
	protected void AddGraphicsToMapView(String pHID, double lo, double la) {
		// TODO Auto-generated method stub
		Point p = new Point(lo,la);
		SpatialReference sr = SpatialReference.create(4326);//��ȡ��ǰ��γ����Ϣ
		Point ptMap = (Point)GeometryEngine.project(p, sr,super.mapView.getSpatialReference());//ת����ϵͳ��������ϵ
		PictureMarkerSymbol symbol = new PictureMarkerSymbol(this.context.getResources().getDrawable(R.drawable.esri_androidviewer_drawable_sim_point_small));  
		Map<String,Object> attributes = new HashMap<String, Object>(); 	
		attributes.put("PHID", pHID);		
		Graphic graphic = new Graphic(ptMap,symbol,attributes);
		samplePointGraphicsLayer.addGraphic(graphic);
	}

	/**
	 * ��MapView��ɾ��Ҫ��
	 * @param i
	 */
	public  static void DelGraphicsToMapView(int i){
		try {
			samplePointGraphicsLayerTmp.removeGraphic(i);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	@Override
	public void inactive() {
		mapView.setOnLongPressListener(drawlistener);
		samplePointGraphicsLayer.clearSelection();//�����ѡ��Ҫ��
		super.inactive();
	}

}

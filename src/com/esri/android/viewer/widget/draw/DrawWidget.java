////////////////////////////////////////////////////////////////////////////////
//
//Copyright (c) 2011-2012 Esri
//
//All rights reserved under the copyright laws of the United States.
//You may freely redistribute and use this software, with or
//without modification, provided you include the original copyright
//and use restrictions.  See use restrictions in the file:
//<install location>/License.txt
//
////////////////////////////////////////////////////////////////////////////////

package com.esri.android.viewer.widget.draw;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;

import jsqlite.Database;
import jsqlite.TableResult;

import android.R.integer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import cn.com.esrichina.spatialitelib.LocalAdd;
import cn.com.esrichina.spatialitelib.LocalVectorTask;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.tasks.ServiceDialogActivity;
import com.esri.android.viewer.BaseWidget;
import com.esri.android.viewer.Log;
import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerActivity;
import com.esri.android.viewer.ViewerApp;
import com.esri.android.viewer.tools.WKT;
import com.esri.android.viewer.tools.sysTools;
import com.esri.android.viewer.tools.taskSqliteHelper;
import com.esri.android.viewer.widget.GPSWidget;
import com.esri.android.viewer.widget.track.TrackWidget;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

public class DrawWidget extends BaseWidget
{
	private MyTouchListener mMyTouchListener = null;
	private MapOnTouchListener mDefaultMyTouchListener;
	public enum DrawType { None, Line, Point, Freeline, FreePolygon,Polygon  }
	private View mPopView = null;//����������
    private View calloutView =null;//��ý����Ϣ����
 
    public static  LocationManager loctionManager;//����LocationManager����
    public static String provider =null;//λ���ṩ��
    //===============================================================================
    private ImageView draw_ImageViewFreeline =null;
    private ImageView draw_ImageViewLine =null;
    private ImageView draw_ImageViewFreePolygon=null;
    private ImageView draw_ImageViewPolygon=null;
    private ImageView draw_ImageViewPoint=null;
    private ImageView draw_ImageViewClear=null;
    private ImageView draw_ImageViewSimplePoint=null;//������
    
    public  TextView draw_txtScale = null;//��ǰ����
    	
	//public static String taskPackagePath = "";//����������ռ�·������biz·��
	public static String taskPackageSimpleDBPath ="";//�����DB·��
	public static String taskPackageSimplePath ="";//������ļ���·��
	//public static String taskPackageSimpleName = "";//������ļ�������
	
	public Spinner spLayer =null;//ͼ���б� 
	
	public static String LAYER_TABLE = "geometry_columns";//Ԫ���ݱ�����¼�洢�ռ����ݵı�
	public static String LAYER_TABLE_NAME_FIELD = "f_table_name";//��¼�洢�ռ����ݱ������ֶ�
	public static String LAYER_TABLE_NAME_TYPE_FIELD = "geometry_type";//�ռ���������
			
	public ToggleButton sw=null;//ͼ���ע
	LocalVectorTask queryTask = null;
	public String dbFile=null;//���ݿ�洢·��
	public static String editLayerName = null;//���༭ͼ������
	public String editLayerType = null;//���༭ͼ������
	public String LaberNameStr =null;//��¼Ҫ�ر�ǩ��
	private List<String>layerFileds ;//ͼ���ֶ��б�
	public static String featureID=null;//Ҫ�ص�Ψһʶ����
	public static  int GraUID ;//Ҫ��ID����ͼ����
	public static boolean isActive = false;//��¼�Ƿ���ʾ
	public static boolean calloutisActive = false;//��¼callout�Ƿ���ʾ
	
	@Override
	public void active() 
	{
		isActive = true;
		super.showToolbar(mPopView);
		mMyTouchListener.setType(DrawType.None);
       intiLocationManager(1);//��ʼ��λ����Ϣ
       CommonValue.drawwitget =DrawWidget.this;
	} 
	
	//��ʼ��ͼ���б�ֵ
	private void intispLayer(ArrayAdapter<String> adapterLayer) {
		// TODO Auto-generated method stub
		mGraphicsLayer.removeAll();
  	    laberGraphicsLayer.removeAll();
		sw.setChecked(false);
  		DrawWidget.super.hideCallout();
  		mMyTouchListener.setType(DrawType.None);
  	    //DrawWidget.super.showMessageBox("��ǰ�������");
  	    try {	    	
  	    	//��ȡ��ǰ���ݿ�·������������
  	    	dbFile = ViewerActivity.taskpath+"/"+ViewerActivity.taskname;
  	    	SQLiteDatabase mDb = SQLiteDatabase.openDatabase(dbFile, null, 0);
				Cursor cursor = mDb.query(LAYER_TABLE, new String[] {
						LAYER_TABLE_NAME_FIELD, LAYER_TABLE_NAME_TYPE_FIELD }, null, null,
						null, null, null);
				adapterLayer.clear();//���ͼ���б�
				while (cursor.moveToNext()) {
					String strfiled = cursor.getString(cursor.getColumnIndex(LAYER_TABLE_NAME_FIELD));
					int type = cursor.getInt(cursor.getColumnIndex(LAYER_TABLE_NAME_TYPE_FIELD));
					if(strfiled.equals("task_extent")) continue;
					switch(type)
					{
						case 1:
							adapterLayer.add(strfiled+" "+"(��)");		
							break;
						case 2:
							adapterLayer.add(strfiled+" "+"(��)");		
							break;
						case 3:
							adapterLayer.add(strfiled+" "+"(��)");		
							break;
					}
					
				}
				mDb.close();
			} catch (Exception e) {
				// TODO: handle exception
				  adapterLayer.clear();//���ͼ���б�
			}

	}

	/**
	 * ����Ҫ�ػ��ư�ť��ʾ������
	 * @param string Ҫ������
	 */
	protected void setDarwBtnVisible(String type) {
		if("(��)".equals(type)){
			//����͸����
			draw_ImageViewFreeline.setAlpha((float)1);
			draw_ImageViewLine.setAlpha((float)1);
			draw_ImageViewFreePolygon.setAlpha((float)0.3);
			draw_ImageViewPolygon.setAlpha((float)0.3);
			draw_ImageViewPoint.setAlpha((float)0.3);
			//���ÿ�����
			draw_ImageViewFreeline.setEnabled(true);
			draw_ImageViewLine.setEnabled(true);
			draw_ImageViewFreePolygon.setEnabled(false);
			draw_ImageViewPolygon.setEnabled(false);
			draw_ImageViewPoint.setEnabled(false);			
		}else if("(��)".equals(type)){
			//����͸����
			draw_ImageViewFreeline.setAlpha((float)0.3);
			draw_ImageViewLine.setAlpha((float)0.3);
			draw_ImageViewFreePolygon.setAlpha((float)1);
			draw_ImageViewPolygon.setAlpha((float)1);
			draw_ImageViewPoint.setAlpha((float)0.3);
			//���ÿ�����
			draw_ImageViewFreeline.setEnabled(false);
			draw_ImageViewLine.setEnabled(false);
			draw_ImageViewFreePolygon.setEnabled(true);
			draw_ImageViewPolygon.setEnabled(true);
			draw_ImageViewPoint.setEnabled(false);
		}else if("(��)".equals(type)){
			//����͸����
			draw_ImageViewFreeline.setAlpha((float)0.3);
			draw_ImageViewLine.setAlpha((float)0.3);
			draw_ImageViewFreePolygon.setAlpha((float)0.3);
			draw_ImageViewPolygon.setAlpha((float)0.3);
			draw_ImageViewPoint.setAlpha((float)1);
			//���ÿ�����
			draw_ImageViewFreeline.setEnabled(false);
			draw_ImageViewLine.setEnabled(false);
			draw_ImageViewFreePolygon.setEnabled(false);
			draw_ImageViewPolygon.setEnabled(false);
			draw_ImageViewPoint.setEnabled(true);			
		}			  
	}

	@Override
	public void inactive() {
		isActive = false;
		super.inactive();
//		DrawWidget.super.hideCallout();
//		mGraphicsLayer.removeAll();		
//		laberGraphicsLayer.removeAll(); 
//		searchGraphicsLayer.removeAll();
		delLocationManager();//����λ�ü���
	}
	
	@Override
	public void create() {
		//��ȡϵͳ�����Ŀ¼
		taskPackageSimpleDBPath = ViewerActivity.taskpath+"/"+ViewerActivity.taskname;
		taskPackageSimplePath = ViewerActivity.taskpath;
		
		FeatureSymbol.setpolygonAlpha(80);//����͸����
		mPopView = getView();
		calloutView = getCalloutView();	
		getLineCalloutView();	
		
		//���ó���Ҫ���¼�
		mapView.setOnLongPressListener(new mapViewOnLongPressListener(this,mGraphicsLayer,calloutView));
		//���������¼�---�����л�
		super.mapView.setOnPinchListener(new mapViewOnPinchListener(this,mGraphicsLayer));	
		//����ƽ���¼�����
		super.mapView.setOnPanListener(new mapViewOnPanListener(this,mGraphicsLayer));
		
		mMyTouchListener = new MyTouchListener(super.context, super.mapView,this,calloutView);
		mDefaultMyTouchListener = new MapOnTouchListener(super.context, super.mapView);
		
		spLayer = (Spinner) mPopView.findViewById(R.id.esri_androidviewer_draw_SpinnerLayer);
		
	   final ArrayAdapter<String> adapterLayer = new ArrayAdapter<String>(mPopView.getContext(), android.R.layout.simple_spinner_item);		 
	   adapterLayer.setDropDownViewResource(android.R.layout. simple_spinner_dropdown_item );
	   spLayer .setAdapter(adapterLayer); 
	  
	   spLayer.setOnItemSelectedListener(new OnItemSelectedListener(){
		
		   public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			   DrawWidget.super.hideCallout();
			   mMyTouchListener.setType(DrawType.None);
			   String layerName = adapterLayer.getItem(arg2);//��¼��ǰͼ�����
			  // Toast.makeText(DrawWidget.super.context,layerName,Toast.LENGTH_SHORT).show();
			  //DrawWidget.super.showMessageBox("��ǰͼ�㣺"+layerName);
			  alertMessageBox("��ǰͼ�㣺"+layerName);
			  sw.setChecked(false);
           	  
			   String[] tmp = layerName.split(" ");			   
			   editLayerName=tmp[0];//��¼��ǰ�༭��ͼ��
			   editLayerType = tmp[1];//��¼��ǰ�༭ͼ�������
			   //������������Ҫ�ػ��ư�ť
			   setDarwBtnVisible(tmp[1]);		
			   //clearMapView();
		   }

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO �Զ����ɵķ������
				 Toast.makeText(DrawWidget.super.context,"ͼ��ѡ��Ϊ�գ�",Toast.LENGTH_SHORT).show();
			}
		   
	   });
	   intispLayer(adapterLayer);//��ʼ��ͼ���б�
		   
		super.create();
		
	}


	/**
	 * ��ȡ������View
	 * @return
	 */
	private View getView()
	{
		LayoutInflater inflater = LayoutInflater.from(super.context);
    	View popView = inflater.inflate(R.layout.esri_androidviewer_draw,null);
    	    	
        sw = (ToggleButton)popView.findViewById(R.id.esri_androidviewer_draw_toggleBtn);
    	sw.setOnCheckedChangeListener(new OnCheckedChangeListener(){
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO �Զ����ɵķ������
				if(isChecked){
					//��ѯ����Table���ֶ��б�
					SQLiteDatabase mDb = SQLiteDatabase.openDatabase(dbFile, null, 0);
					String sqlStr  = "SELECT * FROM "+editLayerName;
					Cursor cursor=mDb.rawQuery(sqlStr,null);
					cursor.moveToFirst();
					final String[] strName = cursor.getColumnNames();	//ͼ���ֶ��б�
					mDb.close();
					new AlertDialog.Builder(context).setTitle("��ע�ֶ�")
			         .setSingleChoiceItems(new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1,strName)
			         		, 0, new DialogInterface.OnClickListener() {
			                 @Override
			                 public void onClick(DialogInterface dialog, int which) {	                        	
			                     dialog.dismiss();
			                    laberGraphicsLayer.removeAll();
			                 	LaberNameStr = strName[which]; 
			                 	double scale = mapView.getScale();
			                 	//DrawWidget.super.showMessageBox("����1:"+LoadState.scale+"ʱ����ʾLaberͼ��");
			                 	alertMessageBox("����1:"+LoadState.scale+"ʱ����ʾLaberͼ��");
			                 	if (scale<LoadState.scale) {
									if (mGraphicsLayer.getNumberOfGraphics()>0) {
										//���Laber����
										LaberTools.addLaber(DrawWidget.this);
									}								
								}		
			                 }
			         })
			        .setNegativeButton("ȡ��",  
						        new DialogInterface.OnClickListener() {  
						            public void onClick(DialogInterface dialog, int whichButton) {  
										sw.setChecked(false);
						            }  
						        })
			         .create().show();
					
				}else{
					laberGraphicsLayer.removeAll();
				}
				
			}
    	});
    	   	
    	draw_ImageViewFreeline = ((ImageView)popView.findViewById(R.id.esri_androidviewer_draw_ImageViewFreeline));
		draw_ImageViewLine = ((ImageView)popView.findViewById(R.id.esri_androidviewer_draw_ImageViewLine));
		draw_ImageViewFreePolygon = ((ImageView)popView.findViewById(R.id.esri_androidviewer_draw_ImageViewFreePolygon));
		draw_ImageViewPolygon = ((ImageView)popView.findViewById(R.id.esri_androidviewer_draw_ImageViewPolygon));
		draw_ImageViewPoint = ((ImageView)popView.findViewById(R.id.esri_androidviewer_draw_ImageViewPoint));
		draw_ImageViewClear = ((ImageView)popView.findViewById(R.id.esri_androidviewer_draw_ImageViewClear));
		draw_ImageViewSimplePoint =  ((ImageView)popView.findViewById(R.id.esri_androidviewer_draw_ImageViewSimplePoint));
		
		draw_txtScale = (TextView)popView.findViewById(R.id.esri_androidviewer_draw_TxtScale);
		
		draw_ImageViewFreeline.setOnClickListener(buttonOnClick);
		draw_ImageViewLine.setOnClickListener(buttonOnClick);
		draw_ImageViewFreePolygon.setOnClickListener(buttonOnClick);
		draw_ImageViewPolygon.setOnClickListener(buttonOnClick);
		draw_ImageViewPoint.setOnClickListener(buttonOnClick);
		draw_ImageViewClear.setOnClickListener(buttonOnClick);
		draw_ImageViewSimplePoint.setOnClickListener(buttonOnClick);
		
		//Ҫ�ؼ���
		Button btnLoad = (Button)popView.findViewById(R.id.esri_androidviewer_draw_BtnLoadFeature);
		btnLoad.setOnClickListener(new OnClickListener(){
			GeoDateTool geotools = new GeoDateTool(DrawWidget.this);
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				searchGraphicsLayer.removeAll();
				Geometry geo = DrawWidget.this.mapView.getExtent();
				Graphic graphic = new Graphic(geo, FeatureSymbol.lineSymboll_search);
				searchGraphicsLayer.addGraphic(graphic);
				
				//����TextSymbol
				TextSymbol  txtsymbol = new TextSymbol(12, sysTools.getTimeNow2(), Color.BLUE);
				Polygon polygon = (Polygon) geo;
				Point p = polygon.getPoint(3);//�������Ͻǵ�һ����
				Graphic textgra = new Graphic(p,txtsymbol);
				searchGraphicsLayer.addGraphic(textgra);
				
//				DrawWidget.this.showMessageBox("Ҫ�ؼ�����...");
				CommonValue.mGraphicsLayer = mGraphicsLayer;
				alertMessageBox("���ڼ��ص�ǰ��ͼ��Χ��Ҫ�أ����Ժ�...");
				clearMapView();//��յ�ͼ
				hideMediaCallout();
				geotools.loadDataFromAPI(DrawWidget.this.editLayerName,
						DrawWidget.this.editLayerType, DrawWidget.this.dbFile, geo,mGraphicsLayer);
				
				//��¼���ؿ�Χ����---���ʱ�䣬�˲�ʱ�䣬������ͼ������ͼ��index����Χgeojson������״̬���������У�
				taskSqliteHelper helper = new taskSqliteHelper(taskPackageSimpleDBPath);
				String layername = spLayer.getSelectedItem().toString();
				Envelope enve = GeometryToEnvelope(geo); //ת��
				String geoStr = enve.getXMax()+","+enve.getXMin()+","+enve.getYMax()+","+enve.getYMin(); //WKT.GeometryToWKT(geo);
				String time = sysTools.getTimeNow2();
				String index = String.valueOf(spLayer.getSelectedItemId());
				String layertype = layername.substring(layername.indexOf("("),layername.length());
				String type="";
				if("(��)".equals(layertype)){
					type = "point";
				}else if("(��)".equals(layertype)){
					type = "polyline";
				}else if("(��)".equals(layertype)){
					type = "polygon";
				}
				helper.insertWorkLogData(time,time,editLayerName,type,layername,index ,geoStr, "��������");
				
			}
			
			//GeometryתEnvelope
			private Envelope GeometryToEnvelope(Geometry geo) {
				// TODO Auto-generated method stub
				Envelope enve  = new Envelope();
				Polygon polygon = (Polygon) geo;
				int pointNum = polygon.getPointCount();//����
				double x_max = 0,x_min =0,y_max=0,y_min=0;
				for(int i=0; i<pointNum;i++){
					Point p = polygon.getPoint(i);
					if(i==0){
						x_max=p.getX();
						x_min=p.getX();
						y_max=p.getY();
						y_min=p.getY();
					}
					if(p.getX()>x_max){
						x_max=p.getX();
					}
					if(p.getX()<x_min){
						x_min = p.getX();
					}
					if(p.getY()>y_max){
						y_max=p.getY();
					}
					if(p.getY()<y_min){
						y_min = p.getY();
					}
				}
				enve.setXMax(x_max);
				enve.setXMin(x_min);
				enve.setYMax(y_max);
				enve.setYMin(y_min);
				return enve;
			}});
    	
		//������־
		Button btnLog = (Button)popView.findViewById(R.id.esri_androidviewer_draw_BtnWorkLog);
		btnLog.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
		    	  DrawWidget.super.showDataPage(getWorkLogView());//��ʾ��־���
			}});
		
    	return popView;
	}
	

	/**
	 * ��ȡ������־Log�б�
	 * @return
	 */
	protected View getWorkLogView() {
		// TODO Auto-generated method stub
		ListView lv = new ListView(super.context);
		LayoutInflater inflater = LayoutInflater.from(super.context);
		
		taskSqliteHelper helper = new taskSqliteHelper(taskPackageSimpleDBPath);
		ArrayList<WorkLogNode> items =helper.getWorkLogList();
		
		WorkLogAdapter listAdapter = new WorkLogAdapter(inflater,items,this);
		lv.setAdapter(listAdapter);
		return lv;	
	}

	/**
	 * ɾ����־��Ŀ
	 */
	public void delWorklogByID(int id){
		taskSqliteHelper helper = new taskSqliteHelper(taskPackageSimpleDBPath);
		helper.delWorkLogByID(id);
	}
	
	/**
	 * ������־��Ŀ
	 */
	public void updateWorklogByID(int id,String key,String value){
		taskSqliteHelper helper = new taskSqliteHelper(taskPackageSimpleDBPath);
		helper.updateWorkLogByID(id, key, value);
	}
	
	/**
	 * ��ȥ���������־
	 */
	public void loadFromWorklog(String tablename,String tabletype,String layername, String layerindex,String time,String extent,String statue){

		this.editLayerName = tablename;
		this.spLayer.setSelection(Integer.valueOf(layerindex));
		
		//String newStr = layername.substring(layername.indexOf("("),layername.length());
		if("point".equals(tabletype)){
			this.editLayerType = "(��)";
		}else if("polyline".equals(tabletype)){
			this.editLayerType = "(��)";
		}else if("polygon".equals(tabletype)){
			this.editLayerType = "(��)";
		}
		searchGraphicsLayer.removeAll();
		String[] arr = extent.split(",");
		Envelope enve = new Envelope(); 
		enve.setXMax(Double.valueOf(arr[0]));
		enve.setXMin(Double.valueOf(arr[1]));
		enve.setYMax(Double.valueOf(arr[2]));
		enve.setYMin(Double.valueOf(arr[3]));
		Geometry geo = enve; //WKT.WKTToGeometry(extent);
		Graphic graphic = new Graphic(geo, FeatureSymbol.lineSymboll_search);
		searchGraphicsLayer.addGraphic(graphic);
		
		//����TextSymbol
		TextSymbol  txtsymbol = new TextSymbol(12,time+","+statue,Color.BLUE);
		//Polygon polygon = (Polygon) geo;
		Point p = new Point();//�������Ͻǵ�һ����
		p.setXY(Double.valueOf(arr[1]), Double.valueOf(arr[2]));
		Graphic textgra = new Graphic(p,txtsymbol);
		searchGraphicsLayer.addGraphic(textgra);
		
		CommonValue.mGraphicsLayer = mGraphicsLayer;
		//alertMessageBox("���ڼ��ص�ǰ��ͼ��Χ��Ҫ�أ����Ժ�...");
		clearMapView();//��յ�ͼ
		hideMediaCallout();
		
		try {
			GeoDateTool geotools = new GeoDateTool(DrawWidget.this);
			geotools.loadDataFromAPI(DrawWidget.this.editLayerName,
					DrawWidget.this.editLayerType, DrawWidget.this.dbFile, geo,
					mGraphicsLayer);
			DrawWidget.this.mapView.setExtent(geo);
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	
	/**
	 * ��ȡ����Ҫ�زɼ�calloutView
	 * @return
	 */
 	private View getCalloutView()
	{
		LayoutInflater inflater = LayoutInflater.from(context);
		View callout= inflater.inflate(R.layout.esri_androidviewer_callout1,null);
		
//      View callout= inflater.inflate(R.layout.esri_androidviewer_mediacallout,null);
    	
//    	CalloutClickListener calloutlistener = new CalloutClickListener();
//    	
//    	Button btnCancle =(Button)callout.findViewById(R.id.esri_androidviewer_callout_btnCancle);
//    	btnCancle.setOnClickListener(calloutlistener);
//     	Button btnDelete =(Button)callout.findViewById(R.id.esri_androidviewer_callout_btnDelete);
//     	btnDelete.setOnClickListener(calloutlistener);
//     	Button btnAtt =(Button)callout.findViewById(R.id.esri_androidviewer_callout_btnAttribute);
//     	btnAtt.setOnClickListener(calloutlistener);
//     	Button btnMedia=(Button)callout.findViewById(R.id.esri_androidviewer_callout_btnMultiMedia);
//     	btnMedia.setOnClickListener(calloutlistener);
//		
//     	ImageButton btnCamera = (ImageButton)callout.findViewById(R.id.esri_androidviewer_callout_imgbtnCamera);
//     	btnCamera.setOnClickListener(calloutlistener);
//     	ImageButton btnVideo = (ImageButton)callout.findViewById(R.id.esri_androidviewer_callout_imgbtnVideo);
//     	btnVideo.setOnClickListener(calloutlistener);
//     	ImageButton btnDraft = (ImageButton)callout.findViewById(R.id.esri_androidviewer_callout_imgbtnDraft);
//     	btnDraft.setOnClickListener(calloutlistener);
//     	ImageButton btnVoice = (ImageButton)callout.findViewById(R.id.esri_androidviewer_callout_imgbtnVoice);
//     	//btnVoice.setOnClickListener(calloutlistener);
//     	btnVoice.setOnTouchListener(imageButtonTouchListener);
    	return callout;
	}
	
	/**
	 * ��ȡ��൯�����
	 * @return
	 */
	private LinearLayout getLineCalloutView() {
		// TODO Auto-generated method stub
		LinearLayout linerLayout=ViewerActivity.linecalloutView;
		ViewerActivity.linecalloutView.setVisibility(View.GONE);
    	
		CalloutClickListener calloutlistener = new CalloutClickListener();
    	
		ImageButton btnCamera = (ImageButton)linerLayout.findViewById(R.id.esri_androidviewer_callout_Camera);
     	btnCamera.setOnClickListener(calloutlistener);
     	ImageButton btnVideo = (ImageButton)linerLayout.findViewById(R.id.esri_androidviewer_callout_Video);
     	btnVideo.setOnClickListener(calloutlistener);
     	ImageButton btnDraft = (ImageButton)linerLayout.findViewById(R.id.esri_androidviewer_callout_Draft);
     	btnDraft.setOnClickListener(calloutlistener);
     	ImageButton btnVoice = (ImageButton)linerLayout.findViewById(R.id.esri_androidviewer_callout_Voice);
     	//btnVoice.setOnClickListener(calloutlistener);
     	btnVoice.setOnTouchListener(imageButtonTouchListener);
     	
     	ImageButton btnCancle =(ImageButton)linerLayout.findViewById(R.id.esri_androidviewer_callout_Cancle);
    	btnCancle.setOnClickListener(calloutlistener);
    	ImageButton btnDelete =(ImageButton)linerLayout.findViewById(R.id.esri_androidviewer_callout_Delete);
     	btnDelete.setOnClickListener(calloutlistener);
     	ImageButton btnAtt =(ImageButton)linerLayout.findViewById(R.id.esri_androidviewer_callout_Attribute);
     	btnAtt.setOnClickListener(calloutlistener);
     	ImageButton btnMedia=(ImageButton)linerLayout.findViewById(R.id.esri_androidviewer_callout_MultiMedia);
     	btnMedia.setOnClickListener(calloutlistener);
     	
		return linerLayout;
	}
	
	/**
	 * ¼����ѹ�¼�
	 */
	private OnTouchListener imageButtonTouchListener = new OnTouchListener() {
		
		  private int  second = 0;//¼��ʱ��
	      private MediaRecorderTool mediarecorderTool = null;
	      String filename =null;
	      
	      private android.app.ProgressDialog ProgressDialog;
	      private Dialog dialog;
	  	  private ImageView dialog_img;
	  	 private double voiceValue=0.0;    //��˷��ȡ������ֵ
	      
		  public boolean onTouch(View v, MotionEvent event) {
			   
			  if(second==0){
					  filename = taskPackageSimplePath+"/"+com.esri.android.viewer.tools.SystemVariables.VoicesDirectory+"/"+
								editLayerName+"_"+featureID+"_"+com.esri.android.viewer.tools.sysTools.getTimeNow()+".mp3";
					  mediarecorderTool = new MediaRecorderTool(filename);//����¼��
					  if(event.getAction()==MotionEvent.ACTION_DOWN){
						  //DrawWidget.this.showMessageBox("¼����ʼ���ɿ���ֹͣ¼����");
						   alertMessageBox("¼����ʼ...");
						   mediarecorderTool.Start();//¼����ʼ
						  //showVoiceDialog();
					  }
				  }else{
//					  voiceValue = mediarecorderTool.getAmplitudeet();
//					  setDialogImage();//����������ʾͼ��
					  if(event.getAction()==MotionEvent.ACTION_UP){
						  mediarecorderTool.Stop();//¼������
						  if(second<=3){	
							  com.esri.android.viewer.tools.fileTools.deleteFiles(filename);
							  if(second<=2){
								  //dialog.dismiss();
								  showRecordingWindow(DrawWidget.super.context, calloutView); 
							  }else{
								  //DrawWidget.this.showMessageBox("¼��ʱ��̫�̣�");
								  alertMessageBox("¼��ʱ��̫�̣�");
							  }	 
						  }else{
							  //DrawWidget.this.showMessageBox("¼���ѱ��棡");
							  alertMessageBox("¼���ѱ��棡");
							  CommonTools.updateFeatureState(taskPackageSimpleDBPath, editLayerName, featureID);//����Ҫ��״̬--����Ϊ�ѱ༭
						  }	
						  second=-1;//����
						 // dialog.dismiss();
						  voiceValue =0;
						  
					  }
				  }
				  second++;
			   return false;
			  }
		  
			//¼��ʱ��ʾDialog
			void showVoiceDialog(){
				dialog = new Dialog(DrawWidget.super.context,R.style.DialogStyle);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
						WindowManager.LayoutParams.FLAG_FULLSCREEN);
				dialog.setContentView(R.layout.recording_dialog);
				dialog_img=(ImageView)dialog.findViewById(R.id.dialog_img);
				dialog.setCancelable(false);
				dialog.show();
			}
			
			//¼��DialogͼƬ��������С�л�
			void setDialogImage(){
				if (voiceValue < 200.0) {
					dialog_img.setImageResource(R.drawable.record_animate_01);
				}else if (voiceValue > 200.0 && voiceValue < 400) {
					dialog_img.setImageResource(R.drawable.record_animate_02);
				}else if (voiceValue > 400.0 && voiceValue < 800) {
					dialog_img.setImageResource(R.drawable.record_animate_03);
				}else if (voiceValue > 800.0 && voiceValue < 1600) {
					dialog_img.setImageResource(R.drawable.record_animate_04);
				}else if (voiceValue > 1600.0 && voiceValue < 3200) {
					dialog_img.setImageResource(R.drawable.record_animate_05);
				}else if (voiceValue > 3200.0 && voiceValue < 5000) {
					dialog_img.setImageResource(R.drawable.record_animate_06);
				}else if (voiceValue > 5000.0 && voiceValue < 7000) {
					dialog_img.setImageResource(R.drawable.record_animate_07);
				}else if (voiceValue > 7000.0 && voiceValue < 10000.0) {
					dialog_img.setImageResource(R.drawable.record_animate_08);
				}else if (voiceValue > 10000.0 && voiceValue < 14000.0) {
					dialog_img.setImageResource(R.drawable.record_animate_09);
				}else if (voiceValue > 14000.0 && voiceValue < 17000.0) {
					dialog_img.setImageResource(R.drawable.record_animate_10);
				}else if (voiceValue > 17000.0 && voiceValue < 20000.0) {
					dialog_img.setImageResource(R.drawable.record_animate_11);
				}else if (voiceValue > 20000.0 && voiceValue < 24000.0) {
					dialog_img.setImageResource(R.drawable.record_animate_12);
				}else if (voiceValue > 24000.0 && voiceValue < 28000.0) {
					dialog_img.setImageResource(R.drawable.record_animate_13);
				}else if (voiceValue > 28000.0) {
					dialog_img.setImageResource(R.drawable.record_animate_14);
				}
			}
			
			
	
	};
	
	
     public class CalloutClickListener implements OnClickListener
    {
		public void onClick(View v) {
			// TODO �Զ����ɵķ������
			switch(v.getId())
	    	{
	    		case R.id.esri_androidviewer_callout_btnCancle:
	    		case R.id.esri_androidviewer_callout_Cancle:
	    			ViewerActivity.linecalloutView.setVisibility(View.GONE);
	    			DrawWidget.super.hideCallout();
	    			mGraphicsLayer.clearSelection();//�����ѡ��Ҫ��
	    			featureID = null;//Ҫ��ΨһID��Ϊ��
	    			DrawWidget.calloutisActive = false;
	    			break;
	    		case R.id.esri_androidviewer_callout_btnDelete:
	    		case R.id.esri_androidviewer_callout_Delete:
	    	    	deleteFeature(featureID);
	    			break;
	    		case R.id.esri_androidviewer_callout_btnAttribute:
	    		case R.id.esri_androidviewer_callout_Attribute:
	    		    Intent attributeintent = new Intent(DrawWidget.super.context, AttributeActivity.class);  //��ת����ҳ
	    		    //��������������ݿ�·����ͼ�����ƣ�Ҫ��ʶ����   		    
	    		    Bundle ba=new Bundle();  
	                ba.putString("taskPackageSimplePath", taskPackageSimpleDBPath);  
	                ba.putString("editLayerName", editLayerName);  
	                ba.putString("featureID", featureID); 
	                attributeintent.putExtras(ba); 
	    		    DrawWidget.super.context.startActivity(attributeintent);
	    			break;
	    		case R.id.esri_androidviewer_callout_btnMultiMedia:
	    		case R.id.esri_androidviewer_callout_MultiMedia:
	    		    Intent intent = new Intent(DrawWidget.super.context, MultiMediaActivity.class);  //��ת����ý����Ϣҳ
	    		    //����������������·����ͼ�����ƣ�Ҫ��ʶ����
	    		    Bundle b=new Bundle();  
	                b.putString("taskPackageSimplePath", taskPackageSimplePath);  
	                b.putString("editLayerName", editLayerName);  
	                b.putString("featureID", featureID); 
	                intent.putExtras(b); 
	    		    DrawWidget.super.context.startActivity(intent);
	    			break;
	    		case R.id.esri_androidviewer_callout_imgbtnCamera:
	    		case R.id.esri_androidviewer_callout_Camera:
	    			Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    			String picture_path =taskPackageSimplePath + "/"+com.esri.android.viewer.tools.SystemVariables.PicturesDirectory;
	    			String name =editLayerName +"_"+featureID+"_"+ com.esri.android.viewer.tools.sysTools.getTimeNow(); 			
	    			Uri imageUri = Uri.fromFile(new File(picture_path,name+".jpg"));   		
	    			openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//д��SD��,д����ʱ�ļ�
	    			ViewerActivity.MainActivity.startActivityForResult(openCameraIntent,0);
	    			break;
	    		case R.id.esri_androidviewer_callout_imgbtnVideo:  		
	    		case R.id.esri_androidviewer_callout_Video:  		
	    			Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	    			String video_path = taskPackageSimplePath + "/"+com.esri.android.viewer.tools.SystemVariables.VideosDirectory;
	    			String videoname = editLayerName +"_"+featureID+"_"+ com.esri.android.viewer.tools.sysTools.getTimeNow()+".mp4";
	    			Uri videoUri = Uri.fromFile(new File(video_path,videoname));   	
	    		    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);//д��SD��
	    		    //DrawWidget.super.context.startActivity(takeVideoIntent);
	    		    ViewerActivity.MainActivity.startActivityForResult(takeVideoIntent,1);
	    			break;
	    		case R.id.esri_androidviewer_callout_imgbtnVoice:
	    		case R.id.esri_androidviewer_callout_Voice:
	    			showRecordingWindow(DrawWidget.super.context, calloutView); 
	    			break;
	    		case R.id.esri_androidviewer_callout_imgbtnDraft:
	    		case R.id.esri_androidviewer_callout_Draft:
	    			  Intent draftintent = new Intent(DrawWidget.super.context, DraftActivity.class);  //��ת����ͼ��ҳ��
	    			  //����������������·����ͼ�����ƣ�Ҫ��ʶ����
	    			  String draftPath = taskPackageSimplePath + "/"+com.esri.android.viewer.tools.SystemVariables.DraftsDirectory;
    			      Bundle bundle=new Bundle();  
	                  bundle.putString("draftPath", draftPath);  
	                  bundle.putString("editLayerName", editLayerName);  
	                  bundle.putString("featureID", featureID); 
	                  draftintent.putExtras(bundle); 
		    		  DrawWidget.super.context.startActivity(draftintent);
	    			break;
	    		default:
	    			Toast.makeText(DrawWidget.super.context,"���ԣ�",Toast.LENGTH_SHORT).show();
	    			break;
	    	}
		}
	
		/**
		 * ɾ��Ҫ��
		 * @param featureID
		 */
		private void deleteFeature(final String featureID) {
			// TODO �Զ����ɵķ������
			
			AlertDialog.Builder builder=new AlertDialog.Builder(DrawWidget.super.context);
			builder.setMessage("�Ƿ�ɾ����Ҫ��?\n˵��:�˲�Ҫ�ر��ɾ�����½�Ҫ������ɾ����");
			builder.setCancelable(true);
			builder.setTitle("ϵͳ��ʾ");
			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface arg0, int arg1) {
					
				}
			});
			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					boolean isNewAdd = false;
					Database db= new Database();
					try {
						db.open(dbFile,2);
						//ɾ��ǰ���ж�Ҫ���Ƿ�Ϊ����Ҫ��ֱ������ɾ��
						String str = "Select F_STATE from "+editLayerName+ " where FEATUREID = '"+featureID+"'";
						TableResult tb =db.get_table(str);
						String[] s = tb.rows.get(0);
						int st = Integer.parseInt(s[0]);
						if(st==2) isNewAdd =true;		
					} catch (jsqlite.Exception e1) {
						e1.printStackTrace();
					}
								
					String sqlStr = "";
					String sqllog ="";
					String time = com.esri.android.viewer.tools.sysTools.getTimeNow2();
					if(isNewAdd){//����
					    sqlStr = "delete from " +editLayerName+ " where FEATUREID = '"+featureID+"'";//����ɾ��
					    sqllog = "INSERT INTO SYS_LOGS(F_USERID,F_TIME,F_LAYER,F_FEATURE,F_ACTION,F_REMARK) VALUES ("
								+ViewerActivity.userid+","
								+"'"+time+"',"
								+"'"+editLayerName+"',"
								+"'"+featureID+"',"
								+FeatureLogState.featureRemarkDel+","
								+"'���ɾ��')";
					}else{
						sqlStr = "UPDATE "+editLayerName+" SET F_STATE =3 WHERE FEATUREID='"+featureID+"'";//3-Ҫ�ر��ɾ��	
						 sqllog = "INSERT INTO SYS_LOGS(F_USERID,F_TIME,F_LAYER,F_FEATURE,F_ACTION,F_REMARK) VALUES ("
									+ViewerActivity.userid+","
									+"'"+time+"',"
									+"'"+editLayerName+"',"
									+"'"+featureID+"',"
									+FeatureLogState.featureTrueDel+","
									+"'ɾ��Ҫ��')";
					}
					
					try{				
						try {
//							db.exec(sqllog, null);//��־   �޸�  2015-12-11  by David.Ocean  ȡ����ȡ
						} catch (Exception e) {
							Toast.makeText(DrawWidget.super.context,"�������־д��ʧ�ܣ�"+e.toString(),Toast.LENGTH_SHORT).show();
						}
						db.exec(sqlStr,null);//Ҫ�ر��ɾ��
						db.close();
						ViewerActivity.linecalloutView.setVisibility(View.GONE);//���ض�ý�崰��
						mGraphicsLayer.removeGraphic(GraUID);	
						DrawWidget.super.hideCallout();
						DrawWidget.calloutisActive = false;
						recordWorkLocation();//��¼��ǰҪ�ر༭ʱλ��
					}catch(Exception e){
						//TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			builder.show();
		}
		
    }
     
    /**
     * ����¼���Ի���
     * @param context
     * @param parent
     */
    public void showRecordingWindow(Context context,View parent){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);     
        final View vPopupWindow=inflater.inflate(R.layout.esri_androidviewer_recording, null, false);  
        final PopupWindow pw= new PopupWindow(vPopupWindow,720,450,true);  
        
        final String file = taskPackageSimplePath+"/"+com.esri.android.viewer.tools.SystemVariables.VoicesDirectory+"/"+
				editLayerName+"_"+featureID+"_"+com.esri.android.viewer.tools.sysTools.getTimeNow()+".mp3";
        final MediaRecorderTool mediarecorderTool = new MediaRecorderTool(file);//����¼��
        
        //Cancel��ť���䴦���¼�  
        final ImageButton btnClose=(ImageButton)vPopupWindow.findViewById(R.id.esri_androidviewer_recording_closebtn);  
        btnClose.setOnClickListener(new OnClickListener(){  
            public void onClick(View v) { 
            	com.esri.android.viewer.tools.fileTools.deleteFiles(file);
                pw.dismiss();//�ر�  
            }  
        });  
        	  
        final Button btnRecordingStart = (Button)vPopupWindow.findViewById(R.id.esri_androidviewer_recording_btnStart);	
        final Button btnRecordingStop = (Button)vPopupWindow.findViewById(R.id.esri_androidviewer_recording_btnStop);
        final Chronometer chron = (Chronometer) vPopupWindow.findViewById(R.id.esri_androidviewer_recording_chronometer);
        
        btnRecordingStart.setEnabled(true);
        btnRecordingStop.setEnabled(false);
        
        btnRecordingStart.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				//��ʼ¼��
				mediarecorderTool.Start();
				chron.setBase(SystemClock.elapsedRealtime());
				chron.start();
				btnRecordingStart.setEnabled(false);
			    btnRecordingStop.setEnabled(true);
			    btnClose.setEnabled(false);
			}
        });
                
        btnRecordingStop.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				//ֹͣ¼��
				mediarecorderTool.Stop();
				chron.stop();
				btnRecordingStart.setEnabled(true);
			    btnRecordingStop.setEnabled(false);
			    btnClose.setEnabled(true);
			    Toast.makeText(DrawWidget.super.context,"¼���ѱ��棡",Toast.LENGTH_SHORT).show();
			    pw.dismiss();//�ر�  
			    CommonTools.updateFeatureState(taskPackageSimpleDBPath, editLayerName, featureID);//����Ҫ��״̬--����Ϊ�ѱ༭
			}
        });           
        //��ʾpopupWindow�Ի���  
        pw.showAtLocation(parent, Gravity.CENTER, 0, 0);  
    }  
    
	/**
	 * ��ʼ��loctionManager
	 */
	private void intiLocationManager(int t) {
		String contextService=Context.LOCATION_SERVICE;
	    //ͨ��ϵͳ����ȡ��LocationManager����
	    loctionManager=(LocationManager) DrawWidget.this.context.getSystemService(contextService);  
	    //ʹ�ñ�׼���ϣ���ϵͳ�Զ�ѡ����õ����λ���ṩ�����ṩλ��
	    Criteria criteria = new Criteria();
	    criteria.setAccuracy(Criteria.ACCURACY_FINE);//�߾���
	    criteria.setAltitudeRequired(true);//Ҫ�󺣰�
	    criteria.setBearingRequired(true);//Ҫ��λ
	    criteria.setCostAllowed(true);//�����л���
	    criteria.setPowerRequirement(Criteria.POWER_MEDIUM);//����   
	    //�ӿ��õ�λ���ṩ���У�ƥ�����ϱ�׼������ṩ��
	     provider = loctionManager.getBestProvider(criteria, true);
		loctionManager.requestLocationUpdates(provider, t*1000, 2, mListener);
	}
	
	/**
	 * �ر�λ�ü��
	 */
	public  void delLocationManager(){
		try {
			loctionManager.removeUpdates(mListener);
			loctionManager = null;
			locGraphicsLayer.removeAll();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
    
	public  LocationListener mListener = new LocationListener()
	{
		private double mLongitude = 0.0;
		private double mLatitude = 0.0;
		public void onLocationChanged(Location location) {
			if(location != null){
				try
				{
					mLatitude = location.getLatitude();
					mLongitude = location.getLongitude();
				}
				catch(Exception e)
				{
					mLatitude = 0;
					mLongitude = 0;
					e.printStackTrace();
				}
				Point ptMap = getPoint(mLongitude,mLatitude);
				//DrawWidget.super.mapView.centerAt(ptMap,false);
				//Symbol symbol = new SimpleMarkerSymbol(Color.RED,10,STYLE.CIRCLE);//������ʽ
				PictureMarkerSymbol symbol = new PictureMarkerSymbol(DrawWidget.this.context.getResources().getDrawable(R.drawable.icon_localation2));  
				Graphic g = new Graphic(ptMap,symbol);
				locGraphicsLayer.removeAll();
				locGraphicsLayer.addGraphic(g);
				//DrawWidget.super.showCallout(ptMap, simplePoint_calloutView);
			}
		}
		
		private Point getPoint(double lo, double la)
		{
			Point p = new Point(lo,la);
			SpatialReference sr = SpatialReference.create(4326);
			Point ptMap = (Point)GeometryEngine.project(p, sr,localspatialReference.spatialReferencePM);
			return ptMap;
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
	
    private Button.OnClickListener buttonOnClick = new Button.OnClickListener()
    {
	    public void onClick(View v)
	    {
	    	switch(v.getId())
	    	{
	    	case R.id.esri_androidviewer_draw_ImageViewSimplePoint:
	    		break;
	    		case R.id.esri_androidviewer_draw_ImageViewClear:
	    			int num  = mGraphicsLayer.getNumberOfGraphics();
	    			if(num>0){
	    				AlertDialog.Builder builder=new AlertDialog.Builder(DrawWidget.this.context);
		    			builder.setMessage("�Ƿ����Ҫ�ؼ����ط�Χ?");
		    			builder.setCancelable(true);
		    			builder.setTitle("ϵͳ��ʾ");
		    			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
		    			{
		    				public void onClick(DialogInterface arg0, int arg1) {
		    					
		    				}
		    			});
		    			builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() 
		    			{
		    				public void onClick(DialogInterface arg0, int arg1) {
		    					// TODO Auto-generated method stub
		    					clearMapView();
		    					searchGraphicsLayer.removeAll();//���������
		    					hideMediaCallout();
		    	    			DrawWidget.super.showMessageBox("����ɹ�");
		    				}
		    			});
		    			builder.show();		
	    			}else{
	    				searchGraphicsLayer.removeAll();//���������
	    				DrawWidget.super.showMessageBox("��ǰ��ͼ��û��Ҫ�أ�");
	    			}
	    			break;
	    		case R.id.esri_androidviewer_draw_ImageViewPoint:
	    	    	mMyTouchListener.setType(DrawType.Point);
	    	    	DrawWidget.super.mapView.setOnTouchListener(mMyTouchListener);
	    	    	DrawWidget.super.showMessageBox("���Ƶ�");
	    			break;
	    		case R.id.esri_androidviewer_draw_ImageViewFreeline:
	    	        mMyTouchListener.setType(DrawType.Freeline);
	    	        DrawWidget.super.mapView.setOnTouchListener(mMyTouchListener);
	    	        DrawWidget.super.showMessageBox("��������");
	    			break;
	    		case R.id.esri_androidviewer_draw_ImageViewLine:
	    	        mMyTouchListener.setType(DrawType.Line);
	    	        DrawWidget.super.mapView.setOnTouchListener(mMyTouchListener);
	    	        DrawWidget.super.showMessageBox("��������");
	    			break;
	    		case R.id.esri_androidviewer_draw_ImageViewPolygon:
	    	        mMyTouchListener.setType(DrawType.Polygon);
	    	        DrawWidget.super.mapView.setOnTouchListener(mMyTouchListener);
	    	        DrawWidget.super.showMessageBox("���ƶ����");
	    			break;
	    		case R.id.esri_androidviewer_draw_ImageViewFreePolygon:
	    	        mMyTouchListener.setType(DrawType.FreePolygon);
	    	        DrawWidget.super.mapView.setOnTouchListener(mMyTouchListener);
	    	        DrawWidget.super.showMessageBox("������״�����");
	    			break;
	    	}
	    }
    };
  
    public class MyTouchListener extends MapOnTouchListener 
    {
    	MultiPath polyline,polygon,line,freepolygon;
        DrawType type = DrawType.None;
        Point startPoint = null;
        MapView map = null;
        DrawWidget drawwidget =null;
        
        private View calloutView =null;
      
        int graphicFreelineId = 0;
        int graphicLineId = 0;
        int graphicFreePloygonId = 0;
        int graphicPloygonId = 0;
   
        public MyTouchListener(Context context, MapView view,DrawWidget  d,View v) {  
        	super(context, view);  
        	map = view;
        	drawwidget=d;
        	calloutView = v;
       }
     
        public void setType(DrawType type) {
          this.type = type;
        }
        public DrawType getType() {
          return this.type;
        }
        
        @Override
        public boolean onSingleTap(MotionEvent e) 
        {
            if(type == DrawType.Point) 
            {
            	Geometry geo = map.toMapPoint(new Point(e.getX(), e.getY()));
            	Graphic gra = addGeometryToLocalDB(geo);        	
            	if(gra!=null){
            		GraUID =mGraphicsLayer.addGraphic(gra);//���Ҫ�������ݿ�
                	//����callout����
            		Point coordinate=(Point) geo;
            		alertMultiMedia(coordinate,this.calloutView);
            		mMyTouchListener.setType(DrawType.None);
            	}     	
        		return true;
            }else if(type == DrawType.Line){
            	//��ȡ��Ļ��������
        		Point point = map.toMapPoint(new Point(e.getX(), e.getY()));          	
            	if (startPoint == null) {            		
					startPoint = point;		
					line = new Polyline();
					line.startPath(point);		
					//��ӽڵ���Ϣ
					Graphic graphic = new Graphic(point,new SimpleMarkerSymbol(Color.BLACK,5,STYLE.CIRCLE));
					tmpLayer.addGraphic(graphic);
					//�����Ҫ��         	
	            	Graphic graphic_line = new Graphic(line,FeatureSymbol.lineSymbol_new);
	            	graphicLineId = mGraphicsLayer.addGraphic(graphic_line);    
				} else{					
					//�����Ҫ�ؽڵ�
					Graphic graphic_t = new Graphic(point,new SimpleMarkerSymbol(Color.BLACK,5,STYLE.CIRCLE));
					tmpLayer.addGraphic(graphic_t);	
					//��������Ϣ
					line.lineTo(point);
					mGraphicsLayer.updateGraphic(graphicLineId, line); 
				}	
            	DrawWidget.super.showMessageBox("˫��������������");
            }else if(type == DrawType.Polygon){
            	//��ȡ��Ļ��������
        		Point point = map.toMapPoint(new Point(e.getX(), e.getY()));          	
            	if (startPoint == null) {
					startPoint = point;		
					polygon = new Polygon();
					polygon.startPath(point);		
					//��ӽڵ���Ϣ
					Graphic graphic = new Graphic(point,new SimpleMarkerSymbol(Color.BLACK,5,STYLE.CIRCLE));
					tmpLayer.addGraphic(graphic);
					//��Ӷ����Ҫ��         	
	            	Graphic graphic_polygon = new Graphic(polygon,FeatureSymbol.polygonSymbol_new);
	            	graphicPloygonId = mGraphicsLayer.addGraphic(graphic_polygon);      	
				} else{					
					//���Ҫ�ؽڵ�
					Graphic graphic_t = new Graphic(point,new SimpleMarkerSymbol(Color.BLACK,5,STYLE.CIRCLE));
					tmpLayer.addGraphic(graphic_t);	
					//���¶������Ϣ
					polygon.lineTo(point);
					mGraphicsLayer.updateGraphic(graphicPloygonId, polygon); 
				}	  
            	DrawWidget.super.showMessageBox("˫���������ƶ����");
            }
            return false;
        }
        
        @Override
		public boolean onDoubleTap(MotionEvent event) {
        	tmpLayer.removeAll();
			if (type == DrawType.Line) {	
				if(line!=null){
					Graphic gral = addGeometryToLocalDB(line);//���Ҫ�������ݿ�
					if(gral!=null){
						mGraphicsLayer.updateGraphic(graphicLineId,gral); 
						alertMultiMedia(this.startPoint,this.calloutView);
					}else{
						mGraphicsLayer.removeGraphic(graphicLineId);
					}	
				}else{
					Toast.makeText(DrawWidget.super.context,"δ�������Ҫ�أ�",Toast.LENGTH_SHORT).show();
				}
				GraUID =graphicLineId;
				startPoint = null;
				line = null;
				mMyTouchListener.setType(DrawType.None);
				 DrawWidget.super.showMessageBox("���߻��ƽ�����");
				return true;
			}else if(type == DrawType.Polygon){		
				if(polygon!=null){
					Graphic gra = addGeometryToLocalDB(polygon);
	        		if(gra!=null){
	        			mGraphicsLayer.updateGraphic(graphicPloygonId,gra ); //���Ҫ�������ݿ�	
	        			alertMultiMedia(this.startPoint,this.calloutView);
	        		}else{
	        			mGraphicsLayer.removeGraphic(graphicPloygonId);
	        		}  
				}else{
					Toast.makeText(DrawWidget.super.context,"δ�������Ҫ�أ�",Toast.LENGTH_SHORT).show();
				}
	    		GraUID =graphicPloygonId;
        		startPoint = null;
				polygon = null;
				mMyTouchListener.setType(DrawType.None);
				DrawWidget.super.showMessageBox("����λ��ƽ�����");
				return true;		
			}
			return super.onDoubleTap(event);
		}
        
        /**
         * ������ý����Ϣ����
         * @param <startPoint>
         */
		private  void alertMultiMedia(Point point, View v) {
			drawwidget.showCallout(point, v);
			ViewerActivity.linecalloutView.setVisibility(View.VISIBLE);
		}
        
        //@Override
        public boolean onDragPointerMove(MotionEvent from, MotionEvent to) 
        {
        	Point mapPt = map.toMapPoint(to.getX(), to.getY());
        	if (type == DrawType.Freeline) 
        	{
        		if (startPoint == null) 
        		{
        			polyline = new Polyline();
        			startPoint = map.toMapPoint(from.getX(), from.getY());
        			polyline.startPath((float) startPoint.getX(), (float) startPoint.getY());
        			graphicFreelineId = mGraphicsLayer.addGraphic(new Graphic(polyline,FeatureSymbol.lineSymbol_new));
        		}
    			polyline.lineTo((float) mapPt.getX(), (float) mapPt.getY());
    			mGraphicsLayer.updateGraphic(graphicFreelineId,new Graphic(polyline,FeatureSymbol.lineSymbol_new));
				return true;
        	}
        	else if (type == DrawType.FreePolygon) 
        	{
        		//polygonSymbol.setAlpha(80);
        		if (startPoint == null) 
        		{
        			freepolygon = new Polygon();
        			startPoint = map.toMapPoint(from.getX(), from.getY());
        			freepolygon.startPath((float) startPoint.getX(), (float) startPoint.getY());
        			graphicFreePloygonId = mGraphicsLayer.addGraphic(new Graphic(freepolygon,FeatureSymbol.polygonSymbol_new));
        		}
        		freepolygon.lineTo((float) mapPt.getX(), (float) mapPt.getY());
    			mGraphicsLayer.updateGraphic(graphicFreePloygonId, new Graphic(freepolygon,FeatureSymbol.polygonSymbol_new));
				return true;
        	}else if(type == DrawType.Polygon||type == DrawType.Line){
        		return false;//����false��ʹ��Ļ�������̶���(����λ��)
        	}
        	return super.onDragPointerMove(from, to);
        }

        @Override
        public boolean onDragPointerUp(MotionEvent from, MotionEvent to) 
        {
        	if(type == DrawType.Line ||type == DrawType.Polygon){
        		//�����κβ���
        		return false;//����false��ʹ��Ļ�������̶���(����λ��)
        	}else if(type ==DrawType.Freeline ){
    			Graphic gral = addGeometryToLocalDB(polyline);//���Ҫ�������ݿ�
    			if(gral!=null){
    				mGraphicsLayer.updateGraphic(graphicFreelineId,gral);
    				alertMultiMedia(this.startPoint,this.calloutView);
    			}else{
    				mGraphicsLayer.removeGraphic(graphicFreelineId);
    			}
        		GraUID = graphicFreelineId;
				startPoint = null;
				polyline = null;//��״��
				DrawWidget.super.mapView.setOnTouchListener(mDefaultMyTouchListener);
        	}else if(type == DrawType.FreePolygon){
    		    Graphic gra = addGeometryToLocalDB(freepolygon);
    		    if(gra!=null){
    		    	mGraphicsLayer.updateGraphic(graphicFreePloygonId,gra);//���Ҫ�������ݿ�
    		    	alertMultiMedia(this.startPoint,this.calloutView);
    		    }else{
    		    	mGraphicsLayer.removeGraphic(graphicFreePloygonId);
    		    }			
				GraUID = graphicFreePloygonId;
				startPoint = null;
				freepolygon = null;//��״��
				DrawWidget.super.mapView.setOnTouchListener(mDefaultMyTouchListener);
        	} 
        	return super.onDragPointerUp(from, to);
        }
        
        /**
         * ���Graphic��������ֵ����д�����ݿ�
         * @param geometry
         * @return
         */
    	public Graphic addGeometryToLocalDB(Geometry geometry) {
    		// TODO ����geometry��д�����ݿ�
    		String md5= java.util.UUID.randomUUID().toString();//GUID;
    		featureID = md5;//����Ҫ��ΨһID
    		Map<String,Object> attributes = new HashMap<String, Object>(); 	
    		attributes.put("FEATUREID", featureID);		
    		    		
    		Graphic graphicPM =null;
    		
			if ("POINT".equals(geometry.getType().toString())) {
				graphicPM = new Graphic(geometry, FeatureSymbol.pointSymbol_new, attributes);	
			}else if("POLYLINE".equals(geometry.getType().toString())){
				graphicPM = new Graphic(geometry, FeatureSymbol.lineSymbol_new, attributes);	
			}else if("POLYGON".equals(geometry.getType().toString())){
				graphicPM = new Graphic(geometry, FeatureSymbol.polygonSymbol_new, attributes);	
			}
    					
    		//����LocalAdd������������
    		LocalAdd localAdd = new LocalAdd();
    		localAdd.setGraphics(new Graphic[]{graphicPM});
    		localAdd.setSpatialReference(localspatialReference.spatialReferencePM);//���ÿռ�ο�Ϊ4490
    		localAdd.setTableName(editLayerName);
    		
    		// dbFile��string���͵Ĳ�����ָ���ݿ��ļ���SD���ϵĴ洢·��
    		LocalVectorTask queryTask = new LocalVectorTask(dbFile);
    		try {
    			//����������Ϊ�������ݸ�LocalVectorTask��add��������ɱ���
    			queryTask.add(localAdd);
    			//������־
    			String time = com.esri.android.viewer.tools.sysTools.getTimeNow2();
				String sqllog = "INSERT INTO SYS_LOGS(F_USERID,F_TIME,F_LAYER,F_FEATURE,F_ACTION,F_REMARK) VALUES ("
									+ViewerActivity.userid+","
									+"'"+time+"',"
									+"'"+editLayerName+"',"
									+"'"+featureID+"',"
									+FeatureLogState.featureAdd+","
									+"'���Ҫ��')";
				Database db= new Database();
				db.open(dbFile,2);
				try {
//					db.exec(sqllog, null);//��־ �޸�  2015-12-11  by David.Ocean  ȡ����ȡ
				} catch (Exception e) {
					 Toast.makeText(DrawWidget.super.context,"�������־д��ʧ�ܣ�"+e.toString(),Toast.LENGTH_SHORT).show();
					return null;
				}
				String setF_sata = "UPDATE "+editLayerName+" SET F_STATE =2 WHERE FEATUREID='"+featureID+"'";//2-Ҫ������
				db.exec(setF_sata,null);//Ҫ��״̬	
				db.close();
				recordWorkLocation();//��¼��ǰҪ�ر༭ʱλ��
				return graphicPM;
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    			//String str = "SpatialReference��"+localspatialReference.spatialReferencePM.getID();
    			Toast.makeText(DrawWidget.super.context,"���������ϵ����Ҫ��д�����ݿ�ʧ�ܣ�"+e.toString(),Toast.LENGTH_SHORT).show();
    			return null;
    		}
    	}
        
    }

    /**
     * ��¼��ǰ����λ����Ϣ
     */
    public void recordWorkLocation(){  	
	    try {
	    	//������һ�α仯��λ��
	    	Location location = loctionManager.getLastKnownLocation(provider);
			Date lo_time = new java.util.Date(location.getTime());//��ȡ��λʱ���
			Date time_now = new Date(System.currentTimeMillis());
			long sp = Math.abs(lo_time.getTime() - time_now.getTime());
			if (sp >5000) {//����λʱ������5�����������ôν��
				return ;
			}
			ViewerApp appState = ((ViewerApp)DrawWidget.super.context.getApplicationContext()); 
			com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
			String ConfigSqliteDB= path.systemConfigFilePath+"/" +com.esri.android.viewer.tools.SystemVariables.ConfigSqliteDB;
	    	FeatureCollectLocState coll = new FeatureCollectLocState(ConfigSqliteDB);
	    	String deviceid = com.esri.android.viewer.tools.sysTools.getLocalMacAddress(DrawWidget.this.context);
	    	coll.insectLocation(ViewerActivity.userid, ViewerActivity.taskid, deviceid, location);
		
		 } catch (Exception e) {
			// TODO: handle exception
			return ;
		}
    	
    }
    
    /**
     * ���Ҫ��
     */
	private void clearMapView() {
		ViewerActivity.linecalloutView.setVisibility(View.GONE);
		mGraphicsLayer.removeAll();
		laberGraphicsLayer.removeAll();
		tmpLayer.removeAll();
		//���õ�ͼtouch�¼���������������쳣������ӽڵ���Ϣ��ȴ�޷���ʾ��·��Ϣ
		mMyTouchListener = new MyTouchListener(DrawWidget.super.context, DrawWidget.super.mapView,DrawWidget.this,calloutView);
		mMyTouchListener.setType(DrawType.None);
		DrawWidget.super.mapView.setOnTouchListener(mMyTouchListener);
	}

	public void hideMediaCallout() {
		DrawWidget.super.hideCallout();// �ر�callout
		    ViewerActivity.linecalloutView.setVisibility(View.GONE);//���ض�ý�崰��
	}

}
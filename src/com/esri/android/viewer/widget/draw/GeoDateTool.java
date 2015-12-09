package com.esri.android.viewer.widget.draw;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;

import cn.com.esrichina.spatialitelib.LocalQuery;
import cn.com.esrichina.spatialitelib.LocalVectorTask;

public class GeoDateTool {

	DrawWidget drawWidget = null;
	MyHandler mHandler = null; 
	long num  =0;
	
	public GeoDateTool(DrawWidget d) {
		drawWidget = d;
		mHandler = new MyHandler();//����Handler 
	}

	/**
	 * ���ص㡢�ߡ���Ҫ�����ݣ��������ڵ�ͼ��
	 */
	public void loadDataFromAPI(final String tablename,final String Type,final String dbFile,final Geometry geo,final GraphicsLayer mGraphicsLayer) {
		//����һ�����̻߳����̳߳أ���ֻ����Ψһ�Ĺ����߳���ִ������
		//��֤����������ָ��˳��(FIFO, LIFO, ���ȼ�)ִ�С�
				
		num=0;//����Ҫ������
		
		//��������ǰ�����Ļ
		mGraphicsLayer.removeAll();
		
		//��һ�߳�ģʽ
		ExecutorService  singleThreadExecutor = Executors.newSingleThreadExecutor();
		singleThreadExecutor.execute(new Runnable() {
	        @Override
	        public void run() {
	          if(LoadState.state==false){
	        	 LoadState.state =true;//Ҫ�ؼ����С�����
	        	 try {	
	    			String type= Type;
	    			LocalQuery query = new LocalQuery();
	    			query.setTableName(tablename);//���ñ���		
	    			query.setOutFields(new String[]{"FEATUREID","F_STATE"});
	    			query.setReturnGeometry(true);
	    			//Ĭ�ϼ���һ�������� 
	    			query.setWhere("F_STATE!=3");//����ʾ�Ѿ����ɾ����
	    			//���ݿռ��ѯ����
	    			query.setGeometry(geo);
	    			//���ݿռ��ϵ����
	    			query.setSpatialRelationship("Intersects");

	    			//ʵ����һ��LocalVectorTask��Ķ���ʱ���乹�캯�����Զ�����openDatabase()������Ĭ�ϴ����ݿ⣻
	    			LocalVectorTask queryTask = new LocalVectorTask(dbFile);
	    								
	    				// ��ȡ���ݣ������ڵ�ͼ��
	    			FeatureSet featureSet = queryTask.query(query);
	    			Graphic[] graphics = featureSet.getGraphics();
	    			for(int i=0;i<graphics.length;i++){
	    				   num++;
	    				   int f_type =Integer.valueOf(graphics[i].getAttributeValue("F_STATE").toString());//Ҫ������
	    					if("(��)".equals(type)){//��Ҫ��
	    						Polyline polyline =(Polyline)graphics[i].getGeometry();
	    						Graphic graphic =null;
	    						switch(f_type){
	    							case 1://Ĭ�ϼ���
	    								graphic = new Graphic(polyline, FeatureSymbol.lineSymbol_old,graphics[i].getAttributes(), (Integer) null);
	    								break;
	    							case 2://����Ҫ��
	    								graphic = new Graphic(polyline, FeatureSymbol.lineSymbol_new,graphics[i].getAttributes());
	    								break;
	    							case 4://�������޸�Ҫ��
	    								graphic = new Graphic(polyline, FeatureSymbol.lineSymboll_update,graphics[i].getAttributes());
	    								break;
	    							case 3://��ɾ��Ҫ��
	    								break;
	    						}		
	    				        mGraphicsLayer.addGraphic(graphic);
	    					}else if("(��)".equals(type)){//��Ҫ��
	    						Polygon polygon = (Polygon)graphics[i].getGeometry();
	    						Graphic graphic2 = null;
	    						switch(f_type){
	    							case 1://Ĭ�ϼ���
	    								graphic2 = new Graphic(polygon, FeatureSymbol.polygonSymbol_old,graphics[i].getAttributes());
	    								break;
	    							case 2://����Ҫ��
	    								graphic2 = new Graphic(polygon, FeatureSymbol.polygonSymbol_new,graphics[i].getAttributes());
	    								break;
	    							case 4://�������޸�Ҫ��
	    								graphic2 = new Graphic(polygon, FeatureSymbol.polygonSymbol_update,graphics[i].getAttributes());
	    								break;
	    							case 3://��ɾ��Ҫ��
	    								break;
	    						}		
	    					    mGraphicsLayer.addGraphic(graphic2);
	    					}else if("(��)".equals(type)){//��Ҫ��
	    						Point point = (Point)graphics[i].getGeometry();
	    						Graphic graphic3 =null;
	    						switch(f_type){
	    							case 1://Ĭ�ϼ���
	    								graphic3 = new Graphic(point, FeatureSymbol.pointSymbol_old,graphics[i].getAttributes());
	    								break;
	    							case 2://����Ҫ��
	    								graphic3 = new Graphic(point, FeatureSymbol.pointSymbol_new,graphics[i].getAttributes());
	    								break;
	    							case 4://�������޸�Ҫ��
	    								graphic3 = new Graphic(point, FeatureSymbol.pointSymbol_update,graphics[i].getAttributes());
	    								break;
	    							case 3://��ɾ��Ҫ��
	    								break;
	    						}		
	    					    mGraphicsLayer.addGraphic(graphic3);
	    					}			  
	    			}			
	    			//ʹ��������closeDatabase()�����ر����ݿ⣬�´���Ҫ�ٴ򿪸����ݿ�ʱ��ʹ��openDatabase����
	    			queryTask.closeDatabase();				
	    			Log.v("Ҫ�ؼ���", "Ҫ�ؼ��ؽ�����"); 
	    		} catch (Exception ex) {
	    			ex.printStackTrace();
	    		}
	        	LoadState.state =false;//Ҫ�ؼ��ؽ���
	        	Message msg = mHandler.obtainMessage(0); 
                mHandler.sendMessage(msg);  
	          }
	        }
	    });
	
		// singleThreadExecutor.shutdownNow();//�ر��̳߳�
	}

	 private class MyHandler extends Handler 
     { 
        @Override 
         public void handleMessage(Message msg) { 
             super.handleMessage(msg); 
//             drawWidget.showMessageBox("Ҫ�ؼ�����ϣ�");     
             Toast.makeText(drawWidget.context,"Ҫ�ؼ�����ϣ����Ƽ���Ҫ��"+num+"����",Toast.LENGTH_SHORT).show();
        } 
     }

}

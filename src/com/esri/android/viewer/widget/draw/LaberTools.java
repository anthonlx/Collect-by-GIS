package com.esri.android.viewer.widget.draw;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Color;
import cn.com.esrichina.spatialitelib.LocalQuery;
import cn.com.esrichina.spatialitelib.LocalVectorTask;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.TextSymbol;

public class LaberTools {
	
 	public static void addLaber(final DrawWidget drawwidget){
 		drawwidget.laberGraphicsLayer.removeAll();//���laberǰ���ͼ��
 		ExecutorService  singleThreadExecutor = Executors.newSingleThreadExecutor();
 		singleThreadExecutor.execute(new Runnable() {
	        @Override
			public void run() {		
			LocalQuery query = new LocalQuery();
			query.setTableName(drawwidget.editLayerName);//���ñ���		
			query.setOutFields(new String[]{drawwidget.LaberNameStr});
			query.setReturnGeometry(true);
			//Ĭ�ϼ���ȫ������ 
			query.setWhere("F_STATE!=3");//����ʾ�Ѿ����ɾ����
			Geometry geo = drawwidget.mapView.getExtent();
			//���ݿռ��ѯ����
			query.setGeometry(geo);
			//���ݿռ��ϵ����
			query.setSpatialRelationship("Intersects");
			
			//ʵ����һ��LocalVectorTask��Ķ���ʱ���乹�캯�����Զ�����openDatabase()������Ĭ�ϴ����ݿ⣻
			LocalVectorTask queryTask = new LocalVectorTask(drawwidget.dbFile);
			
			// ��ȡ���ݣ������ڵ�ͼ��
			FeatureSet featureSet;
				try {
					featureSet = queryTask.query(query);
					Graphic[] graphics = featureSet.getGraphics();									
					for(int i=0;i<graphics.length;i++)
					{
						String laber = graphics[i].getAttributeValue(drawwidget.LaberNameStr).toString();
						String type = graphics[i].getGeometry().getType().name();
						//����TextSymbol
						TextSymbol  txtsymbol = new TextSymbol(16, laber, Color.BLACK);
						Point p =null;
						if("POLYLINE".equals(type)||"LINESTRING".equals(type)){
							Polyline polyline = (Polyline)graphics[i].getGeometry(); 
					        int n_end = polyline.getPointCount();//�߶ε����
							Point p_b=polyline.getPoint(0);//��ȡ�߶����
							Point p_e = polyline.getPoint(n_end-1);//��ȡ�߶��յ�
							Point P_center = new Point((p_b.getX()+p_e.getX())/2,(p_b.getY()+p_e.getY())/2);//��ȡ����֮������ĵ�
							p = P_center;
						}else if("POLYGON".equals(type)){
							Polygon polygon = (Polygon)graphics[i].getGeometry();
							p=polygon.getPoint(0);
						}else if("POINT".equals(type)){
							Point point = (Point)graphics[i].getGeometry();
						    p=point;
						}
						//����GraphicҪ��
						Graphic graphic = new Graphic(p,txtsymbol);  
						//Ҫ�������ͼ��
						drawwidget.laberGraphicsLayer.addGraphic(graphic); 
					}																
				} catch (Exception e) {
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
				}
	        }
	    });
 		singleThreadExecutor.shutdownNow();
	}

	public static void clearLaber(DrawWidget drawwidget){
		drawwidget.laberGraphicsLayer.removeAll();
	}
}

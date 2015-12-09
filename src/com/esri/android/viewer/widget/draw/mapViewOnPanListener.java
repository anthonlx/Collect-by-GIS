package com.esri.android.viewer.widget.draw;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnPanListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Polygon;

public class mapViewOnPanListener implements OnPanListener {


	
	public static DrawWidget drawWidget = null;
	public static GraphicsLayer mGraphicsLayer = null;
//	GeoDateTool geotools = null;
	
	public mapViewOnPanListener(DrawWidget d,GraphicsLayer gra) {
		drawWidget = d;
		mGraphicsLayer =gra;
//		geotools = new GeoDateTool(drawWidget);
	}

	@Override
	public void postPointerMove(float arg0, float arg1, float arg2, float arg3) {
		// TODO Defines the action after the map default move handling.
		
	}

	@Override
	public void postPointerUp(float arg0, float arg1, float arg2, float arg3) {
		//TODO Defines the action after the map default pointer up handling.
//		if (Math.abs(arg0-arg2)>3||Math.abs(arg1-arg3)>3){//����������һ������3����ִ��
			LaberTools.clearLaber(drawWidget);//���laberͼ��
			if (this.drawWidget.isActive) {
				//Toast.makeText(this.drawWidget.context, "ƽ���¼���", Toast.LENGTH_SHORT).show();
				double scale = drawWidget.mapView.getScale();
//				mGraphicsLayer.removeAll();//���ͼ������	
				if (scale < LoadState.scale) {//�̶�������������ʾ					
					if (drawWidget.sw.isChecked()) {
						if (drawWidget.mGraphicsLayer.getNumberOfGraphics()>0) {
							LaberTools.addLaber(drawWidget);
						}
					}		
					
				}
//			}
		}	 
	}

	@Override
	public void prePointerMove(float arg0, float arg1, float arg2, float arg3) {
		// TODO Defines the action before the map default move handling.

	}

	@Override
	public void prePointerUp(float arg0, float arg1, float arg2, float arg3) {
		// TODO Defines the action before the map default pointer up handling.

	}

}

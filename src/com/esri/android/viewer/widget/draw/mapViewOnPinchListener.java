package com.esri.android.viewer.widget.draw;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnPinchListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Polygon;

public class mapViewOnPinchListener implements OnPinchListener {
		
	private static DrawWidget drawWidget =null;
	private static GraphicsLayer mGraphicsLayer=null;
//	GeoDateTool geotools =null;
	
	public mapViewOnPinchListener(DrawWidget d,
			GraphicsLayer gra) {
		drawWidget =d;
		mGraphicsLayer =gra;
//		geotools = new GeoDateTool(drawWidget);
	}

	@Override
	public void postPointersDown(float arg0, float arg1, float arg2,
			float arg3, double arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postPointersMove(float arg0, float arg1, float arg2,
			float arg3, double arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postPointersUp(float arg0, float arg1, float arg2, float arg3,
			double arg4) {
		 //Toast.makeText(this.drawWidget.context,"�����¼���",Toast.LENGTH_SHORT).show();
		double scale = drawWidget.mapView.getScale();
		drawWidget.draw_txtScale.setText("������1:"+String.valueOf((int)scale));
		LaberTools.clearLaber(drawWidget);//���Laberͼ��
		if (DrawWidget.isActive) {//�ɼ�����״̬
//			mGraphicsLayer.removeAll();//���ͼ������		
			if(scale<LoadState.scale){				
				if (drawWidget.sw.isChecked()) {						
					if (drawWidget.mGraphicsLayer.getNumberOfGraphics()>0) {
						LaberTools.addLaber(drawWidget);
					}
				}		
			}else{
				LaberTools.clearLaber(drawWidget);
			}
		}

	}

	@Override
	public void prePointersDown(float arg0, float arg1, float arg2, float arg3,
			double arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void prePointersMove(float arg0, float arg1, float arg2, float arg3,
			double arg4) {
		// TODO Auto-generated method stub

	}

	@Override
	public void prePointersUp(float arg0, float arg1, float arg2, float arg3,
			double arg4) {
		// TODO Auto-generated method stub
	}
	
}
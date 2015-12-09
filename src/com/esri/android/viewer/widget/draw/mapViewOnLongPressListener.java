package com.esri.android.viewer.widget.draw;

import android.graphics.Color;
import android.view.View;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.viewer.ViewerActivity;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;

@SuppressWarnings("serial")
public class mapViewOnLongPressListener implements OnLongPressListener {

	DrawWidget drawWidget =null;
	GraphicsLayer graphicsLayer =null;
	View calloutView =null;
	 
	public mapViewOnLongPressListener(DrawWidget d, GraphicsLayer layer,  View v) {
		// TODO �Զ����ɵĹ��캯�����
		drawWidget = d;
		graphicsLayer = layer;
		calloutView = v;
	}

	public boolean onLongPress(float x, float y) {
		try {
			// TODO �Զ����ɵķ������
			int[] grilist = graphicsLayer.getGraphicIDs(x, y, 30);//Ҫ�ز�ѯ��������Χ		
			int count = grilist.length;
			if(count!=0){
				Graphic graphic = graphicsLayer.getGraphic(grilist[0]);
				String F_ID= (String) graphic.getAttributeValue("FEATUREID");		
				if (F_ID!=null) {
					drawWidget.featureID = F_ID;
					drawWidget.GraUID = graphic.getUid();
					int[] sel = { grilist[0] };//��ȡѡ�еĵ�һ��Ҫ��
					graphicsLayer.clearSelection();//�����ѡ��Ҫ��
					graphicsLayer.setSelectedGraphics(sel, true);
					graphicsLayer.setSelectionColor(Color.YELLOW);
					//����callout����
					Point p = null; //��¼��������ʼ��λ
					Geometry geo = graphic.getGeometry();
					String geotype = geo.getType().name().toString();
					if ("POINT".equals(geotype)) {
						p = (Point) geo;
					} else if ("POLYLINE".equals(geotype)) {
						Polyline line = (Polyline) geo;
						p = line.getPoint(0);//Ĭ��ȥ��һ����		
					} else if ("POLYGON".equals(geotype)) {
						Polygon polygon = (Polygon) geo;
						p = polygon.getPoint(0);
					} else if ("ENVELOPE".equals(geotype)) {
					}
					//drawWidget.mapView.centerAt(p, true);
					drawWidget.showCallout(p, calloutView);
					DrawWidget.calloutisActive = true;
					ViewerActivity.linecalloutView.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			drawWidget.showMessageBox(e.toString());
		}
		return false;  
		
	}

}

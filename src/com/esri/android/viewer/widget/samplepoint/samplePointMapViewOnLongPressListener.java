package com.esri.android.viewer.widget.samplepoint;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.core.map.Graphic;

public class samplePointMapViewOnLongPressListener implements
		OnLongPressListener {

	SamplePointWidget samplePointWidget =null;
	GraphicsLayer samplePointGraphicsLayer =null;
	
	public samplePointMapViewOnLongPressListener(
			SamplePointWidget widget, GraphicsLayer mGraphicsLayer) {
		samplePointGraphicsLayer = mGraphicsLayer;
		samplePointWidget = widget;
	}

	@Override
	public boolean onLongPress(float x, float y) {
		// TODO Auto-generated method stub
		samplePointGraphicsLayer.clearSelection();//�����ѡ��Ҫ��
		int[] grilist_sampoint = samplePointGraphicsLayer.getGraphicIDs(x, y, 30);//Ҫ�ز�ѯ��������Χ
		int count_sampoint = grilist_sampoint.length;
		if(count_sampoint!=0){
			int[] select = { grilist_sampoint[0] };//��ȡѡ�еĵ�һ��Ҫ��
			samplePointGraphicsLayer.setSelectedGraphics(select, true);
			samplePointGraphicsLayer.setSelectionColor(Color.YELLOW);
			Graphic graphic = samplePointGraphicsLayer.getGraphic(select[0]);
			String PHID = (String) graphic.getAttributeValue("PHID");//Ҫ��ID
			int GraUID = graphic.getUid();//Ҫ����ͼ����ID
			Intent intent = new Intent(samplePointWidget.context, SamplePointAttributeActivity.class);    	      
			Bundle bundle = new Bundle();
			bundle.putString("PHID", PHID);
			bundle.putInt("GraID", GraUID);
		    intent.putExtras(bundle);
			samplePointWidget.context.startActivity(intent);
		}
		return false;
	}
}

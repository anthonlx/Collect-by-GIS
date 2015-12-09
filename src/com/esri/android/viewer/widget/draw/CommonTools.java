package com.esri.android.viewer.widget.draw;

import jsqlite.Database;
import jsqlite.Exception;
import jsqlite.TableResult;

import android.util.Log;

import com.esri.core.geometry.Geometry;
import com.esri.core.map.Graphic;

public class CommonTools {

	private static String CONST_FEATURE_STATUS_ORIGINAL = "1";//Ҫ��״̬��δ�ı�
	private static String CONST_FEATURE_STATUS_ADDED = "2";//Ҫ��״̬���ɼ�������
	private static String CONST_FEATURE_STATUS_DELETED = "3";//Ҫ��״̬����ɾ����
	private static String CONST_FEATURE_STATUS_EDITED = "4";//Ҫ��״̬���˲�༭��
	
	 public static void  updateFeatureState(String dbPath,String editLayerName,String featureID){
		 Database db = new Database();
		 String sqlStr = "UPDATE "
					+ editLayerName
					+ " SET "
					+ "F_STATE=  "+CONST_FEATURE_STATUS_EDITED
					+ " WHERE FEATUREID = '"
					+ featureID + "'";
		//�������ݿ�ֵ֮ǰ�ж�Ҫ���Ƿ�Ϊ����������Ҫ�ز��ı�F_STATEֵ
			boolean isNewAdd = false;
			String sqlstr_isadd = "Select F_STATE from " + editLayerName + " WHERE FEATUREID = '"
					+ featureID + "'";
		try {
			db.open(dbPath, 2);
			TableResult tb =db.get_table(sqlstr_isadd);
			String[] s = tb.rows.get(0);
			String st =s[0];
			if(st.equals(CONST_FEATURE_STATUS_ORIGINAL)) isNewAdd =true;
			if(isNewAdd){
				db.exec(sqlStr, null);//Ҫ��״̬����
				db.close();
				//����Ҫ����ɫ
				Graphic gra = CommonValue.mGraphicsLayer.getGraphic(DrawWidget.GraUID);
				Geometry geometry = gra.getGeometry();
				if ("POINT".equals(geometry.getType().toString())) {
					CommonValue.mGraphicsLayer.updateGraphic(DrawWidget.GraUID,
							FeatureSymbol.pointSymbol_update);
				} else if ("POLYLINE".equals(geometry.getType().toString())) {
					CommonValue.mGraphicsLayer.updateGraphic(DrawWidget.GraUID,
							FeatureSymbol.lineSymboll_update);
				} else if ("POLYGON".equals(geometry.getType().toString())) {
					CommonValue.mGraphicsLayer.updateGraphic(DrawWidget.GraUID,
							FeatureSymbol.polygonSymbol_update);
				}
			}
		} catch (Exception e) {
			Log.e("Ҫ��״̬����", "����ʧ�ܣ�");
		}
	
	 }
}

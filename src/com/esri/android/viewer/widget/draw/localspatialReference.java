package com.esri.android.viewer.widget.draw;

import com.esri.core.geometry.SpatialReference;

public class localspatialReference {
	//�����2000������ϵ
	public static SpatialReference spatialReferencePM=SpatialReference.create(4490);
//	public static SpatialReference spatialReferencePM=SpatialReference.create(102100);
	//�޸�  2015.12.14  by David.Ocean  ��arcmap������ʱ�򣬲���102100������3857.  ���ҪŪ�������Ϊ��.sqlite�У��ϸ����ֶν����жϡ�
//	public static SpatialReference spatialReferencePM=SpatialReference.create(3857); 
//	public static SpatialReference spatialReferencePM=SpatialReference.create(2230); 
	public static SpatialReference getTaskSpatialReference(int sr_wkid){
		SpatialReference sr=SpatialReference.create(sr_wkid);
		return sr;
		
	}
	
	
}

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

package com.esri.android.viewer.config;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.viewer.Constant;
import com.esri.android.viewer.Log;
import com.esri.android.viewer.ViewerApp;

import android.content.Context;

public class XmlParser {


	private final static String XML_NODE_MAP = "map"; 
	private final static String XML_NODE_MAP_ATTRIBUTE_EXTENT = "initialextent"; 
	
	private final static String XML_NODE_MAP_BASEMAP = "basemaps";
	private final static String XML_NODE_MAP_OPERATIONAL_LAYER = "operationallayers";
	
	private final static String XML_NODE_MAP_LAYER = "layer"; 
	private final static String XML_NODE_MAP_LAYER_ATTRIBUTE_LABEL = "label";
	private final static String XML_NODE_MAP_LAYER_ATTRIBUTE_TYPE = "type";
	private final static String XML_NODE_MAP_LAYER_ATTRIBUTE_URL = "url";
	private final static String XML_NODE_MAP_LAYER_ATTRIBUTE_VISIBLE = "visible";
	private final static String XML_NODE_MAP_LAYER_ATTRIBUTE_ALPHA = "alpha";
	
	private final static String XML_NODE_WIDGETCONTAINER = "widgetcontainer"; 
	private final static String XML_NODE_MENUS = "menus";
	
	private final static String XML_NODE_WIDGET = "widget";  
	private final static String XML_NODE_WIDGET_ATTRIBUTE_LABEL = "label"; 
	private final static String XML_NODE_WIDGET_ATTRIBUTE_ICON = "icon";
	private final static String XML_NODE_WIDGET_ATTRIBUTE_CONFIG = "config";
	private final static String XML_NODE_WIDGET_ATTRIBUTE_CLASSNAME = "classname"; 
	
	/**
	 * @param c
	 * @return
	 * @throws Exception
	 */
	public static ConfigEntity getConfig(Context c) throws Exception 
	{
		ConfigEntity config = new ConfigEntity();
		XmlPullParserFactory pullParserFactory = XmlPullParserFactory.newInstance();
		XmlPullParser pullParser = pullParserFactory.newPullParser();
		InputStream input;
		int widgetid = 10000;

		try
		{
			input = c.getResources().getAssets().open(Constant.CONFIG_FILE);
		}
		catch(Exception e){e.printStackTrace(); return null;}
		pullParser.setInput(input, "UTF-8");
		
		List<LayerEntity> mListLayer = null;
		List<WidgetEntity> mListWidget = null;
		boolean isWidgetContainer = false;
		boolean isBaseMap = true;
		String nodeName = "", temp = "";
		try
		{
			int eventType = pullParser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) 
			{
				nodeName = pullParser.getName();
				switch (eventType) 
				{
					case XmlPullParser.START_DOCUMENT:
						break;
					case XmlPullParser.START_TAG:
						if (XML_NODE_MAP.equals(nodeName)) 
						{
							if(pullParser.getAttributeValue(null,XML_NODE_MAP_ATTRIBUTE_EXTENT) != null)
							{
								temp = pullParser.getAttributeValue(null,XML_NODE_MAP_ATTRIBUTE_EXTENT);
								if(!temp.equals(""))
								{
									config.setMapExtent(temp);
								}
							}
						}
						else if (XML_NODE_MAP_BASEMAP.equals(nodeName)) 
						{
							if(mListLayer == null) mListLayer = new ArrayList<LayerEntity>();
							isBaseMap = true;
						}
						else if (XML_NODE_MAP_OPERATIONAL_LAYER.equals(nodeName)) 
						{
							if(mListLayer == null) mListLayer = new ArrayList<LayerEntity>();
							isBaseMap = false;//�����б���Ŀ����
						}
						else if (XML_NODE_MAP_LAYER.equals(nodeName)) 
						{
							//��������ļ������ߵ�ͼ����
//							LayerEntity entity = new LayerEntity();							
//							entity.setLabel(pullParser.getAttributeValue(null,XML_NODE_MAP_LAYER_ATTRIBUTE_LABEL).trim());
//							entity.setType(pullParser.getAttributeValue(null,XML_NODE_MAP_LAYER_ATTRIBUTE_TYPE).trim());
//							entity.setURL(pullParser.getAttributeValue(null,XML_NODE_MAP_LAYER_ATTRIBUTE_URL).trim());
//							entity.setVisible(getBoolean(pullParser.getAttributeValue(null,XML_NODE_MAP_LAYER_ATTRIBUTE_VISIBLE)));
//							entity.setAlpha(getFloat(pullParser.getAttributeValue(null,XML_NODE_MAP_LAYER_ATTRIBUTE_ALPHA)));
//							entity.setProperty(isBaseMap?LayerEntity.EnumProperty.Basemap:LayerEntity.EnumProperty.Operational);							
//							if(mListLayer != null) {
//								//mListLayer.add(entity); //ȡ��������ߵ�ͼ
//							}							
						}
						else if (XML_NODE_WIDGETCONTAINER.equals(nodeName)) 
						{
							if(mListWidget == null) mListWidget = new ArrayList<WidgetEntity>();
							isWidgetContainer = true;
						} 
						else if (XML_NODE_WIDGET.equals(nodeName)) 
						{
							WidgetEntity entity = new WidgetEntity();
							entity.setId(widgetid++);
							entity.setLabel(pullParser.getAttributeValue(null,XML_NODE_WIDGET_ATTRIBUTE_LABEL));
							entity.setClassname(pullParser.getAttributeValue(null,XML_NODE_WIDGET_ATTRIBUTE_CLASSNAME));
							entity.setIconName(pullParser.getAttributeValue(null,XML_NODE_WIDGET_ATTRIBUTE_ICON));
							
							if(pullParser.getAttributeValue(null,XML_NODE_WIDGET_ATTRIBUTE_CONFIG) != null)
							{
								temp = pullParser.getAttributeValue(null,XML_NODE_WIDGET_ATTRIBUTE_CONFIG);
								if(!temp.equals("")) entity.setConfig(temp);
							}
							
							entity.setProperty(isWidgetContainer?WidgetEntity.EnumProperty.WidgetContainer:WidgetEntity.EnumProperty.Menus);
							
							if(mListWidget != null) mListWidget.add(entity);
						} 
						else if (XML_NODE_MENUS.equals(nodeName)) 
						{
							if(mListWidget == null) mListWidget = new ArrayList<WidgetEntity>();
							isWidgetContainer = false;
						} 
						break;
					case XmlPullParser.END_TAG:
						break;
				}
				eventType = pullParser.next();
			}
					
			//��ȡϵͳ������ͼĿ¼
			ViewerApp appState = ((ViewerApp)c.getApplicationContext()); 
			com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
			String BaseMapPath = path.baseMapFilePath.toString();							
			AddBaseMap(mListLayer, BaseMapPath);
			
			//��ȡSD���е�ͼ�ļ�
			String sdBaseMapPath = path.extsdcardbaseMapFilePath.toString();
			File _pathext = new File(sdBaseMapPath); 
			if(_pathext.exists()){
				AddBaseMap(mListLayer, sdBaseMapPath);
			}
				
			config.setListLayer(mListLayer);
			config.setListWidget(mListWidget);
			input.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return config;
	}

	/**
	 * ��ӵ�ͼ�����б���
	 * @param mListLayer
	 * @param BaseMapPath
	 */
	private static void AddBaseMap(List<LayerEntity> mListLayer,String BaseMapPath) {
		//��ȡ�����ļ�����tpk�ļ�
		if(BaseMapPath!=""){
			//��ȡ���ߵ�ͼ�б�
			com.esri.android.viewer.tools.fileUtil fileutil = new com.esri.android.viewer.tools.fileUtil();
			List<com.esri.android.viewer.tools.fileUtil.file> BaseMap_Tpk_File_List  = fileutil.getFileDir(BaseMapPath,"all");//����Ϊ�ɶ�����allȫ�� .tpk
			String pathStr = "file://";
			for(int i=0;i<BaseMap_Tpk_File_List.size();i++)
			{
				com.esri.android.viewer.tools.fileUtil.file file = BaseMap_Tpk_File_List.get(i);//��ȡ��ͼ���ļ�����·��
				//����ͼ������
				LayerEntity entitylocal = new LayerEntity();
				entitylocal.setLabel(file.item);
				entitylocal.setType("local");
				if (i==0) {
					entitylocal.setVisible(false);//Ĭ�ϲ���ʾ
				}else{
					entitylocal.setVisible(false);//����ͼ�㲻��ʾ
				}
				entitylocal.setURL(pathStr+file.path);			
				entitylocal.setProperty(LayerEntity.EnumProperty.Operational);//����ͼ������ ���ߵ�ͼ or ���ߵ�ͼ	
				
				mListLayer.add(entitylocal);
				
			}
		}
	}
	
	private static float getFloat(String alpha)
	{
		float value = 1;
		if(alpha == null || alpha.equals("")) alpha = "1";
		try
		{
			value = Float.parseFloat(alpha);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return value;
	}
	private static boolean getBoolean(String visible)
	{
		boolean value = false;
		if(visible == null || visible.equals("")) visible = "false";
		try
		{
			value = Boolean.parseBoolean(visible);
		}
		catch(Exception e)
		{
			value = false;
			e.printStackTrace();
		}
		return value;
	}
	
}

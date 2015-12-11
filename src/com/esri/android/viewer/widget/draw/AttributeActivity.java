package com.esri.android.viewer.widget.draw;

import java.util.ArrayList;
import java.util.List;
import jsqlite.Database;
import jsqlite.TableResult;

import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerActivity;
import com.esri.android.viewer.module.NoScrollListView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.map.Graphic;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AttributeActivity extends Activity {

//	public static String LAYER_TABLE_LAYOUT = "layer_table_layout";//Ԫ���ݱ����洢�ֶ���
//	public static String LAYER_TABLE_LAYOUT_TABLENAME_FIELD="table_name";//�����ֶ�
//	public static String LAYER_TABLE_LAYOUT_COLUMUNAME_FIELD="column_name";//�ֶ����ֶ�	
//	public static String LAYER_TABLE_FILED_ALIAS = "SYS_TABLE_ALIAS";//�ֶα���
	
	private String taskPackageSimplePath =null;
	private String editLayerName = null;  //���༭����
	private String featureID = null;
	private String dbFilePath = null;
		
	private NoScrollListView attributeListView =null;
	private ArrayList<Attribute> attributelist = new ArrayList<Attribute>();//�����б�
	private String[] strName ;//�ֶ����б�	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attribute);
		//��ȡ��һ��ҳ��Ĵ�ֵ
	    Bundle bundle = getIntent().getExtras();
	    taskPackageSimplePath = bundle.getString("taskPackageSimplePath");
	    editLayerName = bundle.getString("editLayerName");
	    featureID = bundle.getString("featureID");
	    dbFilePath = taskPackageSimplePath;
	    
	    attributeListView =(NoScrollListView) findViewById(R.id.esri_androidviewer_attribute_listView);
 
	    //��ѯ����Table���ֶ��б�
		SQLiteDatabase mDb = SQLiteDatabase.openDatabase(dbFilePath, null, 0);
		String sqlStr  = "SELECT * FROM "+editLayerName+ " WHERE FEATUREID = '"+featureID+"'";
		Cursor cursor=mDb.rawQuery(sqlStr,null);
		cursor.moveToFirst();
		strName = cursor.getColumnNames();	
		for(int i=0;i<strName.length;i++){
			try {
				Attribute attribute = new Attribute();
				attribute.key = strName[i];
				int index = cursor.getColumnIndex(strName[i]);
				attribute.value = cursor.getString(index);
				attribute.alias = GetFiledAlias(editLayerName,attribute.key);//��ȡ�ֶα���
				if ((!"FEATUREID".equals(strName[i]))&&(!"F_STATE".equals(strName[i]))&&(!"OBJECTID".equals(strName[i]))) {// ����ʾ���ֶ�
					attributelist.add(attribute);
				}
			} catch (Exception e) {
				if (!"Shape".equals(strName[i])) {
					// TODO: handle exception
					Attribute attribute = new Attribute();
					attribute.key = "������ṹ�쳣";
					attribute.value = "������ṹ�쳣";
					attributelist.add(attribute);
				}
			}
		}
		cursor.close();
	    
        AttributeAdapter adapter = new AttributeAdapter(this.getLayoutInflater());
        //��Ӳ�����ʾ
        attributeListView.setAdapter(adapter);
	}

	/**
	 * ��ȡ�����ֶεı���
	 * @param editLayerName2 ����
	 * @param key �ֶ���
	 * @return �ֶα���  ���ֶα���Ϊ���򷵻ؿ�
	 */
	 private String GetFiledAlias(String editLayerName2, String key) {
		try {
			// TODO Auto-generated method stub
			SQLiteDatabase mDb = SQLiteDatabase.openDatabase(dbFilePath, null,0);
			String sqlStr = "SELECT FILEDALIAS FROM  SYS_TABLE_ALIAS"
					+ " WHERE TABLENAME = '" + editLayerName2 + "'"
					+ " AND FILEDNAME ='" + key + "'";
			Cursor cursor = mDb.rawQuery(sqlStr, null);
			cursor.moveToFirst();
			String alias = cursor.getString(cursor.getColumnIndex("FILEDALIAS"));
			mDb.close();
			if (alias.equals("")) {
			} else {
				return alias;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "null";
	}

	 /**
	  * �ֶ���
	  * @author lq
	  */
	public final class Attribute{//����
		 public String key;  //�ֶ���
		 public String value;  //�ֶ�ֵ
		 public String alias;  //�ֶα���
		 
	 }
	
	  public final class ViewHolder{//�б����
	        public TextView title;
	        public EditText editText;
	    }
		
	  public class AttributeAdapter extends BaseAdapter{
		        private LayoutInflater mInflater;

		        public AttributeAdapter(LayoutInflater Inflater){
		            this.mInflater = Inflater;
		        }

		        public int getCount() {
		            // TODO Auto-generated method stub
		            return attributelist.size();
		        }

		        public Object getItem(int arg0) {
		            // TODO Auto-generated method stub
		            return null;
		        }

		        public long getItemId(int arg0) {
		            // TODO Auto-generated method stu
		            return 0;
		        }

		        public void refreshData(){
		        	
		        	notifyDataSetChanged();//ˢ������
		        } 
		        
		        public View getView( final int position, View convertView, ViewGroup parent) {
		            ViewHolder holder = null;
		            if (convertView == null) {
		                holder=new ViewHolder();  
		                convertView = mInflater.inflate(R.layout.view_list_attribute_item_txt, null);
		                holder.title = (TextView)convertView.findViewById(R.id.view_list_attribute_item_txt_name);
		                holder.editText = (EditText)convertView.findViewById(R.id.view_list_attribute_item_edittxt_value);
		                convertView.setTag(holder);
		            }else {
		                holder = (ViewHolder)convertView.getTag();
		            }
		            String alias = " ("+attributelist.get(position).alias+")";
		            holder.title.setText(attributelist.get(position).key+alias);
		            holder.editText.setText(attributelist.get(position).value);
		            holder.editText.setFocusable(false);
		            holder.editText.setOnClickListener(new oneditClickListener(holder,position));
		            //holder.editText.addTextChangedListener(new myTextWatcher(holder,position));		            
		            return convertView;
		        }
		        
		    	public class oneditClickListener implements OnClickListener {

				    ViewHolder holder;
				    int position;//ID
					public oneditClickListener(ViewHolder h, int p) {
					 // TODO �Զ����ɵĹ��캯�����
						holder = h;
						position = p;
				    }

					@Override
					public void onClick(View v) {
						// TODO �Զ����ɵķ������
						int f_dicid =-1;//Ĭ���ֵ������
						//��ȡ�ֶ�����
						String strtitle =attributelist.get(position).key;
						//�������ݿ�����
						SQLiteDatabase mDb = SQLiteDatabase.openDatabase(dbFilePath, null, 0);
		        		String sqlStr  = "select F_DICID from SYS_DIC_FIELD_REL where F_TABLENAME ='"+editLayerName+"' and F_FIELDNAME ='"+strtitle+"'";
		        		Cursor cursor = mDb.rawQuery(sqlStr, null);
		        		while (cursor.moveToNext()) {
		        			f_dicid = cursor.getInt(cursor.getColumnIndex("F_DICID"));
						}
		        		
		        		if(f_dicid!=-1){
		        			//�����ֵ�����ֵ��б�
		        			String sqldic  = "select F_ITEMNAME from SYS_DIC_ITEMS where F_DICID ="+f_dicid;
		            		Cursor cursordic = mDb.rawQuery(sqldic, null);
		            		ArrayList<String> lsdic = new ArrayList<String>();
		            		while (cursordic.moveToNext()) {
		            			String f_itemname = cursordic.getString(cursordic.getColumnIndex("F_ITEMNAME"));
		            			lsdic.add(f_itemname);
		    				}        		
		            		setValueByList(lsdic,strtitle);
		        		}else{
		        			setValueByText(strtitle);  
		        		}
		        		mDb.close();
					
					}

					private void setValueByList(final List<String> lsdic, final String strtitle) {
						// TODO �Զ����ɵķ������
						LayoutInflater inflater = LayoutInflater.from(AttributeActivity.this);  
						final View listEntryView = inflater.inflate( R.layout.attribute_dialog_listview, null);  
						final ListView edtInput=(ListView)listEntryView.findViewById(R.id.attribute_dialog_listview_list); 		
						String strTitle = attributelist.get(position).key +" ("+attributelist.get(position).alias+")";
						new AlertDialog.Builder(AttributeActivity.this).setTitle(strTitle)		
		                .setSingleChoiceItems(new ArrayAdapter<String>(AttributeActivity.this, android.R.layout.simple_expandable_list_item_1,lsdic)
		                		, 0, new DialogInterface.OnClickListener() {
		                        @Override
		                        public void onClick(DialogInterface dialog, int which) {                    	
		                            dialog.dismiss();
		                           //�������ݿ�ֵ                    
		                            String str = lsdic.get(which);                         					     						
		    				        try {
		    				        	if (!holder.editText.getText().toString().equals(str)) {//�б仯ִ��
											//�������ݿ�ֵ        						
											Database db = new Database();
											db.open(dbFilePath, 2);
											//�������ݿ�ֵ֮ǰ�ж�Ҫ���Ƿ�Ϊ����������Ҫ�ز��ı�F_STATEֵ
											boolean isNewAdd = false;
											String sqlstr_isadd = "Select F_STATE from " + editLayerName + " WHERE FEATUREID = '"
													+ featureID + "'";
											TableResult tb =db.get_table(sqlstr_isadd);
											String[] s = tb.rows.get(0);
											int st = Integer.parseInt(s[0]);
											if(st==2) isNewAdd =true;		
							
											String sqlStr ="";
											if(isNewAdd){
												sqlStr = "UPDATE " + editLayerName
														+ " SET "
														+ attributelist.get(position).key
														+ "='" + str + "'"
														+ " WHERE FEATUREID = '"
														+ featureID + "'";
											}else{
												sqlStr = "UPDATE " + editLayerName
														+ " SET "
														+ attributelist.get(position).key
														+ "='" + str + "'"
														+ ",F_STATE= 4 "
														+ " WHERE FEATUREID = '"
														+ featureID + "'";
											}
												
											//ϵͳ��־���============================================================
											String oldStr = "null";
											if (holder.editText.getText().toString().equals("")) {

											} else {
												oldStr = holder.editText.getText().toString();//ԭʼֵ
											}
											String time = com.esri.android.viewer.tools.sysTools.getTimeNow2();
											String sqllog = "INSERT INTO SYS_LOGS(F_USERID,F_TIME,F_LAYER,F_FEATURE,F_ACTION,F_REMARK) VALUES ("
													+ViewerActivity.userid+","
													+"'"+time+"',"
													+"'"+editLayerName+"',"
													+"'"+featureID+"',"
													+FeatureLogState.featureUpdate+","
													+"'"+attributelist.get(position).key+":"+oldStr+">"+str+"')";
											db.exec(sqlStr, null);
//											db.exec(sqllog, null);//��־  �޸� 2015-12-11 by David.Ocean ȡ��log��д��
											db.close();
											holder.editText.setText(str);
											Toast.makeText(AttributeActivity.this,
													"�ֶ�ֵ���³ɹ���", Toast.LENGTH_SHORT)
													.show();
											GraphicUpdateSymbol(isNewAdd);//����Ҫ�ط���			
										}
									} catch (jsqlite.Exception e) {
										// TODO �Զ����ɵ� catch ��
										e.printStackTrace();
										  Toast.makeText(AttributeActivity.this,"�ֶ�ֵ����ʧ�ܣ�"+e.toString(),Toast.LENGTH_SHORT).show();
									}
		                        }
		                })
		               .setNegativeButton("ȡ��",  
						        new DialogInterface.OnClickListener() {  
						            public void onClick(DialogInterface dialog, int whichButton) {  
						                
						            }  
						        })
		                .create().show();				
					}
							
					private void setValueByText(final String strtitle) {
						LayoutInflater inflater = LayoutInflater.from(AttributeActivity.this);  
						final View textEntryView = inflater.inflate( R.layout.attribute_dialog_textview, null);  
						final EditText edtInput=(EditText)textEntryView.findViewById(R.id.arrtibute_dialog_textview_edittext);  
						edtInput.setText(holder.editText.getText());
						final AlertDialog.Builder builder = new AlertDialog.Builder(AttributeActivity.this);  
						builder.setCancelable(false);  
						String strTitle = attributelist.get(position).key +" ("+attributelist.get(position).alias+")";
						builder.setTitle(strTitle);  
						builder.setView(textEntryView);  
						builder.setPositiveButton("ȷ��",  
						        new DialogInterface.OnClickListener() {  
						            public void onClick(DialogInterface dialog, int whichButton) {  
						            	String str = edtInput.getText().toString();
						            	  try {
				    				        	if (!holder.editText.getText().toString().equals(str)) {//�б仯�Ÿ���
													//�������ݿ�ֵ        						
													Database db = new Database();
													db.open(dbFilePath, 2);
													//�������ݿ�ֵ֮ǰ�ж�Ҫ���Ƿ�Ϊ����������Ҫ�ز��ı�F_STATEֵ
													boolean isNewAdd = false;
													String sqlstr_isadd = "Select F_STATE from " + editLayerName + " WHERE FEATUREID = '"
															+ featureID + "'";
													TableResult tb =db.get_table(sqlstr_isadd);
													String[] s = tb.rows.get(0);
													int st = Integer.parseInt(s[0]);
													if(st==2) isNewAdd =true;		
													
													String sqlStr ="";
													if(isNewAdd){
														sqlStr = "UPDATE "
																+ editLayerName
																+ " SET "
																+ attributelist.get(position).key + "='"
																+ str + "'"
																+ " WHERE FEATUREID = '"
																+ featureID + "'";
													}else{
														sqlStr = "UPDATE "
																+ editLayerName
																+ " SET "
																+ attributelist.get(position).key + "='"
																+ str + "'"
																+ ",F_STATE= 4 "
																+ " WHERE FEATUREID = '"
																+ featureID + "'";
													}
													
													//ϵͳ��־���============================================================
													String oldStr = "null";
													if (holder.editText.getText().toString().equals("")) {

													} else {
														oldStr = holder.editText.getText().toString();//ԭʼֵ
													}
													String time = com.esri.android.viewer.tools.sysTools.getTimeNow2();
													String sqllog = "INSERT INTO SYS_LOGS(F_USERID,F_TIME,F_LAYER,F_FEATURE,F_ACTION,F_REMARK) VALUES ("
															+ViewerActivity.userid+","
															+"'"+time+"',"
															+"'"+editLayerName+"',"
															+"'"+featureID+"',"
															+FeatureLogState.featureUpdate+","
															+"'"+attributelist.get(position).key+":"+oldStr+">"+str+"')";
													db.exec(sqlStr, null);
//													db.exec(sqllog, null);//��־  �޸� 2015-12-11 by David.Ocean ȡ��logsд��
													db.close();
													holder.editText.setText(str);
													Toast.makeText(
															AttributeActivity.this,
															"�ֶ�ֵ���³ɹ���",
															Toast.LENGTH_SHORT).show();
													GraphicUpdateSymbol(isNewAdd);//����Ҫ�ط���			
												}
											} catch (jsqlite.Exception e) {
												// TODO �Զ����ɵ� catch ��
												e.printStackTrace();
												  Toast.makeText(AttributeActivity.this,"�ֶ�ֵ����ʧ�ܣ�"+e.toString(),Toast.LENGTH_SHORT).show();
											}
						            }  
						        });  
						builder.setNegativeButton("ȡ��",  
						        new DialogInterface.OnClickListener() {  
						            public void onClick(DialogInterface dialog, int whichButton) {  
						                
						            }  
						        });  
						builder.show();
					}

					
			}
		        
		    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_attribute, menu);
		return false;
	}

	private void GraphicUpdateSymbol(boolean isNewAdd) {
		if (!isNewAdd) {
			Graphic gra = CommonValue.mGraphicsLayer
					.getGraphic(DrawWidget.GraUID);
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
		if(CommonValue.drawwitget!=null){
			CommonValue.drawwitget.recordWorkLocation();//��¼��ǰҪ�ر༭ʱλ��
		}
	}
	
}

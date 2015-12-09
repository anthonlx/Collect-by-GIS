package com.esri.android.viewer.widget.samplepoint;

import java.io.File;

import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerActivity;
import com.esri.android.viewer.ViewerApp;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SamplePointAttributeActivity extends Activity {
	
	private ImageView picimgview = null;//����ͼ
	private TextView txtPHID = null;//��Ƭ��ʶ��
	private TextView txtFILE = null;//��Ƭ�ļ���
	private TextView txtPHTM = null;//����ʱ��
	private TextView txtLONG = null;//����㾭��
	private TextView txtLAT = null;//�����γ��
	private TextView txtDOP = null;//λ�ö�λˮƽ����ˮƽ
	private TextView txtALT = null;//�����߳�
	private TextView txtMMODE = null;//��λ����
	private TextView txtSAT = null;//��λʱ�۲⵽����������
	private TextView txtAZIM = null;//��Ƭ��λ��
	private TextView txtAZIMR = null;//��Ƭ��λ�ǵĲο�����
	private TextView txtAZIMP = null;//��λ��׼ȷ�̶�
	private TextView txtDIST = null;//�������
	private TextView txtTILT = null;//���������
	private TextView txtROLL = null;//��������
	private TextView txtCC = null;//��Ƭ���������ĵ��������Ϣ���ʹ���
	private TextView txtREMARK = null;//��������������
	private TextView txtCREATOR = null;//������
	private TextView txtFOCAL = null;//35m��Ч����
	private Button  btnSave = null;//����
	private Button  btnCancle = null;//ȡ��
	private Button btnDelete =null;//ɾ��
	private SamplePointAttribute samplePointAttribute ;
	public static String picURL ="";
	
	private static String PHID ="";
	private static int GraID =-1;
	
	private static String pathdb;//���������ݿ�
	private static String pathphoto;//�������ļ���
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample_point_attribute);
		ViewerApp appState = ((ViewerApp)this.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		pathdb= path.samplepointPath+"/"
				+com.esri.android.viewer.tools.SystemVariables.PointPackageDirectory +"/"
				+com.esri.android.viewer.tools.SystemVariables.PointDB;
		pathphoto =  path.samplepointPath+"/"
				+com.esri.android.viewer.tools.SystemVariables.PointPackageDirectory +"/"
				+com.esri.android.viewer.tools.SystemVariables.PicturesDirectory;
		setView();
		//��ȡ��һ��ҳ��Ĵ�ֵ
	    Bundle bundle = getIntent().getExtras();
	    PHID = bundle.getString("PHID");
	    GraID = bundle.getInt("GraID");
	    samplePointAttribute = loadFromDB(PHID);
	    if(samplePointAttribute!=null){
	    	SetViewBysamplePointAttribute(samplePointAttribute);//������ʾ����
	    }
	}

	/**
	 * ������������Ϣ��ʼ��������Ϣ
	 * @param s ��������Ϣ
	 */
	private void SetViewBysamplePointAttribute(SamplePointAttribute s) {
		  //�����������
	    this.picURL =  s.getPicUrl();
		this.txtPHID.setText(s.getPHID());
		this.txtPHID.setEnabled(false);
		//this.txtPHID.setTextColor(Color.WHITE);
		this.txtFILE.setText(s.getFILE());
		this.txtFILE.setEnabled(false);
		//this.txtFILE.setTextColor(Color.WHITE);
		this.txtPHTM.setText(s.getPHTM());
		this.txtPHTM.setEnabled(false);
		//this.txtPHTM.setTextColor(Color.WHITE);
		//���ǲ���
		this.txtLONG.setText(s.getLONG());
		this.txtLONG.setEnabled(false);
		//this.txtLONG.setTextColor(Color.WHITE);
		this.txtLAT.setText(s.getLAT());
		this.txtLAT.setEnabled(false);
		//this.txtLAT.setTextColor(Color.WHITE);
		this.txtALT.setText(s.getALT());
		this.txtALT.setEnabled(false);
		//this.txtALT.setTextColor(Color.WHITE);
		this.txtMMODE.setText(s.getMMODE());
		this.txtMMODE.setEnabled(false);
		//this.txtMMODE.setTextColor(Color.WHITE);
		this.txtSAT.setText(s.getSAT());
		this.txtSAT.setEnabled(false);
		//this.txtSAT.setTextColor(Color.WHITE);
		this.txtAZIM.setText(s.getAZIM());
		this.txtAZIM.setEnabled(false);
		//this.txtAZIM.setTextColor(Color.WHITE);
		this.txtDOP.setText(s.getDOP());
		this.txtDOP.setEnabled(false);
		//this.txtDOP.setTextColor(Color.WHITE);
		//�������
		this.txtTILT.setText(s.getTILT());
		this.txtTILT.setEnabled(false);
		//this.txtTILT.setTextColor(Color.WHITE);
		this.txtROLL.setText(s.getROLL());
		this.txtROLL.setEnabled(false);
		//this.txtROLL.setTextColor(Color.WHITE);
		
		//��λ��
		this.txtAZIM.setText(s.getAZIM());
		this.txtAZIM.setEnabled(false);
		//this.txtAZIM.setTextColor(Color.WHITE);
		this.txtAZIMR.setText(s.getAZIMR());
		this.txtAZIMR.setEnabled(false);
		//this.txtAZIMR.setTextColor(Color.WHITE);
		this.txtAZIMP.setText(s.getAZIMP());
		this.txtAZIMP.setEnabled(false);
		//this.txtAZIMP.setTextColor(Color.WHITE);
		
		//������
		this.txtCREATOR.setText(s.getCREATOR());
		this.txtCREATOR.setEnabled(false);
		//this.txtCREATOR.setTextColor(Color.WHITE);
		
		this.txtFOCAL.setText(s.getFOCAL());
		this.txtFOCAL.setEnabled(false);
		//this.txtFOCAL.setTextColor(Color.WHITE);
		
		//�ֶ���д��Ŀ
		this.txtDIST.setText(s.getDIST());
		this.txtCC.setText(s.getCC());
		this.txtREMARK.setText(s.getREMARK());
		
	}

	/**
	 * ����Id��ȡ����������
	 * @param pHID2
	 * @return
	 */
	private SamplePointAttribute loadFromDB(String pHID2) {
		// TODO Auto-generated method stub
		 //������ݿⴴ���ɹ��򴴽����ݱ�
	   SQLiteDatabase mDb = SQLiteDatabase.openDatabase(pathdb, null,0);
	   Cursor cursor = mDb.query("PHOTO", new String[] {
			   "PHID","FILE","PHTM","LONG","LAT","DOP","ALT","MMODE","SAT","AZIM","AZIMR","AZIMP","DIST","TILT","ROLL","CC","REMARK","CREATOR","FOCAL" }, 
			   "PHID=?" ,new String[] {pHID2},null, null, null);
		if (cursor.moveToFirst()) {
			String PHID= cursor.getString(cursor.getColumnIndex("PHID"));
			String FILE = cursor.getString(cursor.getColumnIndex("FILE"));
			String PHTM = cursor.getString(cursor.getColumnIndex("PHTM"));
			String LONG = cursor.getString(cursor.getColumnIndex("LONG"));
			String LAT = cursor.getString(cursor.getColumnIndex("LAT"));
			String DOP = cursor.getString(cursor.getColumnIndex("DOP"));
			String ALT = cursor.getString(cursor.getColumnIndex("ALT"));
			String MMODE = cursor.getString(cursor.getColumnIndex("MMODE"));
			String SAT = cursor.getString(cursor.getColumnIndex("SAT"));
			String AZIM = cursor.getString(cursor.getColumnIndex("AZIM"));
			String AZIMR = cursor.getString(cursor.getColumnIndex("AZIMR"));
			String AZIMP = cursor.getString(cursor.getColumnIndex("AZIMP"));
			String DIST = cursor.getString(cursor.getColumnIndex("DIST"));
			String TILT = cursor.getString(cursor.getColumnIndex("TILT"));
			String ROLL = cursor.getString(cursor.getColumnIndex("ROLL"));
			String CC = cursor.getString(cursor.getColumnIndex("CC"));
			String REMARK = cursor.getString(cursor.getColumnIndex("REMARK"));
			String CREATOR = cursor.getString(cursor.getColumnIndex("CREATOR"));
			String FOCAL = cursor.getString(cursor.getColumnIndex("FOCAL"));
			String picURl = pathphoto+"/"+PHID+".jpg";
			SamplePointAttribute samAtt = new SamplePointAttribute();
			samAtt.setPicUrl(picURl);
			samAtt.setALT(ALT);
			samAtt.setAZIM(AZIM);
			samAtt.setAZIMP(AZIMP);
			samAtt.setAZIMR(AZIMR);
			samAtt.setCC(CC);
			samAtt.setCREATOR(CREATOR);
			samAtt.setDIST(DIST);
			samAtt.setDOP(DOP);
			samAtt.setFILE(FILE);
			samAtt.setFOCAL(FOCAL);
			samAtt.setLAT(LAT);
			samAtt.setLONG(LONG);
			samAtt.setMMODE(MMODE);
			samAtt.setPHID(PHID);
			samAtt.setPHTM(PHTM);
			samAtt.setREMARK(REMARK);
			samAtt.setROLL(ROLL);
			samAtt.setSAT(SAT);
			samAtt.setTILT(TILT);
			return samAtt;
		}
		return null;
	}

	private void setView(){
		this.picimgview =(ImageView) this.findViewById(R.id.activity_sample_point_attribute_Imgpicture);
		this.txtPHID = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtphid);
		this.txtFILE = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtfile);
		this.txtPHTM = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtphtm);
		this.txtLONG = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtlong);
		this.txtLAT = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtlat);
		this.txtDOP = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtdop);
		this.txtALT = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtalt);
		this.txtMMODE = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtmmode);
		this.txtSAT = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtsat);
		this.txtAZIM = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtazim);
		this.txtAZIMR = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtazimr);
		this.txtAZIMP = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtazimp);
		this.txtDIST = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtdist);
		this.txtTILT = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txttilt);
		this.txtROLL = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtroll);
		this.txtCC = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtcc);
		this.txtREMARK = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtremark);
		this.txtCREATOR = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtcreator);
		this.txtFOCAL = (TextView)this.findViewById(R.id.activity_sample_point_attribute_Txtfocal);
		this.btnSave = (Button)this.findViewById(R.id.activity_sample_point_attribute_Btnsave);
		this.btnCancle = (Button)this.findViewById(R.id.activity_sample_point_attribute_Btncancle);
		this.btnDelete = (Button)this.findViewById(R.id.activity_sample_point_attribute_Btndel);
		this.picimgview.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				File file = new File(picURL);
				Intent it = new Intent(Intent.ACTION_VIEW);
				it.setDataAndType(Uri.fromFile(file),"image/*");
				startActivity(it);
			}});
		
		this.btnSave.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				String phid = txtPHID.getText().toString();
				String dist =txtDIST.getText().toString();
				String cc = txtCC.getText().toString();
				String remark = txtREMARK.getText().toString();
				boolean bool =UpdateSamplePointDB(phid,dist,cc,remark);
				if(bool){
					AlertBox("��������³ɹ�");
					exitAttribute();
				}else{
					AlertBox("���������ʧ��");
				}
			}});
		
		this.btnCancle.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				exitAttribute();
			}});
		
		this.btnDelete.setOnClickListener(new OnClickListener (){
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder=new AlertDialog.Builder(SamplePointAttributeActivity.this);
				builder.setMessage("ȷ��ɾ����������?");
				builder.setCancelable(true);
				builder.setTitle("ϵͳ��ʾ");
				builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface arg0, int arg1) {
						
					}
				});
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface arg0, int arg1) {
						exitAttribute();
						boolean b1=com.esri.android.viewer.tools.fileTools.deleteFiles(picURL);
						boolean b2 =DeleteSamplePointFromDB(PHID);
						if(b1&&b2){
							AlertBox("������ɾ���ɹ�");		
							SamplePointWidget.DelGraphicsToMapView(GraID);
						}else{
							AlertBox("������ɾ��ʧ��");
						}
					}		
				});
				builder.show();
			}});
		
	}
	
	/**
	 * ɾ��������
	 * @param pHID
	 * @return
	 */
	private boolean DeleteSamplePointFromDB(String pHID) {
		SQLiteDatabase mDb = SQLiteDatabase.openDatabase(pathdb, null,0);
		  String Sqlstr = "DELETE FROM PHOTO"+" WHERE PHID = '"+pHID+"'";
		  try {
			mDb.execSQL(Sqlstr);
			return true;
		} catch (Exception e) {
		}
		 return false;
	}
	
	/**
	 * �������ݿ�ֵ
	 * @param phid2
	 * @param dist
	 * @param cc
	 * @param remark
	 * @return
	 */
	protected boolean UpdateSamplePointDB(String phid2, String dist, String cc,String remark) {
		  SQLiteDatabase mDb = SQLiteDatabase.openDatabase(pathdb, null,0);
		  String Sqlstr = "UPDATE PHOTO SET DIST = '"+dist+"',CC='"+cc+"',REMARK ='"+remark+"' WHERE PHID = '"+phid2+"'";
		  try {
			mDb.execSQL(Sqlstr);
			return true;
		} catch (Exception e) {
		}
		 return false;
	}

	private  void exitAttribute(){
		this.finish();
	}
	
	private void AlertBox(String str){
		Toast.makeText(this,str, Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sample_point_attribute, menu);
		return false;
	}

}

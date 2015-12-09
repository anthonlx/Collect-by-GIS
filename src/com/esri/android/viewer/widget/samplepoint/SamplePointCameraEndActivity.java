package com.esri.android.viewer.widget.samplepoint;

import java.io.File;

import com.esri.android.login.UserLoginActivity;
import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerActivity;
import com.esri.android.viewer.ViewerApp;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SamplePointCameraEndActivity extends Activity {
	
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
	//private SamplePointAttributeParcelable SamplePointClass ;
	public static String picURL ="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sample_point_camera_end);
		//SamplePointClass = (SamplePointAttributeParcelable)getIntent().getParcelableExtra("SamplePointClass");  
		setView();
		//��ȡ��һ��ҳ��Ĵ�ֵ
	    Bundle bundle = getIntent().getExtras();
	    //�����������
	    this.picURL =  bundle.getString("picURL");
		String phid = bundle.getString("PHID");
		this.txtPHID.setText(phid);
		this.txtPHID.setEnabled(false);
		//this.txtPHID.setTextColor(Color.WHITE);
		String file = bundle.getString("FILE");
		this.txtFILE.setText(file);
		this.txtFILE.setEnabled(false);
		//this.txtFILE.setTextColor(Color.WHITE);
		String time = bundle.getString("PHTM");
		this.txtPHTM.setText(time);
		this.txtPHTM.setEnabled(false);
		//this.txtPHTM.setTextColor(Color.WHITE);
		//���ǲ���
		String lon = bundle.getString("LONG");
		this.txtLONG.setText(lon);
		this.txtLONG.setEnabled(false);
		//this.txtLONG.setTextColor(Color.WHITE);
		String lat = bundle.getString("LAT");
		this.txtLAT.setText(lat);
		this.txtLAT.setEnabled(false);
		//this.txtLAT.setTextColor(Color.WHITE);
		String alt = bundle.getString("ALT");
		this.txtALT.setText(alt);
		this.txtALT.setEnabled(false);
		//this.txtALT.setTextColor(Color.WHITE);
		String mmod = bundle.getString("MMODE");
		this.txtMMODE.setText(mmod);
		this.txtMMODE.setEnabled(false);
		//this.txtMMODE.setTextColor(Color.WHITE);
		String sat = bundle.getString("SAT");
		this.txtSAT.setText(sat);
		this.txtSAT.setEnabled(false);
		//this.txtSAT.setTextColor(Color.WHITE);
		String azim = bundle.getString("AZIM");
		this.txtAZIM.setText(azim);
		this.txtAZIM.setEnabled(false);
		//this.txtAZIM.setTextColor(Color.WHITE);
		String azimr = bundle.getString("AZIMR");
		this.txtAZIMR.setText(azimr);
		this.txtAZIMR.setEnabled(false);
		
		String dop = bundle.getString("DOP");
		this.txtDOP.setText(dop);
		this.txtDOP.setEnabled(false);
		//this.txtDOP.setTextColor(Color.WHITE);
		//�������
		String tilt = bundle.getString("TILT");
		this.txtTILT.setText(tilt);
		this.txtTILT.setEnabled(false);
		//this.txtTILT.setTextColor(Color.WHITE);
		String roll = bundle.getString("ROLL");
		this.txtROLL.setText(roll);
		this.txtROLL.setEnabled(false);
		//this.txtROLL.setTextColor(Color.WHITE);
		
		//������
		String creater = String.valueOf(ViewerActivity.userid);
		this.txtCREATOR.setText(creater);
		this.txtCREATOR.setEnabled(false);
		//this.txtCREATOR.setTextColor(Color.WHITE);
		
		this.txtFOCAL.setText("35.0");
		this.txtFOCAL.setEnabled(false);
		//this.txtFOCAL.setTextColor(Color.WHITE);

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
				// TODO Auto-generated method stub
				SamplePointAttribute samAtt = new SamplePointAttribute();
				samAtt.setALT(txtALT.getText().toString());
				samAtt.setAZIM(txtAZIM.getText().toString());
				samAtt.setAZIMP(txtAZIMP.getText().toString());
				samAtt.setAZIMR(txtAZIMR.getText().toString());
				samAtt.setCC(txtCC.getText().toString());
				samAtt.setCREATOR(txtCREATOR.getText().toString());
				samAtt.setDIST(txtDIST.getText().toString());
				samAtt.setDOP(txtDOP.getText().toString());
				samAtt.setFILE(txtFILE.getText().toString());
				samAtt.setFOCAL(txtFOCAL.getText().toString());
				samAtt.setLAT(txtLAT.getText().toString());
				samAtt.setLONG(txtLONG.getText().toString());
				samAtt.setMMODE(txtMMODE.getText().toString());
				samAtt.setPHID(txtPHID.getText().toString());
				samAtt.setPHTM(txtPHTM.getText().toString());
				samAtt.setREMARK(txtREMARK.getText().toString());
				samAtt.setROLL(txtROLL.getText().toString());
				samAtt.setSAT(txtSAT.getText().toString());
				samAtt.setTILT(txtTILT.getText().toString());
				SaveToDB(samAtt);
				exitCameraAttribute(true);
			}});
		
		this.btnCancle.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				exitCameraAttribute(false);
			}});
		
	}
	
	/**
	 * ������������ϵͳ���ݿ�
	 * @param samAtt
	 */
	protected void SaveToDB(SamplePointAttribute samAtt) {
		// TODO Auto-generated method stub
		ViewerApp appState = ((ViewerApp)this.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		String pathdb= path.samplepointPath+"/"
				+com.esri.android.viewer.tools.SystemVariables.PointPackageDirectory +"/"
				+com.esri.android.viewer.tools.SystemVariables.PointDB;
		 //������ݿⴴ���ɹ��򴴽����ݱ�
	   SQLiteDatabase mDb = SQLiteDatabase.openOrCreateDatabase(pathdb, null);
 	   try {
			String sqlStr = "INSERT INTO PHOTO (PHID,FILE,PHTM,LONG,LAT,DOP,ALT,MMODE,SAT,AZIM,AZIMR,AZIMP,DIST,TILT,ROLL,CC,REMARK,CREATOR,FOCAL) VALUES (" 
					+"'"+samAtt.getPHID()+"',"
					+"'"+samAtt.getFILE()+"',"		
					+"'"+samAtt.getPHTM()+"',"		
					+"'"+samAtt.getLONG()+"',"		
					+"'"+samAtt.getLAT()+"',"		
					+"'"+samAtt.getDOP()+"',"		
					+"'"+samAtt.getALT()+"',"		
					+"'"+samAtt.getMMODE()+"',"		
					+"'"+samAtt.getSAT()+"',"		
					+"'"+samAtt.getAZIM()+"',"		
					+"'"+samAtt.getAZIMR()+"',"		
					+"'"+samAtt.getAZIMP()+"',"		
					+"'"+samAtt.getDIST()+"',"		
					+"'"+samAtt.getTILT()+"',"		
					+"'"+samAtt.getROLL()+"',"		
					+"'"+samAtt.getCC()+"',"		
					+"'"+samAtt.getREMARK()+"',"		
					+"'"+samAtt.getCREATOR()+"',"		
					+"'"+samAtt.getFOCAL()+"'"
					+")";
			mDb.execSQL(sqlStr );//������ǩ��
			 Toast.makeText(SamplePointCameraEndActivity.this,"�����㱣��ɹ�", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			//Log.d("������д���쳣", e.toString());				
			Toast.makeText(SamplePointCameraEndActivity.this,"�����㱣��ʧ��"+e.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	private  void exitCameraAttribute(boolean issave){
		try {
			if(!issave){
				com.esri.android.viewer.tools.fileTools.deleteFiles(picURL);
			}
		} catch (Exception e) {
		}
		this.finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sample_point_camera_end, menu);
		return false;
	}

}

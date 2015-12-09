package com.esri.android.login;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.esri.android.login.UserLoginActivity.RefreshHandler;
import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerActivity;
import com.esri.android.viewer.ViewerApp;
import com.esri.android.viewer.R.layout;
import com.esri.android.viewer.R.menu;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserRegisterActivity extends Activity {

	private EditText username=null;
	private EditText userpwd=null;
	private EditText userpwd2=null;
	private EditText dept=null;
	private EditText tel=null;
	private Button btnOK = null;
	private ProgressDialog mProgressDlg=null;//�ȴ���
	
	private static String NameSpace="http://tempuri.org/";//�����ռ�
	private static String host="";//Ĭ�Ϸ�������ַ���������Զ����������ļ�����
	private static String webService="";//webServiceĿ¼���������Զ����������ļ�����
	private static String MethodName="RegistryUser";//Ҫ���õ�webService����
	private static String soapAction=NameSpace+MethodName;
	
	private RefreshHandler refreshHandler =new RefreshHandler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_register);
		this.username = (EditText)this.findViewById(R.id.activity_user_register_editText_username);
		this.userpwd =(EditText)this.findViewById(R.id.activity_user_register_editText_userpwd);
		this.userpwd2 = (EditText)this.findViewById(R.id.activity_user_register_editText_userpwd2);
		this.dept =(EditText)this.findViewById(R.id.activity_user_register_editText_dept);
		this.tel = (EditText)this.findViewById(R.id.activity_user_register_editText_tel);
		this.btnOK = (Button)this.findViewById(R.id.activity_user_register_btnOK);
		this.btnOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(username.getText().toString().equals("")){
					Toast.makeText(UserRegisterActivity.this,"�û���Ϊ�գ�", Toast.LENGTH_SHORT).show();
					return;
				}else if(userpwd.getText().toString().equals("")){
					Toast.makeText(UserRegisterActivity.this,"����Ϊ�գ�", Toast.LENGTH_SHORT).show();
					return;
				}else if(userpwd2.getText().toString().equals("")){
					Toast.makeText(UserRegisterActivity.this,"����Ϊ�գ�", Toast.LENGTH_SHORT).show();
					return;
				}else if(dept.getText().toString().equals("")){
					Toast.makeText(UserRegisterActivity.this,"����Ϊ�գ�", Toast.LENGTH_SHORT).show();
					return;
				}else if(tel.getText().toString().equals("")){
					Toast.makeText(UserRegisterActivity.this,"�绰Ϊ�գ�", Toast.LENGTH_SHORT).show();
					return;
				}else if(!userpwd.getText().toString().equals(userpwd2.getText().toString())){
					Toast.makeText(UserRegisterActivity.this,"�����������벻һ�£�", Toast.LENGTH_SHORT).show();
				}else{
					mProgressDlg = new ProgressDialog(UserRegisterActivity.this);
					mProgressDlg = ProgressDialog.show(UserRegisterActivity.this, "", "ע����...");
					refreshHandler.sleep(500);	//500�����ִ���߳�
				}
				
			}});
	}
	
	/**
	 * ע���û�
	 * @param name �û���
	 * @param pwd ����
	 * @param dept ����
	 * @param tel �绰
	 */
 	protected void registerUser(String name, String pwd, String dept,String tel) {
 		intiWebserviceStr();//��ʼ�������ַ	
		 try{//ע��
	    		SoapObject request=new SoapObject(NameSpace,MethodName);//NameSpace
	    		//webService�����еĲ���������������webservice��������û�С�
	    		//��ע�⣬�������ƺͲ������ͣ��ͻ��˺ͷ����һ��Ҫһ�£����򽫿��ܻ�ȡ��������Ҫ��ֵ
	    		request.addProperty("name", name);
	    		request.addProperty("password", pwd);
	    		request.addProperty("dept", dept);
	    		request.addProperty("tel", tel);
	    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    		envelope.dotNet=true;//����.NET��webservice
	    		envelope.setOutputSoapObject(request);
	    		HttpTransportSE ht=new HttpTransportSE(host+webService);
	    		ht.call(soapAction, envelope);//����call����������webservice
	    		if(envelope.getResponse()!=null){
	    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
	    			if("true".equals(response.toString())) {
	    				 Toast.makeText(UserRegisterActivity.this,"ע��ɹ���", Toast.LENGTH_SHORT).show();
	    				 this.finish();
	    			}else{
	    				 Toast.makeText(UserRegisterActivity.this,"ע��ʧ�ܣ����Ժ����ԣ�", Toast.LENGTH_SHORT).show();
	    			}
	    		}
	    	}catch(Exception e){
	    		Toast.makeText(UserRegisterActivity.this,"���������쳣��"+e.getMessage(), Toast.LENGTH_SHORT).show();	    	
	    	}
	}

	/**
	 * ��ʼ��WebService�����ַ������������ļ���ȡ
	 */
	 private void intiWebserviceStr() {
		// TODO ��ʼ��host��webservices
		 try {  
			 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();  
			 DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();  
			 Document doc = docBuilder.parse(getFileInputStream());
			 //root element  
			 Element root = doc.getDocumentElement();  
			 //get a NodeList by tagname  
			 NodeList nodeList = root.getElementsByTagName("servicehost");
			 host = nodeList.item(0).getTextContent();//�������ļ���ȡ��������ַ
			 NodeList nodeList2 = root.getElementsByTagName("userservice");
			 webService = nodeList2.item(0).getTextContent();//�������ļ���ȡ�����ַ
			 } catch (Exception e) { 
				 Toast.makeText(UserRegisterActivity.this,"webservice�����ַ��ʼ������"+e.toString(), Toast.LENGTH_SHORT).show();
		 }  
	}
 	
	/**
 	 * ��ȡxml�ļ�������
 	 * @return
 	 * @throws IOException 
 	 */
	private InputStream getFileInputStream() throws IOException
	{
		InputStream is=null;
		//is = this.getAssets().open("sys.xml");
		ViewerApp appState = ((ViewerApp)this.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		String file= path.systemConfigFilePath+"/" +"sys.xml";
		is = new FileInputStream(file);
		return is;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_user_register, menu);
		return false;//����ʾ�˵�
	}

	class RefreshHandler extends Handler{	 
		  @Override
		   public void handleMessage(Message msg) {		 
			  //�û�ע��
			  registerUser(username.getText().toString(),userpwd.getText().toString(),dept.getText().toString(),tel.getText().toString());
			  mProgressDlg.dismiss();//���������
		   }
		   
		   public void sleep(long delayMillis){
			    this.removeMessages(0);
			    sendMessageDelayed(obtainMessage(0), delayMillis);
		   }
	}
	
}

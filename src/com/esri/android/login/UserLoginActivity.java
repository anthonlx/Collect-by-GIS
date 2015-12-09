package com.esri.android.login;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;  
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;  
import org.w3c.dom.Element;  
import org.w3c.dom.NodeList;  
import org.xml.sax.SAXException; 

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.esri.android.tasks.TaskManagerActivity;
import com.esri.android.tasks.TaskPackageActivity;
import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerActivity;
import com.esri.android.viewer.ViewerApp;
import com.esri.android.viewer.tools.fileUtil.file;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UserLoginActivity extends Activity {

	private Button btnRegister=null;
	private Button btnLogin =null;
	private EditText userEditText = null;
	private EditText pwdEditText = null;
	private ProgressDialog mProgressDlg=null;

	private static String NameSpace="http://tempuri.org/";//�����ռ�
	private static String webService="";
	private static String MethodName="Login";//Ҫ���õ�webService����
	private static String soapAction=NameSpace+MethodName;
		
	private RefreshHandler refreshHandler =new RefreshHandler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//������ص��߳�ģʽ
    	StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
            .detectDiskReads()
            .detectDiskWrites()
            .detectNetwork()// or .detectAll() for all detectable problems      
            .penaltyLog()
            .build()); 
    	//������ص����������---------�ᵼ��Ҫ�زɼ�ģ�����
        /*StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()     
	            .detectLeakedSqlLiteObjects()     
	            //.detectLeakedClosableObjects()     
	            .penaltyLog()     
	            .penaltyDeath()     
	            .build());  */  
    	//requestWindowFeature(Window.FEATURE_NO_TITLE);// ��������
		setContentView(R.layout.activity_user_login);
	
		com.esri.android.viewer.tools.sysTools.intiWorkspaceDir(this);//ϵͳ����Ŀ¼��ʼ��
		
		copyXMLtoDataTemp();//����Ӧ�ó��������ļ���Ӧ�ó�����ʱĿ¼
		
		userEditText=(EditText)findViewById(R.id.login_activity_userEditText);
		pwdEditText=(EditText)findViewById(R.id.login_activity_pwdEditText);	       
		
		//��Button�¼�
		btnRegister =(Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				// TODO �Զ����ɵķ������
    	        Intent intent = new Intent(UserLoginActivity.this, UserRegisterActivity.class);    	      
    	        UserLoginActivity.this.startActivity(intent);
			}});	
		
		btnLogin =(Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new OnClickListener(){
			public void onClick(View arg0) {
				// TODO �û���½	
//				if(userEditText.getText().equals("")||pwdEditText.getText().toString().equals("")){
//					 Toast.makeText(UserLoginActivity.this,"�û���������Ϊ�գ�", Toast.LENGTH_SHORT).show();
//				}else{
//					mProgressDlg = new ProgressDialog(UserLoginActivity.this);
//					mProgressDlg = ProgressDialog.show(UserLoginActivity.this, "", "��¼��...");
//					refreshHandler.sleep(500);	
//				}	
				
				//ֱ��������¼
				Intent intent = new Intent(UserLoginActivity.this, TaskPackageActivity.class);
		    	UserLoginActivity.this.startActivity(intent);
			}});	
	}
	
 	/**
 	 * ��ȡxml�ļ�������
 	 * @return
 	 * @throws IOException 
 	 */
	private InputStream getFileInputStream() throws IOException
	{
		ViewerApp appState = ((ViewerApp)this.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		String sysFilePath= path.systemConfigFilePath+"/" +"sys.xml";
		InputStream is=null;
		//is = this.getAssets().open("sys.xml");
		is = new FileInputStream(sysFilePath);
		return is;
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
			 Element root = doc.getDocumentElement();  
			 NodeList nodeList2 = root.getElementsByTagName("userservice");
			 webService = nodeList2.item(0).getTextContent();//�������ļ���ȡ�����ַ
			 } catch (Exception e) { 
				 Toast.makeText(UserLoginActivity.this,"webservice�����ַ��ʼ������"+e.toString(), Toast.LENGTH_SHORT).show();
			 } 
	}

	/**
	 * ����XML�ļ���ϵͳ��ʱĿ¼
	 * @throws IOException 
	 * @throws ParserConfigurationException 
	 * @throws SAXException 
	 */
	private void copyXMLtoDataTemp() {
		// TODO �ж������ļ��Ƿ����
		ViewerApp appState = ((ViewerApp)this.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		String sysFilePath= path.systemConfigFilePath+"/" +"sys.xml";
		File file = new File(sysFilePath);
		if(!file.exists())
		{
			//�ļ��������򴴽����ļ�
			InputStream is;
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();  
			DocumentBuilder docBuilder = null;
			Document doc = null;
			try {
				is = this.getAssets().open("sys.xml");
				docBuilder = docBuilderFactory.newDocumentBuilder();
				doc = docBuilder.parse(is);
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			String doc_str = toStringFromDoc(doc);
			com.esri.android.viewer.tools.fileTools.saveTxt(sysFilePath, doc_str);	
		}
	}
	
 	/**
     * ��dom�ļ�ת��Ϊxml�ַ���  
     */  
	 public static String toStringFromDoc(Document document) {  
	        String result = null;  
	        if (document != null) {  
	            StringWriter strWtr = new StringWriter();  
	            StreamResult strResult = new StreamResult(strWtr);  
	            TransformerFactory tfac = TransformerFactory.newInstance();  
	            try {  
	                javax.xml.transform.Transformer t = tfac.newTransformer();  
	                t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");  
	                t.setOutputProperty(OutputKeys.INDENT, "yes");  
	                t.setOutputProperty(OutputKeys.METHOD, "xml"); // xml, html,  
	                t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");  
	                t.transform(new DOMSource(document.getDocumentElement()),  strResult);  
	            } catch (Exception e) {  
	                System.err.println("XML.toString(Document): " + e);  
	            }  
	            result = strResult.getWriter().toString();  
	            try {  
	                strWtr.close();  
	            } catch (IOException e) {  
	                e.printStackTrace();  
	            }  
	        }  
	        return result;   
	    }  

	 /**
	  * �û���½���
	  * @param user �û���
	  * @param pwd ����
	  * @return ����    �ַ���   �û�ID ������֤   ������֤ 
	  */
	protected UserInfo checkLogin(String user , String pwd) {
		 //���ص�½��֤
		 UserInfo userinfo_loc = getLocalUserInfo(user,pwd);
		 if(userinfo_loc!=null){
			 userinfo_loc.tpye = 0;//������֤
		 }		 
		 //������¼��֤
		 UserInfo userinfo_online =null;
		 try{
	    		SoapObject request=new SoapObject(NameSpace,MethodName);//NameSpace
	    		//webService�����еĲ���������������webservice��������û�С�
	    		//��ע�⣬�������ƺͲ������ͣ��ͻ��˺ͷ����һ��Ҫһ�£����򽫿��ܻ�ȡ��������Ҫ��ֵ
	    		request.addProperty("userName", user);
	    		request.addProperty("password", pwd);
	    		//��ȡ�豸ID
	    		String drverid =  com.esri.android.viewer.tools.sysTools.getLocalMacAddress(this);
	    		request.addProperty("deviceId", drverid);
	    		SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER11);
	    		envelope.dotNet=true;//����.NET��webservice
	    		envelope.setOutputSoapObject(request);
	    		HttpTransportSE ht=new HttpTransportSE(webService);
	    		ht.call(soapAction, envelope);//����call����������webservice
	    		if(envelope.getResponse()!=null){
	    			SoapPrimitive response=(SoapPrimitive)envelope.getResponse();
	    			String userstrid = response.toString();//�����û�ID
	    			if(userstrid.equals("-1")) {
	    				 userinfo_online =null;
					}else{	
						userinfo_online =new UserInfo();
						userinfo_online.id = userstrid;
						userinfo_online.name = user;
						userinfo_online.pwd = pwd;
						userinfo_online.tpye = 1;//������֤
					}
	    		}
	    	}catch(Exception e){
	    		Toast.makeText(UserLoginActivity.this,"�����쳣��\n"+e.getMessage(), Toast.LENGTH_SHORT).show();	    	
	    	}
		    //��������״̬
		 	Boolean isOnline = com.esri.android.viewer.tools.sysTools.isConnected(this);
		 	if (isOnline){
		 		//�����������ж�������֤�뱾����֤����� �������������ڱ��� ��������������
		 		if(userinfo_online!=null){//�������طǿ�
		 			SaveUserToDB(userinfo_online);//�����û������ݿ�
					return userinfo_online;
		 		}else{
		 			return null;
		 		}
		 	}else{
		 		//��������֤ȡ����ֵ�򷵻ر�����֤����
		 		return userinfo_loc;
		 	}				
	}
	
	/**
	 * �����û��������û����ݿ�
	 * @param usr
	 */
	private void SaveUserToDB(UserInfo usr) {
		// TODO Auto-generated method stub
		ViewerApp appState = ((ViewerApp)this.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		String bizpath = path.systemConfigFilePath;
		String dbpath = bizpath+"/"+com.esri.android.viewer.tools.SystemVariables.ConfigSqliteDB;
		SQLiteDatabase mDb = SQLiteDatabase.openDatabase(dbpath, null, 0);
		String sqlStr  = "INSERT INTO Local_USERS(F_USERID,F_USERNAME,F_PASSWORD) VALUES('"+
		usr.id+"','"+usr.name +"','" +usr.pwd +"')";
		//ִ��ǰ��ɾ��ͬID�û�
		String sqlStrDel = "delete from Local_USERS WHERE F_USERID ="+usr.id+"";
		mDb.execSQL(sqlStrDel);
		mDb.execSQL(sqlStr);
		mDb.close();
	}


	private UserInfo getLocalUserInfo(String user, String pwd) {
		// TODO Auto-generated method stub
		ViewerApp appState = ((ViewerApp)this.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		String bizpath = path.systemConfigFilePath;
		String dbpath = bizpath+"/"+com.esri.android.viewer.tools.SystemVariables.ConfigSqliteDB;
		SQLiteDatabase mDb = SQLiteDatabase.openDatabase(dbpath, null, 0);
		String sqlStr  = "SELECT * FROM Local_USERS WHERE F_USERNAME ='" +user+"' and F_PASSWORD = '"+pwd+"'";
		Cursor cursor=mDb.rawQuery(sqlStr,null);
		if(cursor.getCount()!=0){
			cursor.moveToFirst();
			String userid = cursor.getString(cursor.getColumnIndex("F_USERID"));
			String username = cursor.getString(cursor.getColumnIndex("F_USERNAME"));
			String userpwd = cursor.getString(cursor.getColumnIndex("F_PASSWORD"));
			String userdept = cursor.getString(cursor.getColumnIndex("F_DEPARTMENT"));
			String usertel = cursor.getString(cursor.getColumnIndex("F_TELEPHONE"));
			UserInfo userinfo = new UserInfo();
			userinfo.id = userid;
			userinfo.name = username;
			userinfo.pwd = userpwd;
			userinfo.dept = userdept;
			userinfo.tel = usertel;
			return userinfo;
		}
		return null;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {
	        case R.id.menu_sys_settings:
	            Intent intent = new Intent(UserLoginActivity.this, SysConfigActivity.class);  
	            Bundle bundle=new Bundle();  
	            UserLoginActivity.this.startActivity(intent);
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }
	    }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_userlogin, menu);
		return true;//��ʾ�˵�
	}

	
	public class RefreshHandler extends Handler{	 
		  @Override
		   public void handleMessage(Message msg) {
			    try{ 
			    	intiWebserviceStr();//��ʼ�������ַ
			    	UserInfo user = checkLogin(userEditText.getText().toString(),pwdEditText.getText().toString());
			    	if(user!=null){
			    		if(user.tpye==0){//������֤
			    			Toast.makeText(UserLoginActivity.this,"��½�ɹ���������֤", Toast.LENGTH_SHORT).show();	
			    		}else{
			    			Toast.makeText(UserLoginActivity.this,"��½�ɹ���������֤", Toast.LENGTH_SHORT).show();
			    		}
			    		boolean is = CheckLocalIsHaveTask();
		    			if(is){
		    				LoginOK(1);//���������
		    			}else{
		    				LoginOK(0);//�����������
		    			}
		    			ViewerActivity.userid = Integer.parseInt(user.id);//�����û�ID
			    	}else{
			    		Toast.makeText(UserLoginActivity.this,"��½ʧ�ܣ�", Toast.LENGTH_SHORT).show();
			    	}
			   }catch(Exception e){
				    e.printStackTrace();
				    Toast.makeText(UserLoginActivity.this,"��½�쳣��"+e.toString(), Toast.LENGTH_SHORT).show();
			    }finally{
			    	mProgressDlg.dismiss();//���������
			    }
		   }
		   
		   public void sleep(long delayMillis){
			    this.removeMessages(0);
			    sendMessageDelayed(obtainMessage(0), delayMillis);
		   }
		 }


	/**
	 * ��½�ɹ�
	 * @param type ��½����
	 */
	private void LoginOK(int type) {
		Intent intent = new Intent(UserLoginActivity.this, TaskPackageActivity.class);
    	UserLoginActivity.this.startActivity(intent);
//		switch(type){
//		case 0://�����������
//			Intent i = new Intent(UserLoginActivity.this, TaskManagerActivity.class);
//			UserLoginActivity.this.startActivity(i);
//			break;
//		case 1://���������
//			Intent intent = new Intent(UserLoginActivity.this, TaskPackageActivity.class);
//			UserLoginActivity.this.startActivity(intent);
//			break;
//		}
		finish();//������ǰ����
	}


	/**
	 * �жϱ���������Ƿ����
	 * @return
	 */
	public boolean CheckLocalIsHaveTask() {
		// TODO Auto-generated method stub
		ViewerApp appState = ((ViewerApp)this.getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		String bizpath = path.taskPackageFilePath;
		com.esri.android.viewer.tools.fileUtil fileutil = new com.esri.android.viewer.tools.fileUtil();
		List<com.esri.android.viewer.tools.fileUtil.file> filelist_folder = fileutil.getFileDir(bizpath, "folder");
		List<com.esri.android.viewer.tools.fileUtil.file> filelist_sqlite= fileutil.getFileDir(bizpath, ".sqlite");
		if(filelist_sqlite.isEmpty()&&filelist_folder.isEmpty()) {
			return false;
		}else{
			return true;
		}
	}
			
}

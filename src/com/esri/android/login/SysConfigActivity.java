package com.esri.android.login;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

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

import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerApp;
import com.esri.android.viewer.R.layout;
import com.esri.android.viewer.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SysConfigActivity extends Activity {

	private EditText editText_userService=null;
	private EditText editText_trackService=null;
	private EditText editText_taskService=null;
	private EditText editText_taskDirPath=null;
	private Button btnOK = null;
	private static String sysFilePath="";//sys.xml�ļ�·��
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sys_config);
		
		ViewerApp appState = ((ViewerApp) this.getApplicationContext());
		com.esri.android.viewer.tools.fileTools.filePath path = appState.getFilePaths();
		this.sysFilePath= path.systemConfigFilePath + "/" + "sys.xml";
		
		this.editText_trackService =(EditText)this.findViewById(R.id.activity_sys_config_editText_trackService);
		this.editText_userService = (EditText)this.findViewById(R.id.activity_sys_config_editText_userService);
		this.editText_taskService = (EditText)this.findViewById(R.id.activity_sys_config_editText_taskService);
		this.editText_taskDirPath= (EditText)this.findViewById(R.id.activity_sys_config_editText_taskDirPath);
		this.btnOK = (Button)this.findViewById(R.id.activity_sys_config_btnOK);
		this.btnOK.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String user = editText_userService.getText().toString();
				String track = editText_trackService.getText().toString();
				String task = editText_taskService.getText().toString();
				String taskpath = editText_taskDirPath.getText().toString();
				configSave(user,track,task,taskpath);
			}});
		intiConfig();//��ʼ����������
	}

	/**
	 * �������������Ϣ
	 * @param host ������ַ
	 * @param user �û������ַ
	 * @param track λ�÷����ַ
	 * @param taskpath 
	 */
	protected void configSave(String user, String track,String task, String taskpath) {
		try {
			// TODO Auto-generated method stub
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(getFileInputStream());
			Element root = doc.getDocumentElement();
			NodeList nodeList2 = root.getElementsByTagName("userservice");
			nodeList2.item(0).setTextContent(user);//�����û������ַ
			NodeList nodeList3 = root.getElementsByTagName("trackservice");
			nodeList3.item(0).setTextContent(track);//�������ļ���ȡλ�÷����ַ
			NodeList nodeList4 = root.getElementsByTagName("taskservice");
			nodeList4.item(0).setTextContent(task);//�������ļ���ȡ��������ַ
			NodeList nodeList5 = root.getElementsByTagName("taskfiledir");
			nodeList5.item(0).setTextContent(taskpath);//�������ļ���ȡ�����ļ���������ַ
			// ��������ַ��Ϣ
			String doc_str = toStringFromDoc(doc);
			com.esri.android.viewer.tools.fileTools.saveTxt(this.sysFilePath, doc_str);
			
			//������ϵͳȫ�ֱ���
			ViewerApp appState = ((ViewerApp) this.getApplicationContext());
			appState.setUserService(user);
			appState.setTrackService(track);
			appState.setTaskService(task);
			
			Toast.makeText(SysConfigActivity.this,"ϵͳ������Ϣ���³ɹ���", Toast.LENGTH_SHORT).show();
			this.finish();
		} catch (Exception e) {
			// TODO: handle exception
			 Toast.makeText(SysConfigActivity.this,"ϵͳ������Ϣ����ʧ�ܣ�\n"+e.toString(), Toast.LENGTH_SHORT).show();
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
	 * ��ʼ��������Ϣ
	 */
 	private void intiConfig() {
		// TODO Auto-generated method stub
 		 try {  
				 DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();  
				 DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();  
				 Document doc = docBuilder.parse(getFileInputStream());
				 //root element  
				 Element root = doc.getDocumentElement();  
				 //get a NodeList by tagname  
				 NodeList nodeList2 = root.getElementsByTagName("userservice");
				 String  userService = nodeList2.item(0).getTextContent();//�������ļ���ȡ�û������ַ
				 this.editText_userService.setText(userService);
				 NodeList nodeList3 = root.getElementsByTagName("trackservice");
				 String  trackService = nodeList3.item(0).getTextContent();//�������ļ���ȡλ�÷����ַ
				 this.editText_trackService.setText(trackService);
				 NodeList nodeList4 = root.getElementsByTagName("taskservice");
				 String taskService = nodeList4.item(0).getTextContent();//�������ļ���ȡ��������ַ
				 this.editText_taskService.setText(taskService);
				 NodeList nodeList5 = root.getElementsByTagName("taskfiledir");
				 String taskpath = nodeList5.item(0).getTextContent();//�������ļ���ȡ�������������ַ
				 this.editText_taskDirPath.setText(taskpath);
				 
			 } catch (IOException e) { 
			 } catch (SAXException e) {  
			 } catch (ParserConfigurationException e) { 
			 } finally {	
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
		is = new FileInputStream(this.sysFilePath);
		return is;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_sys_config, menu);
		return false;
	}

}

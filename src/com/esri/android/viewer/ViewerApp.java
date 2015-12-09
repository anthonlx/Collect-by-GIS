package com.esri.android.viewer;

import com.esri.android.login.UserInfo;
import com.esri.android.viewer.tools.fileTools.filePath;

import android.app.Application;

public class ViewerApp extends Application  {

	private String servicehost; //��������ַ          
	public String getServiceHost() {            
		return servicehost;            
		}            
	public void setServiceHost(String s) {            
		servicehost = s;            
		}
	
	private String userservice; //�û������ַ   
	public String getUserService() {            
		return userservice;            
		}            
	public void setUserService(String s) {            
		userservice = s;            
		}
	
	private String trackservice; //λ�÷����ַ   
	public String getTrackService() {            
		return trackservice;            
		}            
	public void setTrackService(String s) {            
		trackservice = s;            
		}
	
	private String taskservice; //��������ַ   
	public String getTaskService() {            
		return taskservice;            
		}            
	public void setTaskService(String s) {            
		taskservice = s;            
		}
	
	private filePath filepath ;//ϵͳ�ļ���·��
	public filePath getFilePaths() {            
		return filepath;            
		}	
	public void SetFilePaths(filePath f) { 
		filepath = f;            
		}
	
	private UserInfo userinfo ;//ϵͳ�û�
	public UserInfo getUserInfo() {            
		return userinfo;            
		}	
	public void SetUserInfo(UserInfo u) { 
		userinfo = u;            
		}
	
}

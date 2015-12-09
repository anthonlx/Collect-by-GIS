package com.esri.android.viewer.widget.samplepoint;

import android.os.Parcel;
import android.os.Parcelable;

public class SamplePointAttribute{
	
	private String picUrl = null;//����ͼ
	private String PHID = null;//��Ƭ��ʶ��
	private String FILE = null;//��Ƭ�ļ���
	private String PHTM = null;//����ʱ��
	private String LONG = null;//����㾭��
	private String LAT = null;//�����γ��
	private String DOP = null;//λ�ö�λˮƽ����ˮƽ
	private String ALT = null;//�����߳�
	private String MMODE = null;//��λ����
	private String SAT = null;//��λʱ�۲⵽����������
	private String AZIM = null;//��Ƭ��λ��
	private String AZIMR = null;//��Ƭ��λ�ǵĲο�����
	private String AZIMP = null;//��λ��׼ȷ�̶�
	private String DIST = null;//�������
	private String TILT = null;//���������
	private String ROLL = null;//��������
	private String CC = null;//��Ƭ���������ĵ��������Ϣ���ʹ���
	private String REMARK = null;//��������������
	private String CREATOR = null;//������
	private String FOCAL = null;//35m��Ч����
	
	public SamplePointAttribute(){
		
	}
	
	/**
	 * ��ȡͼƬURL
	 * @return
	 */
	public String getPicUrl() {  
		 return this.picUrl;  
	} 
	/**
	 * ����ͼƬURL
	 * @param url
	 */
	public void setPicUrl(String url) {  
		 this.picUrl = url;  
	}  
	
	/**
	 * ��ȡͼƬ��ʶ��
	 * @return
	 */
	public String getPHID() {  
		 return this.PHID;  
	}  
	/**
	 * ����ͼƬ��ʶ��
	 * @param s
	 */
	public void setPHID(String s){  
		 this.PHID = s;  
	}  
	
	/**
	 * ��ȡͼƬ�ļ���
	 * @return
	 */
	public String getFILE() {  
		 return this.FILE;  
	}  
	/**
	 * ����ͼƬ�ļ���
	 * @param url
	 */
	public void setFILE(String s){  
		 this.FILE = s;  
	} 

	/**
	 * ��ȡͼƬ����ʱ��
	 * @return
	 */
	public String getPHTM() {  
		 return this.PHTM;  
	}  
	/**
	 * ����ͼƬ����ʱ��
	 * @param s
	 */
	public void setPHTM(String s){  
		 this.PHTM = s;  
	} 
	
	/**
	 * ��ȡ����㾭��
	 * @return
	 */
	public String getLONG() {  
		 return this.LONG;  
	}  
	/**
	 * ��������㾭��
	 * @param s
	 */
	public void setLONG(String s){  
		 this.LONG = s;  
	} 
	
	/**
	 * ��ȡ�����γ��
	 * @return
	 */
	public String getLAT() {  
		 return this.LAT;  
	}  	
	/**
	 * ���������γ��
	 * @param s
	 */
	public void setLAT(String s){  
		 this.LAT = s;  
	} 
	
	/**
	 * ��ȡλ�ö�λˮƽ����ˮƽ
	 * @return
	 */
	public String getDOP() {  
		 return this.DOP;  
	}  
	/**
	 * ����λ�ö�λˮƽ����ˮƽ
	 * @param s
	 */
	public void setDOP(String s){  
		 this.DOP = s;  
	}
	
	/**
	 * ��ȡ�����߳�
	 * @return
	 */
	public String getALT() {  
		 return this.ALT;  
	}  
	/**
	 * ���������߳�
	 * @param s
	 */
	public void setALT(String s){  
		 this.ALT = s; 
	}
	
	/**
	 * ��ȡ��λ����
	 * @return
	 */
	public String getMMODE() {  
		 return this.MMODE;  
	}  
	/**
	 * ���ö�λ����
	 * @param s
	 */
	public void setMMODE(String s){  
		 this.MMODE = s; 
	}
	
	/**
	 * ��ȡ��λʱ�۲⵽����������
	 * @return
	 */
	public String getSAT() {  
		 return this.SAT;  
	}  
	/**
	 * ���ö�λʱ�۲⵽����������
	 * @param s
	 */
	public void setSAT(String s){  
		 this.SAT = s; 
	}
	
	/**
	 * ��ȡ��Ƭ��λ��
	 * @return
	 */
	public String getAZIM() {  
		 return this.AZIM;  
	}  
	/**
	 * ������Ƭ��λ��
	 * @param s
	 */
	public void setAZIM(String s){  
		 this.AZIM = s; 
	}
	
	/**
	 * ��ȡ��Ƭ��λ�ǲο�����
	 * @return
	 */
	public String getAZIMR() {  
		 return this.AZIMR;  
	}  
	/**
	 * ������Ƭ��λ�ǲο�����
	 * @param s
	 */
	public void setAZIMR(String s){  
		 this.AZIMR = s; 
	}
	
	/**
	 * ��ȡ��Ƭ��λ��׼ȷ�̶�
	 * @return
	 */
	public String getAZIMP() {  
		 return this.AZIMP;  
	}  
	/**
	 * ������Ƭ��λ��׼ȷ�̶�
	 * @param s
	 */
	public void setAZIMP(String s){  
		 this.AZIMP = s; 
	}
	
	/**
	 * ��ȡ�������
	 * @return
	 */
	public String getDIST() {  
		 return this.DIST;  
	}  
	/**
	 * �����������
	 * @param s
	 */
	public void setDIST(String s){  
		 this.DIST = s; 
	}
	
	/**
	 * ��ȡ���������
	 * @return
	 */
	public String getTILT() {  
		 return this.TILT;  
	}  
	/**
	 * �������������
	 * @param s
	 */
	public void setTILT(String s){  
		 this.TILT = s; 
	}
	
	/**
	 * ��ȡ���������
	 * @return
	 */
	public String getROLL() {  
		 return this.ROLL;  
	}  
	/**
	 * �������������
	 * @param s
	 */
	public void setROLL(String s){ 
		 this.ROLL = s; 
	}
	
	/**
	 * ��ȡ��Ƭ���������ĵ��������Ϣ���ʹ���
	 * @return
	 */
	public String getCC() {  
		 return this.CC;  
	}  
	/**
	 * ������Ƭ���������ĵ��������Ϣ���ʹ���
	 * @param s
	 */
	public void setCC(String s){ 
		 this.CC = s; 
	}
	
	/**
	 * ��ȡ��������������
	 * @return
	 */
	public String getREMARK() {  
		 return this.REMARK;  
	}  
	/**
	 * ������������������
	 * @param s
	 */
	public void setREMARK(String s){ 
		 this.REMARK = s; 
	}
	
	/**
	 * ��ȡ������
	 * @return
	 */
	public String getCREATOR() {  
		 return this.CREATOR;  
	}  
	/**
	 * ����������
	 * @param s
	 */
	public void setCREATOR(String s){ 
		 this.CREATOR = s; 
	}
	
	/**
	 * ��ȡ35MM��Ч����
	 * @return
	 */
	public String getFOCAL() {  
		 return this.FOCAL;  
	}  
	/**
	 * ����35MM��Ч����
	 * @param s
	 */
	public void setFOCAL(String s){ 
		 this.FOCAL = s; 
	}
	
}

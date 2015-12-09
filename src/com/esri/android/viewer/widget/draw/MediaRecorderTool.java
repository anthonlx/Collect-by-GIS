package com.esri.android.viewer.widget.draw;

import java.io.File;
import java.io.IOException;

import android.media.MediaRecorder;
import android.media.MediaRecorder.AudioEncoder;

public class MediaRecorderTool {
	 private MediaRecorder mediaRecorder = null;
	 private String FileFullPathStr =null;//�ļ�ȫ��
	 public MediaRecorderTool(String fullpath){
		 FileFullPathStr = fullpath;
		 init();
	 }
	 
	 /**
	  * ¼����ʼ��
	  */
	 private void init(){
		 mediaRecorder = new MediaRecorder();  
         // ��1����������Ƶ��Դ��MIC��ʾ��˷磩
		 mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		 //��2����������Ƶ�����ʽ��Ĭ�ϵ������ʽ��
		 mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
		 //��3����������Ƶ���뷽ʽ��Ĭ�ϵı��뷽ʽ��
		 mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);	
		 //����һ����ʱ����Ƶ����ļ�
		 File audioFile = new File(FileFullPathStr);
		 //��4����ָ����Ƶ����ļ�
		 mediaRecorder.setOutputFile(audioFile.getAbsolutePath());		
		//��5��������prepare����
	    try {
			mediaRecorder.prepare();
		} catch (IllegalStateException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	 }
	 
	 /**
	  * ¼����ʼ
	  */
	 public void Start(){
		 try {
			if (mediaRecorder != null) {
				mediaRecorder.start();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	 }
	 
	 /**
	  * ¼������
	  */
	 public void Stop(){
		 try {
			if (mediaRecorder != null) {
				//ֹͣ¼��
				mediaRecorder.stop();
				mediaRecorder.release();//¼�������ͷ������Դ
				mediaRecorder = null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	 }
	 
	 /**
	  * ��ȡ������С
	  * @return
	  */
	 public int getAmplitudeet(){
		if (mediaRecorder != null){			
			return  (mediaRecorder.getMaxAmplitude());		
			}		
		else			
			return 0;	
	 }

}

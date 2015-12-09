package com.esri.android.tasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

public class BaseTaskPackageActivity  extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	        AlertDialog aDlg=createSysConfirmDialog();
	        aDlg.show();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	/**
	 *ϵͳ�˳�����
	 * @return
	 */
	private AlertDialog createSysConfirmDialog()
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setMessage("ȷ���˳�ϵͳ?");
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
				// TODO Auto-generated method stub
				exitSystem();
			}
		});
		
		return builder.create();
	}
	private void exitSystem()
	{
		this.finish();
	}
	
}

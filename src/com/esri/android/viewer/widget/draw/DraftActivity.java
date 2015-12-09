package com.esri.android.viewer.widget.draw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerApp;
import com.esri.android.viewer.base.BaseViewerActivity;
import com.esri.android.viewer.draft.ColorPickerDialog;
import com.esri.android.viewer.draft.Draft;
import com.esri.android.viewer.draft.PathStore;
import com.esri.android.viewer.draft.PaintView;
import com.esri.android.viewer.draft.PaintView.OnPaintListener;
import com.esri.android.viewer.draft.PathStore.node;
import com.esri.android.viewer.draft.WidthPickerDialog;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class DraftActivity extends Activity {

	private Context context;
	private PaintView paint;
	private PathStore myPathStore;//ȫ�ֱ���
	
	private String ScreenShutPath ;//����ͼƬ�洢·��
	private String editLayerName;//����ͼ����
	private String featureID;//Ҫ��ΨһID
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ViewerApp appState = ((ViewerApp)getApplicationContext());            
		ScreenShutPath= appState.getFilePaths().mainFilePath+"/biz/pacage_tmp/draft";//��ȡϵͳ�ļ���·��
		
		//��ȡ��һ��ҳ��Ĵ�ֵ��Ҫ�ض�ý��洢·����
	    Bundle bundle = getIntent().getExtras();  
	    ScreenShutPath = bundle.getString("draftPath");  
	    editLayerName = bundle.getString("editLayerName");  
	    featureID = bundle.getString("featureID");
		
		context =this;
		myPathStore=new PathStore();
        setContentView(R.layout.activity_draft);
		paint=(PaintView)findViewById(R.id.esri_androidviewer_draftpanal);
		paint.setOnPaintListener(new OnPaintListener() {		
			public void paint(float x, float y, int action) {
				// TODO Auto-generated method stub
				PathStore.node tempnode=myPathStore.new node();
				tempnode.x=x;
				tempnode.y=y;
				tempnode.action=action;
				tempnode.time=System.currentTimeMillis();
				myPathStore.addNode(tempnode);
			}
		});
		
		ImageButton btnClear = (ImageButton) findViewById(R.id.esri_androidviewer_draft_clear);
		btnClear.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO ����
				paint.clean();
				myPathStore.cleanStore();
				}});

		ImageButton penWidthPicker=(ImageButton)findViewById(R.id.esri_androidviewer_draft_penwidth);
		penWidthPicker.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				// TODO ������ ��ϸ
				WidthPickerDialog dialog=new WidthPickerDialog(context, new WidthPickerDialog.OnWidthChangedListener() {	
					public void widthChanged(int penWidth) {
						// TODO Auto-generated method stub
						paint.setPenWidth(penWidth);
					}
				});
				dialog.show();
				dialog.setPenWidth(paint.getPenWidth());
			}
		});
		
		final ImageButton colorPicker=(ImageButton)findViewById(R.id.esri_androidviewer_draft_color);
		colorPicker.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO ���û�ˢ��ɫ
				ColorPickerDialog	dialog = new ColorPickerDialog(context, Color.BLACK,  "������ɫ",   
                        	new ColorPickerDialog.OnColorChangedListener() {
		                    public void colorChanged(int color) {  
		                        paint.setColor(color);
		                        //colorPicker.setBackgroundColor(color);
		                    }  
                		});  
                dialog.show();
			}
		});
		
		ImageButton screenshot =(ImageButton)findViewById(R.id.esri_androidviewer_draft_screenshot);
		screenshot.setOnClickListener(new OnClickListener() {		
			public void onClick(View v) {
				// TODO ����
				AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
				builder.setMessage("�Ƿ񱣴�ý�ͼ?");
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
						// TODO ����					
						try {
							getWindow().getDecorView().setDrawingCacheEnabled(true);
							Bitmap  bmp=getWindow().getDecorView().getDrawingCache();
							String filename = ScreenShutPath+"/"+editLayerName+"_"+featureID+"_"+com.esri.android.viewer.tools.sysTools.getTimeNow()+".jpg";
							File file = new File(filename);
							bmp.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
							 Toast.makeText(DraftActivity.this,"��ͼ����ɹ���",Toast.LENGTH_SHORT).show();
							 CommonTools.updateFeatureState(DrawWidget.taskPackageSimpleDBPath, editLayerName, featureID);//����Ҫ��״̬--����Ϊ�ѱ༭
							 DraftActivity.this.finish();//������ǰ����					 
						} catch (FileNotFoundException e) {
							// TODO �Զ����ɵ� catch ��
							Toast.makeText(DraftActivity.this,"��ͼʧ�ܣ�",Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
						
					}
				});
				builder.show();
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_draft, menu);
		return false;
	}

}

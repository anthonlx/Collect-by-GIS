package com.esri.android.tasks;

import java.util.ArrayList;
import java.util.List;

import com.esri.android.login.SysConfigActivity;
import com.esri.android.login.UserLoginActivity;
import com.esri.android.login.UserRegisterActivity;
import com.esri.android.viewer.BaseMapActivity;
import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerActivity;
import com.esri.android.viewer.ViewerApp;
import com.esri.android.viewer.tools.SystemVariables;
import com.esri.android.viewer.tools.fileUtil.file;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaskPackageActivity extends BaseTaskPackageActivity {

	private ListView tasklistView;
	private ProgressDialog mProgressDlg=null;
	private RefreshHandler refreshHandler =new RefreshHandler();
	private List<TaskInfo> taskinfolist=null;//�������Ϣ�б�
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_package);
		/*1.���������ļ��У�Ϊ�ճ�����ֹ����ʾ�������ȡ�������̣�
		 * 2.��ʼ��������б����첽
		 * 3.��ȡ�û�ID���豸ID������ID��ϵͳȫ������
		 * 4.��ʼ����ͼ����
		 */	
		tasklistView =(ListView)this.findViewById(R.id.activity_task_package_listview);
		LoadTackpackage();	
	}

	private void LoadTackpackage() {
		mProgressDlg = new ProgressDialog(TaskPackageActivity.this);
		mProgressDlg = ProgressDialog.show(TaskPackageActivity.this, "", "�������ʼ����...");
		refreshHandler.sleep(2000);
	}

	/**
	 * ��ʼ��TaskInfo�б�ֵ     2015.12.9  
	 * @param file
	 * @return
	 */
	private TaskInfo AddTaskInfo(file file) {
		 try {
			 TaskInfo taskinfo =null;
			 String dbpath = file.path +"/"+file.item+".sqlite"; 
			 SQLiteDatabase mDb = SQLiteDatabase.openDatabase(dbpath, null, 0);
			 Cursor cursor = mDb.query("BIZ_TASKS", new String[] {
					"F_TASKID", "F_NAME" ,"F_DESC","F_DISTRIBUTOR","F_EXECUTOR","F_DEADLINE"}, null, null,
					null, null, null);
			 cursor.moveToFirst();
			 taskinfo = new TaskInfo();
			 int id = cursor.getInt(cursor.getColumnIndex("F_TASKID"));
			 String name = cursor.getString(cursor.getColumnIndex("F_NAME"));
			 String desc = cursor.getString(cursor.getColumnIndex("F_DESC"));
			 int distri = cursor.getInt(cursor.getColumnIndex("F_DISTRIBUTOR"));
			 int execut = cursor.getInt(cursor.getColumnIndex("F_EXECUTOR"));
			 String deadline = cursor.getString(cursor.getColumnIndex("F_DEADLINE"));
			 
			 taskinfo.filename = file.item;
			 taskinfo.filepath = file.path;
			 taskinfo.id = id;
			 taskinfo.name = name;
			 taskinfo.desc = desc;
			 taskinfo.distributor_id = distri;
			 taskinfo.executor_id = execut;		
			 taskinfo.deadline = deadline;
			 return taskinfo;
		 } catch (Exception e) {
				// TODO: handle exception
			String str = file.item + "�ڲ�����\n����������ռ����ʧ�ܣ�����������ṹ�Ƿ���ȷ��\n";
			final String path = file.path;			
			Toast.makeText(TaskPackageActivity.this.getApplicationContext(),str+e.toString(),Toast.LENGTH_SHORT).show();
			//���ڽṹ��������������ʼ����ɾ���乤���ռ�
			new Thread(new Runnable(){
	            @Override
	            public void run() {   
	            	 boolean bool = com.esri.android.viewer.tools.fileTools.deleteFiles(path);
	            }   
		        }).start();
			return null;
		 } 
	}
	 
	/**
	  * ��ȡ������ļ����б�
	 * @return 
	  */
	private  List<file> getTaskPackList() {
		// TODO 	
		//��ȡϵͳ�����Ŀ¼
		ViewerApp appState = ((ViewerApp)getApplicationContext()); 
		com.esri.android.viewer.tools.fileTools.filePath  path = appState.getFilePaths();
		String	taskPackagePath = path.taskPackageFilePath.toString();
		com.esri.android.viewer.tools.fileUtil fileutil = new com.esri.android.viewer.tools.fileUtil();
		return fileutil.getFileDir(taskPackagePath,"folder");	
	}
	
	/**
	 * ϵͳ��ʾ���������Ϊ��
	 * @return
	 */
	private AlertDialog createConfirmDialog()
	{
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		builder.setMessage("�����Ŀ¼Ϊ�գ������Գ���һ�����ַ�ʽ��ȡ�������\n1.��������������ļ� \n2.��������ļ���*.sqlite��������Collect for ArcGIS\\biz\\Ŀ¼�£�");
		builder.setCancelable(true);
		builder.setTitle("ϵͳ��ʾ");
		builder.setPositiveButton("ȡ��", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//TaskPackageActivity.this.finish();
			}
		});
		builder.setNeutralButton("���������", new DialogInterface.OnClickListener() 
		{
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent i = new Intent(TaskPackageActivity.this, TaskManagerActivity.class);
				TaskPackageActivity.this.startActivity(i);
				//TaskPackageActivity.this.finish();
			}
		});
		return builder.create();
	}
		
	/**
	 * ���������ļ������Ƿ��������
	 * @return  true ���������   false �����������
	 */
	private boolean checkTaskPackage(){
		boolean result=true;//Ĭ�ϴ���
	    ViewerApp appState = ((ViewerApp)getApplicationContext());
        com.esri.android.viewer.tools.fileTools.filePath  WorkSpacePath = appState.getFilePaths();
	  	//��ȡ������µ�sqlite�ļ��б�������Ӧ�Ķ�ý���ļ���
		String bizPath = WorkSpacePath.taskPackageFilePath;//�����·��
		com.esri.android.viewer.tools.fileUtil fileutil = new com.esri.android.viewer.tools.fileUtil();
		List<com.esri.android.viewer.tools.fileUtil.file> filelist = fileutil.getFileDir(bizPath, "sqlite");
		List<com.esri.android.viewer.tools.fileUtil.file> fileworklist = fileutil.getFileDir(bizPath, "folder");
		if(filelist.isEmpty()&&fileworklist.isEmpty()) {
			//ͬʱΪ��
			result =false;
		}
		return result;	
		
	}
	
	   /**
     * ��ʼ��������ļ���
     */
	private void intiTaskPage() {
        ViewerApp appState = ((ViewerApp)getApplicationContext());
        com.esri.android.viewer.tools.fileTools.filePath  WorkSpacePath = appState.getFilePaths();
	  	//��ȡ������µ�sqlite�ļ��б�������Ӧ�Ķ�ý���ļ���
		String bizPath = WorkSpacePath.taskPackageFilePath;//�����·��
		com.esri.android.viewer.tools.fileUtil fileutil = new com.esri.android.viewer.tools.fileUtil();
		List<com.esri.android.viewer.tools.fileUtil.file> filelist = fileutil.getFileDir(bizPath, "sqlite");
		for (com.esri.android.viewer.tools.fileUtil.file file : filelist) {
			String[] arr = file.item.toString().split("\\.");//��ȡ���ݿ��ļ���
			//���������
			com.esri.android.viewer.tools.fileTools.initPackageDir(bizPath, arr[0]);
			//ƴ�����ļ��洢·��
			String toPath = bizPath
					+ "/"
					+ arr[0]
					+ "/"
					+ file.item;
			boolean isEsist = com.esri.android.viewer.tools.fileTools
					.isExist(toPath);//�ж�������Ƿ����
			if (!isEsist) {
				com.esri.android.viewer.tools.fileTools.copyFile(file.path, toPath);
				//Toast.makeText(ViewerActivity.this,"������ṹ��ʼ���ɹ���", Toast.LENGTH_SHORT).show();		
			}	
		  }
		  
	}
	
	/**
	 *  ��ʼ������listview����
	 */
	public void intiTaskListView() {
		taskAdapter adapter = new taskAdapter(this.getLayoutInflater());	
		//��Ӳ�����ʾ
		tasklistView.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
	    int base = Menu.FIRST;
		menu.add(base, base+1, base+1, "���������");
        menu.add(base, base+2, base+2, "��ͼ������");
        menu.add(base, base+3, base+3, "��������");
        menu.add(base, base+4, base+4, "GPS����");
        menu.add(base, base+5, base+5, "�����ַ����");
        getMenuInflater().inflate(R.menu.activity_task_package, menu);
	    // ��ʾ�˵�
	    return true;

	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		 switch(id){
	        case 2:
	        	 Intent intent = new Intent(TaskPackageActivity.this, TaskManagerActivity.class);  //��ת���������
				 startActivity(intent);
	        	break;
	        case 3:
	        	 Intent intent2 = new Intent(TaskPackageActivity.this, BaseMapActivity.class);  //��ת����ͼ������
				 startActivity(intent2);
	        	break;
	        case 4:
	        	startActivity(new Intent(Settings.ACTION_SETTINGS));
	        	break;
	        case 5:
	        	startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	        	break;
	        case 6:
	        	Intent intent_sys = new Intent(TaskPackageActivity.this, SysConfigActivity.class);    
	        	startActivity(intent_sys);
	        	break;
	       default:
	    	  if(item.getTitle().equals("ˢ��")){
	    		  LoadTackpackage();	
	    	  }else if(item.getTitle().equals("������")){
	    		  
	    	  }
	         break;
     }    
 	return true;
    }

	class RefreshHandler extends Handler{	 
		  @Override
		   public void handleMessage(Message msg) {
			    try{ 
			    	boolean is =checkTaskPackage();
					if(is){		
						//��������ڳ�ʼ���б�
						intiTaskPage();
						List<file> taskpathlist = getTaskPackList();//������ļ����б�
						taskinfolist = new ArrayList<TaskInfo>(); 
						for(int i=0;i<taskpathlist.size();i++){
							TaskInfo taskinfo = AddTaskInfo(taskpathlist.get(i));
							if (taskinfo!=null) {
								taskinfolist.add(taskinfo);
							}
						}
						//��ʼ�����������
						intiTaskListView();
					}else{
						//�����������
						AlertDialog aDlg=createConfirmDialog();
					    aDlg.show();
					}
			   }catch(Exception e){
				    e.printStackTrace();
			    }finally{
			    	mProgressDlg.dismiss();//���������
			    }
		   }
		   
		   public void sleep(long delayMillis){
			    this.removeMessages(0);
			    sendMessageDelayed(obtainMessage(0), delayMillis);
		   }
		 }

	public final class TaskViewHolder{//�б����
		public TextView id;
		public TextView name;
	    public TextView desc;
	    public TextView endtime;
	    public TextView path;
	    public Button BtnIntomap;
	    public Button BtnInfo;
	    public Button BtnDel;
	}
	
	public class taskAdapter extends BaseAdapter{
	        private LayoutInflater mInflater;

	        public taskAdapter(LayoutInflater Inflater){
	            this.mInflater = Inflater;
	        }

	        public int getCount() {
	            return taskinfolist.size();
	        }

	        public Object getItem(int arg0) {
	            return null;
	        }

	        public long getItemId(int arg0) {
	            return 0;
	        }

	        public void refreshData(){	 
	        	List<file> taskpathlist = getTaskPackList();//������ļ����б�
				taskinfolist = new ArrayList<TaskInfo>(); 
				for(int i=0;i<taskpathlist.size();i++){
					TaskInfo taskinfo = AddTaskInfo(taskpathlist.get(i));
					if (taskinfo!=null){
						taskinfolist.add(taskinfo);
					}
				}
	        	notifyDataSetChanged();//ˢ������
	        } 
	        
	        public View getView(final int position, View convertView, ViewGroup parent) {
	        	TaskViewHolder holder = null;
	            if (convertView == null) {
	                holder=new TaskViewHolder();  
	                convertView = mInflater.inflate(R.layout.view_task_package_list_item, null);
	                holder.id = (TextView)convertView.findViewById(R.id.view_task_package_list_item_id);
	                holder.name = (TextView)convertView.findViewById(R.id.view_taskpackage_list_online_item_title);
	                holder.desc = (TextView)convertView.findViewById(R.id.view_task_package_list_item_remark);
	                holder.endtime = (TextView)convertView.findViewById(R.id.view_task_package_list_item_endtime);
	                holder.path = (TextView)convertView.findViewById(R.id.view_task_package_list_item_path);
	                holder.BtnIntomap =(Button)convertView.findViewById(R.id.view_taskpackage_item_btnintomap);
	                holder.BtnInfo = (Button)convertView.findViewById(R.id.view_taskpackage_item_btninfo);
	                holder.BtnDel = (Button)convertView.findViewById(R.id.view_taskpackage_item_btndel);
	                convertView.setTag(holder);
	            }else {
	                holder = (TaskViewHolder)convertView.getTag();
	            }
	            holder.id.setText(String.valueOf(taskinfolist.get(position).id));
	            holder.name.setText((String)taskinfolist.get(position).name);
	            holder.desc.setText((String)taskinfolist.get(position).desc);
	            holder.endtime.setText(String.valueOf(taskinfolist.get(position).deadline));  
	            String strpath ="/"+com.esri.android.viewer.tools.SystemVariables.taskPackageDirectory
	            		+"/"+taskinfolist.get(position).filename;
				holder.path.setText(strpath);
	            holder.BtnIntomap.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						 Intent intent = new Intent(TaskPackageActivity.this, ViewerActivity.class);  //��ת���б�ҳ
			    	     // ����������� db���ݿ��ַ������ID���û�ID������ִ���ˣ�
						 Bundle bundle = new Bundle();
						 String taskpath = taskinfolist.get(position).filepath;
						 String tasppackagename = taskinfolist.get(position).filename;
						 bundle.putInt("taskID", taskinfolist.get(position).id);
						 //bundle.putInt("userID", taskinfolist.get(position).executor_id);//ִ����ID
						 bundle.putString("taskpath", taskpath);//���ݿ��ַ
						 bundle.putString("tasppackagename", tasppackagename);//���ݿ�����
						 intent.putExtras(bundle);
						 TaskPackageActivity.this.startActivity(intent);
					}});
	            holder.BtnInfo.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String dbsize = com.esri.android.viewer.tools.sysTools.getFileSize(taskinfolist.get(position).filepath);
	                	String filepic = taskinfolist.get(position).filepath+"/" +SystemVariables.PicturesDirectory;
	                	String filevideo =  taskinfolist.get(position).filepath+"/" +SystemVariables.VideosDirectory;
	                	String filevoice =  taskinfolist.get(position).filepath+"/" +SystemVariables.VoicesDirectory;
	                	String filedraft =  taskinfolist.get(position).filepath+"/" +SystemVariables.DraftsDirectory;	
	                	String info = 	
	                			 //"����ID:" + taskinfolist.get(position).id
	                			 //+"\n��������:" + taskinfolist.get(position).name
	                			 //+"\n����˵��:" + taskinfolist.get(position).desc
	                			 //+"\nԤ�����ʱ��:" + taskinfolist.get(position).deadline
	                		     //	"Ҫ�زɼ��� 168 \n���Ա༭�� 97 \n"+
	                			 "ͼƬ�ɼ��� " +com.esri.android.viewer.tools.fileTools.getFilesNum(filepic)
	                			 +"\n��Ƶ�ɼ��� " +com.esri.android.viewer.tools.fileTools.getFilesNum(filevideo)
	                			 +"\n¼���ɼ��� " +com.esri.android.viewer.tools.fileTools.getFilesNum(filevoice)
	                			 +"\n��ͼ�ɼ��� "+com.esri.android.viewer.tools.fileTools.getFilesNum(filedraft);	                	
	                	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	            		//builder.setIcon(R.drawable.ic_launcher);
	            		builder.setTitle("״̬");
	            		builder.setMessage(info);
	            		builder.setPositiveButton("ȷ��",
	            				new DialogInterface.OnClickListener() {
	            					public void onClick(DialogInterface dialog, int whichButton) {
	            					
	            						
	            					}
	            				});
//	            		builder.setNegativeButton("ȡ��",
//	            				new DialogInterface.OnClickListener() {
//	            					public void onClick(DialogInterface dialog, int whichButton) {
//	            				
//	            					}
//	            				});
	            		builder.show();	                	
					}});
	            holder.BtnDel.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(final View v) {
						// TODO Auto-generated method stub
						AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
	            		//builder.setIcon(R.drawable.ic_launcher);
	            		builder.setTitle("ϵͳ��ʾ");
	            		builder.setMessage("ɾ������������ռ䣬��ɾ������������ռ��������ļ���\n1.����������ڣ�����������ռ佫�ڳ����ٴ�����ʱ��ԭΪ��ʼ״̬��\n2.��������������򳹵�ɾ����\n�Ƿ���Ȼ����ɾ����");
	            		builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
        			        @Override
        					public void onClick(DialogInterface dialog, int whichButton) {
        					  boolean isdel = com.esri.android.viewer.tools.fileTools.deleteFiles(taskinfolist.get(position).filepath); 
        					  if(isdel){
        						  Toast.makeText(v.getContext(),"����������ռ�ɾ���ɹ���",Toast.LENGTH_SHORT).show();
        						  refreshData();//ˢ�½���
        					  }	  
        					}
	            		});
	            		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {								
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO �Զ����ɵķ������
								
							}
						});
	            		builder.show();	                			
					}});
	            
	            return convertView;
	        }
	    }
	 
}

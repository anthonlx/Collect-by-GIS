package com.esri.android.tasks;

import java.util.List;

import com.esri.android.viewer.R;
import com.esri.android.viewer.tools.fileUtil.file;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OfflineTaskAdapter extends BaseAdapter{
	
		public class OfflineTaskViewHolder{//�б����
		    public TextView title;
		    public TextView path;
		    public Button BtnInfo;
		    public Button BtnDel;
		}
	
        private LayoutInflater mInflater;
        List<file> taskPackage_db_File_List =null;
        String taskPackagePath;
        
        public OfflineTaskAdapter(LayoutInflater inflater,
				List<file> list,String path) {
        	this.mInflater = inflater;
        	taskPackage_db_File_List = list;
        	taskPackagePath = path;
		}

		public int getCount() {
			// TODO Auto-generated method stub
            return taskPackage_db_File_List.size();
        }

        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int arg0) {
            // TODO Auto-generated method stu
            return 0;
        }

        public void refreshData(){	        	
        	notifyDataSetChanged();//ˢ������
        } 
        
        public View getView(final int position, View convertView, ViewGroup parent) {
        	OfflineTaskViewHolder holder = null;
            if (convertView == null) {
                holder=new OfflineTaskViewHolder();  
                convertView = mInflater.inflate(R.layout.view_taskpackage_list_offline_item, null);
                holder.title = (TextView)convertView.findViewById(R.id.view_taskpackage_list_online_item_title);
                holder.path = (TextView)convertView.findViewById(R.id.taskPackageItemPath);
                holder.BtnInfo = (Button)convertView.findViewById(R.id.view_taskpackage_offline_item_btninfo);
                holder.BtnDel = (Button)convertView.findViewById(R.id.view_taskpackage_offline_item_btndel);
                convertView.setTag(holder);
            }else {
                holder = (OfflineTaskViewHolder)convertView.getTag();
            }
            final String dbname = (String)taskPackage_db_File_List.get(position).item;
            holder.title.setText(dbname);
            holder.path.setText((String)taskPackage_db_File_List.get(position).path);
            holder.BtnInfo.setOnClickListener(new View.OnClickListener() {     	
                public void onClick(View v) {	  
                	String dbsize = com.esri.android.viewer.tools.sysTools.getFileSize(taskPackage_db_File_List.get(position).path);
                	String info = "����:" + dbname
                			 +"\n��С��"+dbsize;	                	
                	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            		//builder.setIcon(R.drawable.ic_launcher);
            		builder.setTitle("��ϸ��Ϣ");
            		builder.setMessage(info);
            		builder.setPositiveButton("ȷ��",
            				new DialogInterface.OnClickListener() {
            					public void onClick(DialogInterface dialog, int whichButton) {
            						
            					}
            				});
            		builder.show();	                	
                }
            });
            holder.BtnDel.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                	AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            		//builder.setIcon(R.drawable.ic_launcher);
            		builder.setTitle("ϵͳ��ʾ");
            		builder.setMessage("ɾ������������᳹��ɾ�������ԭʼ�ļ����Ƿ����ɾ����");
            		builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
    			        @Override
    					public void onClick(DialogInterface dialog, int whichButton) {
    					  String path = taskPackage_db_File_List.get(position).path;
    					  //String workspacepath = path.replace(".sqlite","");
    					  boolean isfiledel = com.esri.android.viewer.tools.fileTools.deleteFiles(path);
    					  //boolean isworkspacedel = com.esri.android.viewer.tools.fileTools.deleteFiles(workspacepath);	  
    					  if(isfiledel){
    						  Toast.makeText(v.getContext(),"����������ļ�ɾ���ɹ���",Toast.LENGTH_SHORT).show();
    						  getTaskPackList();//���»�ȡ������б�
    						  refreshData();//ˢ�½���
    					  }	  
    					}

						private void getTaskPackList() {
							// TODO Auto-generated method stub
							com.esri.android.viewer.tools.fileUtil fileutil = new com.esri.android.viewer.tools.fileUtil();
							taskPackage_db_File_List = fileutil.getFileDir(taskPackagePath,".sqlite");	
						}
            		});
            		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {								
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO �Զ����ɵķ������
							
						}
					});
            		builder.show();	                	
                }
            });
            return convertView;
        }


}
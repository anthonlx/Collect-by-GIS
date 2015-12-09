package com.esri.android.tasks;

import com.esri.android.viewer.R;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class OnlineTaskAdapter extends BaseAdapter{
	
		public class onlineTaskViewHolder{//�б����
		    public TextView title;//��������
		    public TextView name;//�洢����
		    public TextView path;//����·��
		    public TextView download_progress;//���ؽ���
		    public ImageView imgbtn;//��ʾ���ذ�ť
		    public LinearLayout downloading_controller;
		    public ProgressBar progressbar;
		    public Button BtnDownload;//����
		    public Button BtnDel;
		}
	
        private LayoutInflater mInflater;
        private Context conext;
        
        public OnlineTaskAdapter(LayoutInflater inflater,Context c) {
        	this.mInflater = inflater;
        	this.conext = c;
		}

		public int getCount() {
			// TODO Auto-generated method stub
            return Gvariable.downloadQueue.size();
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
          onlineTaskViewHolder holder = null;
            if (convertView == null) {
                holder=new onlineTaskViewHolder();  
                convertView = mInflater.inflate(R.layout.view_taskpackage_list_online_item, null);
                holder.title = (TextView)convertView.findViewById(R.id.downloading_name);
                holder.name =  (TextView)convertView.findViewById(R.id.downloading_filename);
                holder.path = (TextView)convertView.findViewById(R.id.download_urltext);
                holder.imgbtn = (ImageView)convertView.findViewById(R.id.icon_arrow);
                holder.progressbar = (ProgressBar)convertView.findViewById(R.id.progressbar);
                holder.download_progress = (TextView)convertView.findViewById(R.id.download_progress);
                holder.downloading_controller =(LinearLayout)convertView.findViewById(R.id.downloading_controller);
                holder.BtnDownload = (Button)convertView.findViewById(R.id.start);
                convertView.setTag(holder);
            }else {
                holder = (onlineTaskViewHolder)convertView.getTag();
//                int progre = holder.progressbar.getProgress();
//                if(progre==0||progre==100)
//                {
//                	holder.
//                }
            }
            String[] tile = Gvariable.downloadQueue.get(position).getFileName().split("��");
            holder.title.setText(tile[0]);
            holder.name.setText(tile[1]);
            String url = Gvariable.downloadQueue.get(position).getDownLoadUrl();
            holder.path.setText(url);
            holder.imgbtn.setOnClickListener(new imgOnClickListener(convertView,holder));
            holder.BtnDownload.setOnClickListener(new DownLoadingBtnClickListener(convertView,holder,position,conext));
           
            return convertView;
        }
       
        
		public class imgOnClickListener implements OnClickListener {
		onlineTaskViewHolder holder =null;
		View convertView = null;  
		private ImageView icon_arrow;
		
		public imgOnClickListener(View v, onlineTaskViewHolder h) {
				// TODO Auto-generated constructor stub
			this.holder = h;
			this.convertView = v;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			icon_arrow = (ImageView) convertView.findViewById(R.id.icon_arrow);
			int vState = holder.downloading_controller.getVisibility();
			if (View.GONE == vState) {
				icon_arrow.setImageResource(R.drawable.icon_arrow_up);
				holder.downloading_controller.setVisibility(View.VISIBLE);
			} else {
				icon_arrow.setImageResource(R.drawable.icon_arrow_down);
				holder.downloading_controller.setVisibility(View.GONE);
			}
		}

	}

		
		/**
		 * �����¼�����
		 * @author lq
		 */
		public class DownLoadingBtnClickListener implements OnClickListener  {

			private View convertView ;
			private int position;
			private onlineTaskViewHolder hloder;
			private Context context;
			
			private static final int UPDATE_PROGRESS = 0;
			
			private Intent service;
			private DownLoadServiceConnection conn;
			private IGetProgress ibinder;

			public DownLoadingBtnClickListener(View c,onlineTaskViewHolder h, int p,Context con) {
				this.convertView = c;
				this.hloder = h;
				this.position = p;
				this.context = con;
			}

			Handler handler = new Handler() {
				public void handleMessage(Message msg) {
					int key = msg.what;
					switch (key) {
					case UPDATE_PROGRESS:
						if ((Integer) msg.obj < 100) {
							hloder.progressbar.setMax(100);
							hloder.progressbar.setProgress((Integer) msg.obj);
							hloder.download_progress.setText("������ " + (Integer) msg.obj + "%");
						} else {
							hloder.progressbar.setProgress((Integer) msg.obj);
							hloder.download_progress.setText("������ " + (Integer) msg.obj + "%");
							//���±��������
							TaskManagerActivity.getLocalTaskPackList();//���±���������б�
							TaskManagerActivity.Offlineadapter = new OfflineTaskAdapter(TaskManagerActivity.inflater_all ,
									TaskManagerActivity.taskPackage_db_File_List,
									TaskManagerActivity.taskPackagePath);	
							TaskManagerActivity.OfflineTaskListView.setAdapter(TaskManagerActivity.Offlineadapter);
							String downname  = Gvariable.downloadQueue.get(position).getFileName();
							TaskManagerActivity.taskls.remove(downname);//�б��������������ɵ�
						}
						break;
					default:
						break;
					}
				}

			};

			public void onClick(View v) {
				int id = v.getId();
				switch (id) {
				case R.id.start:
					v.setEnabled(false);
					service = new Intent(context, DownLoadService.class);
					conn = new DownLoadServiceConnection();
					TaskManagerActivity.context.bindService(service, conn, Context.BIND_AUTO_CREATE);	
					String downname  = Gvariable.downloadQueue.get(position).getFileName();
					TaskManagerActivity.taskls.add(downname);//��ӵ����ض�����
					break;
				case R.id.pause:
					break;
				case R.id.delete:
					break;
				default:
					break;
				}
		}
			
		/**
		 * ���������࣬����������ӺͶϿ�ʱ������
		 */
		private class DownLoadServiceConnection implements ServiceConnection {
				public void onServiceConnected(ComponentName name, IBinder service) {
					ibinder = (IGetProgress) service;
					ibinder.setHandlerAndFileInfo(handler,  Gvariable.downloadQueue.get(position));
				}

				public void onServiceDisconnected(ComponentName name) {
					conn = null;
					ibinder = null;
				}
			}	
		 }
		 
		Handler handler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	       
	        }
		 };
		 
}
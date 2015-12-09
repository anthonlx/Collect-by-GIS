package com.esri.android.viewer.widget.draw;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.esri.android.viewer.R;
import com.esri.android.viewer.ViewerApp;
import com.esri.android.viewer.BaseMapActivity.DummySectionFragment.MyAdapter;
import com.esri.android.viewer.BaseMapActivity.DummySectionFragment.ViewHolder;
import com.esri.android.viewer.base.BaseViewerActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MultiMediaActivity extends FragmentActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	
	
	private static final int TAKE_PICTURE = 0;//���ջ�ȡͼƬ
	private static final int CHOOSE_PICTURE = 1;//�����ѡ��ͼƬ
	private static final int  ACTION_TAKE_VIDEO =2;//¼����Ƶ
  
	private String Orientation ="";//��¼��ǰ�ֻ�״̬��Ϣ
	private static com.esri.android.viewer.tools.fileTools.filePath WoskSpaceFilePath = null;//ϵͳ�ļ���·��
	
	private static String featurepath = "";//Ҫ��·�� ��¼��Ҫ�ع������ļ���·������·����
	private static String picture_path = "";// ͼƬĿ¼
	private static String video_path = "";// ��ƵĿ¼
	private static String voice_path  ="";//����Ŀ¼
	private static String draft_path = "";//��ͼĿ¼
	
	private static String FeatureID = "";//Ҫ��ID
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_media);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		//GetOrientation();//����ֻ�״̬��Ϣ����
	
		//��ȡ��һ��ҳ��Ĵ�ֵ��Ҫ�ض�ý��洢·����
	    Bundle bundle = getIntent().getExtras();  
	    featurepath = bundle.getString("taskPackageSimplePath");  
	    FeatureID = bundle.getString("featureID");  
	    this.picture_path = featurepath +"/"+com.esri.android.viewer.tools.SystemVariables.PicturesDirectory;
        this.video_path = featurepath +"/"+com.esri.android.viewer.tools.SystemVariables.VideosDirectory;
        this.voice_path = featurepath +"/"+com.esri.android.viewer.tools.SystemVariables.VoicesDirectory;
        this.draft_path = featurepath +"/"+com.esri.android.viewer.tools.SystemVariables.DraftsDirectory;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_multi_media, menu);
		return true;
	}


	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}


	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}


	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.multi_media_activity_voice).toUpperCase();
			case 1:
				return getString(R.string.multi_media_activity_camera).toUpperCase();
			case 2:
				return getString(R.string.multi_media_activity_video).toUpperCase();
			case 3:
				return getString(R.string.multi_media_activity_sketch).toUpperCase();
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		 com.esri.android.viewer.tools.fileUtil fileutil = new com.esri.android.viewer.tools.fileUtil();
		 List<com.esri.android.viewer.tools.fileUtil.file> file_list = null;			
         int num_view ;
		 @Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
			//˵����������1,2,3,4,5���ҳ�桪����ʼ�����Զ�ˢ��ҳ��1,2
			//��������ҳ��2ʱ����ʼ��ҳ��3��������ҳ��3ʱ����ʼ��ҳ��4���Դ�����
			int num = getArguments().getInt(ARG_SECTION_NUMBER);
			num_view = num;
			View rootView =null;// ��ҳ��	
			 get_file_list(num_view);
			 rootView = inflater.inflate(R.layout.view_list, container, false); 	 			
			 //��XML�е�ListView����ΪItem������
			 ListView listView =(ListView) rootView.findViewById(R.id.listView);
			 multiAdapter adapter = new multiAdapter(inflater);
			 listView.setAdapter(adapter);
			 
			return rootView;
		}

		private void get_file_list(int num) {
				switch(num)
				 {
					 case 1:
						 //��ȡ��Ƶ�ļ������ļ�
					   	  file_list =fileutil.getFileDir(voice_path,FeatureID);				 
						    break;
					 case 2:
						 //��ȡͼƬ�ļ������ļ�
					   	  file_list =fileutil.getFileDir(picture_path,FeatureID);
							break;
					 case 3:
						 //��ȡ��Ƶ�ļ������ļ�
					   	  file_list =fileutil.getFileDir(video_path,FeatureID);
						    break;
					 case 4:
						  //��ȡ��ͼ�ļ������ļ�
					   	  file_list =fileutil.getFileDir(draft_path,FeatureID);
						    break;
					 default:
							break;
				 }
			}
		
	   public final class ViewMultiHolder{//�б����
	        public TextView title;
	    }
		
		public class multiAdapter extends BaseAdapter{
		        private LayoutInflater mInflater;

		        public multiAdapter(LayoutInflater Inflater){
		            this.mInflater = Inflater;
		        }

		        public int getCount() {
		            // TODO Auto-generated method stub
		            return file_list.size();
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
		        
		        public View getView( final int position, View convertView, ViewGroup parent) {
		        	ViewMultiHolder holder = null;
		            if (convertView == null) {
		                holder=new ViewMultiHolder();  
		                convertView = mInflater.inflate(R.layout.view_list_item, null);
		                holder.title = (TextView)convertView.findViewById(R.id.esri_androidviewer_view_list_item_txtcontenct);
		                convertView.setTag(holder);
		            }else {
		                holder = (ViewMultiHolder)convertView.getTag();
		            }
		            holder.title.setText((String)file_list.get(position).item);         
		            holder.title.setOnLongClickListener(new View.OnLongClickListener() {
						@Override
						public boolean onLongClick(final View v) {
							// TODO �Զ����ɵķ������
							AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
		            		//builder.setIcon(R.drawable.ic_launcher);
		            		builder.setTitle("ϵͳ��ʾ");
		            		builder.setMessage("�Ƿ�ɾ��\""+file_list.get(position).item+"\"��");
		            		builder.setPositiveButton("ȷ��",
		            				new DialogInterface.OnClickListener() {
		            					public void onClick(DialogInterface dialog, int whichButton) {
		            					    boolean isDel= com.esri.android.viewer.tools.fileTools.deleteFiles(file_list.get(position).path);
		            					    if(isDel){
		            					    	Toast.makeText(v.getContext(),"ɾ���ɹ���",Toast.LENGTH_SHORT).show();
		            					    	get_file_list(num_view);//���»�ȡ���������б�
		            					    	refreshData();//ˢ��MyAdapter
		            					    }else{
		            					    	Toast.makeText(v.getContext(),"ɾ��ʧ�ܣ�",Toast.LENGTH_SHORT).show();
		            					    }	            					    	
		            					}
		            				});
		            		builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
		            					public void onClick(DialogInterface dialog, int whichButton) {
		            					
		            					}
		            				});
		            		builder.show();	               
							return false;
						}
		            });
		          
	            holder.title.setOnClickListener(new View.OnClickListener() {				
						@Override
						public void onClick(View v) {
							// TODO �Զ����ɵķ������
							int selnum=position;						
							try {
								com.esri.android.viewer.tools.fileUtil.file filepath = file_list.get(selnum);
								File file = new File(filepath.path);
								Intent it = new Intent(Intent.ACTION_VIEW);
								if (filepath.item.contains(".jpg")) {
									it.setDataAndType(Uri.fromFile(file),
											"image/*");
								} else if (filepath.item.contains(".mp4")) {
									it.setDataAndType(Uri.fromFile(file),
											"video/*");
								} else if (filepath.item.contains(".mp3")) {
									it.setDataAndType(Uri.fromFile(file),
											"audio/*");
								} else if (filepath.item.contains(".txt")) {
									it.setDataAndType(Uri.fromFile(file),
											"text/*");
								} else {
									it.setData(Uri.fromFile(file));
								}
								startActivity(it);
							} catch (Exception e) {
								// TODO: handle exception
							} 
						}
					});
		            
		            return convertView;
		        }
		    }
		 
		
	}


	
	
	 @Override
	public boolean onOptionsItemSelected(MenuItem item) {
	  /*      switch (item.getItemId()) {
	        case R.id.menu_camera:
	        	// ѡ��ͼƬ�������� or ���
				showPicturePicker(this);
	            return true;
	        case R.id.menu_video:
		    	//¼����Ƶ
				showTakeVideo();
	        	return true;
	        case R.id.menu_voice:
	            Intent intent = new Intent(MultiMediaActivity.this, VoiceActivity.class);  //��ת��¼��ҳ��
	            Bundle bundle=new Bundle();  
                bundle.putString("voice_path", voice_path);  
                intent.putExtras(bundle); //���������ļ�д��·��voice_path
	            MultiMediaActivity.this.startActivity(intent);
	            //MultiMediaActivity.this.finish();
	        	return true;
	        case R.id.menu_sketch:
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	        }*/
		  return false;
	    }
	
    /**
     * ��ȡ��γ����Ϣ
     * @return
     */
	private String GetLocaltion()
		{
			//����LocationManager����
		    LocationManager loctionManager;
		    String contextService=Context.LOCATION_SERVICE;
		    //ͨ��ϵͳ����ȡ��LocationManager����
		    loctionManager=(LocationManager) getSystemService(contextService);
		    
		    //ʹ�ñ�׼���ϣ���ϵͳ�Զ�ѡ����õ����λ���ṩ�����ṩλ��
		    Criteria criteria = new Criteria();
		    criteria.setAccuracy(Criteria.ACCURACY_FINE);//�߾���
		    criteria.setAltitudeRequired(true);//Ҫ�󺣰�
		    criteria.setBearingRequired(true);//��Ҫ��λ
		    criteria.setCostAllowed(true);//�����л���
		    criteria.setPowerRequirement(Criteria.POWER_LOW);//�͹���
		    
		    //�ӿ��õ�λ���ṩ���У�ƥ�����ϱ�׼������ṩ��
		    String provider = loctionManager.getBestProvider(criteria, true);
		    
		    //������һ�α仯��λ��
		    Location location = loctionManager.getLastKnownLocation(provider);
		    
		    String latLongString;
			if(location!=null){
				double lat=location.getLatitude();
				double lng=location.getLongitude();
				double alt = location.getAltitude();
				double bear = location.getBearing();			
				latLongString = "Lat(γ��): "+lat+"\nLong(����): "+lng+"\nAlt(�߳�): "+alt+"\nBear(��λ): "+bear;
						
			}else{
				latLongString="û�ҵ�λ��";
			}
			return latLongString;
		    
		}
	 
	/**
	 * ��ȡ�ֻ���ǰ״̬��Ϣ����ǣ������ǣ�������ֵ��Orientation
	 */
	 private void GetOrientation()
		{
			  SensorManager sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE); 
			 	Sensor sensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		        /*TYPEע��
		        		int TYPE_ACCELEROMETER ���ٶ� 
		                int TYPE_ALL �������ͣ�NexusOneĬ��Ϊ ���ٶ� 
		                int TYPE_GYROSCOPE ��ת��(�����̫��) 
		                int TYPE_LIGHT ���߸�Ӧ
		                int TYPE_MAGNETIC_FIELD �ų� 
		                int TYPE_ORIENTATION ����ָ���룩�ͽǶ� 
		                int TYPE_PRESSUR ѹ���� 
		                int TYPE_PROXIMITY ���룿��̫�� 
		                int TYPE_TEMPERATURE �¶���
		        */
		        SensorEventListener lsn = new SensorEventListener() {
		            public void onSensorChanged(SensorEvent e) {
		                 double   x = e.values[SensorManager.AXIS_X-1];  //����ȣ�Χ�� z ��ĽǶȣ�
		                 double   y = e.values[SensorManager.AXIS_Y-1];  //�����ȣ�Χ�� x ��ĽǶȣ�
		                 double   z = e.values[SensorManager.AXIS_Z-1]; //�����ȣ�Χ�� y ��ĽǶȣ�
		                    //setTitle("x="+ (int)x +","+"y="+(int)y+","+"z="+(int)z);//��ʾΪ����
		                    
		                    //����ȣ�Χ�� z �����ת�ǣ�������ָ�豸 y ����شű�����ļнǡ����磬����豸�� y ��ָ��شű������
							//ֵΪ 0����� y ��ָ���Ϸ����ֵΪ 180�� ͬ��y ��ָ�򶫷����ֵΪ 90����ָ���������ֵΪ 270�� 
		                    
							//�����ȣ�Χ�� x �����ת�ǣ����� z �����ֵ���ֳ��� y �����ֵ������תʱ����ֵΪ���� �� z �����ֵ����
							//���� y ��ĸ�ֵ������תʱ����ֵΪ����ȡֵ��ΧΪ -180 �ȵ� 180 �ȡ� 
		                    
							//�����ȣ�Χ�� y �����ת�ǣ����� z �����ֵ���ֳ��� x �����ֵ������תʱ����ֵΪ���� �� z �����ֵ����
							//���� x ��ĸ�ֵ������תʱ����ֵΪ����ȡֵ��ΧΪ -90 �ȵ� 90 �ȡ� 
		                    
		                 String  orientStr ="\n����ȣ�Χ�� z ��ĽǶȣ�:"+x +",\n"+"�����ȣ�Χ�� x ��ĽǶȣ�"+y+",\n"+"�����ȣ�Χ�� y ��ĽǶȣ�"+z;
		                 Orientation  =  orientStr;                   
		            }
		            public void onAccuracyChanged(Sensor s, int accuracy) {
		            }
		        };
		        //ע��listener�������������Ǽ���������
		        sensorMgr.registerListener(lsn, sensor, SensorManager.SENSOR_DELAY_GAME);
		        /*
		        SENSOR_DELAY_FASTEST �����������Ȼ������
		        SENSOR_DELAY_GAME ��Ϸ��ʱ�������������һ��������͹��ˣ�����һ�����ѿ�������
		        SENSOR_DELAY_NORMAL �Ƚ�����
		        SENSOR_DELAY_UI �����ģ��������Ǻ���ݵ�����
		        */				 		
		}
   
	  /**
	  * ͼƬ��Դѡ�� ���ջ����
	  * @param context ��ǰ����Activity
	  */
	 public void showPicturePicker(Context context){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("ͼƬ��Դ");
			builder.setNegativeButton("ȡ��", null);
			builder.setItems(new String[]{"����","���"}, new DialogInterface.OnClickListener() {	
				public void onClick(DialogInterface dialog, int which) {
					Uri imageUri;       
					String fileName ="tmp.jpg" ;//��ʱ�洢����Ƭ�ļ�
					imageUri = Uri.fromFile(new File(picture_path,fileName));
					switch (which) {
						case TAKE_PICTURE:			
							Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
							openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//д��SD��,д����ʱ�ļ�
							startActivityForResult(openCameraIntent, TAKE_PICTURE);
							break;						
						case CHOOSE_PICTURE:
							Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
							openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
							startActivityForResult(openAlbumIntent, CHOOSE_PICTURE);
							break;
						default:
							break;
					}
				}
			});
			builder.create().show();
		}
	 
	 /**
	  * ¼����Ƶ
	  */
	 private void showTakeVideo() {
			Uri videoUri;
			String fileName = com.esri.android.viewer.tools.sysTools.getTimeNow()+".mp4" ;
			videoUri = Uri.fromFile(new File(video_path,fileName));
			Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);//д��SD��
		    startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
		}
	 
	 /**
	  * ���գ�ѡ��ͼƬ��������Ƶ�������¼�
	  */
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			//TODO ������ɺ�ִ��
			if (resultCode == RESULT_OK) {
				switch (requestCode) {
				case TAKE_PICTURE:		
					Toast.makeText(MultiMediaActivity.this,"��Ƭ����ɹ���", Toast.LENGTH_LONG).show();
					//��ȡ��ǰ������Ϣ
					String locStr =GetLocaltion();
					//��ȡ��ǰ�ⷽλԪ����Ϣ
					String Ori = Orientation;
					//д�뵽ϵͳ��ʱ�ļ���
					String name  = com.esri.android.viewer.tools.sysTools.getTimeNow();
					com.esri.android.viewer.tools.fileTools.saveTxt(picture_path+"/"+name+".txt", locStr+Ori);
					com.esri.android.viewer.tools.fileTools.copyFile(picture_path+"/" +"tmp.jpg", picture_path+"/"+name+".jpg");
					com.esri.android.viewer.tools.fileTools.deleteFiles(picture_path+"/" +"tmp.jpg");//ɾ����ʱͼƬ
					this.recreate();
					break;

				case CHOOSE_PICTURE:
					//��Ƭ��ԭʼ��Դ��ַ
					Uri originalUri = data.getData(); 
					String[] proj = { MediaStore.Images.Media.DATA };
					Cursor actualimagecursor = managedQuery(originalUri,proj,null,null,null);
					int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					actualimagecursor.moveToFirst();
					String img_path = actualimagecursor.getString(actual_image_column_index);				
					//��ȡ��ǰϵͳʱ��
					String str = com.esri.android.viewer.tools.sysTools.getTimeNow();
					String fileName = (str +".jpg").toString();		
					//��ȡ�����Դ���Ƶ�ָ��Ҫ���ļ���
					com.esri.android.viewer.tools.fileTools.copyFile(img_path,picture_path +"/"+fileName);
					
					Toast.makeText(MultiMediaActivity.this,"ͼƬ����ɹ���", Toast.LENGTH_LONG).show();
					this.recreate();
					break;
				
				case ACTION_TAKE_VIDEO:
					Toast.makeText(MultiMediaActivity.this,"��Ƶ����ɹ���", Toast.LENGTH_LONG).show();
					this.recreate();
					break;				
				}			
			}
		}
	
	 
	
}

package com.esri.android.viewer.module;

import java.util.Date;

import com.esri.android.viewer.R;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

public class DropLayout extends LinearLayout {
	
	private ScrollView sc;
	private LayoutInflater inflater;
	private LinearLayout header;
	private ImageView arrowImg;
	private ProgressBar headProgress;
	private TextView lastUpdateTxt;
	private TextView tipsTxt;
	private RotateAnimation tipsAnimation;
	private RotateAnimation reverseAnimation;
	private int headerHeight;	//ͷ�߶�
	private int lastHeaderPadding; //���һ�ε���Move Header��Padding
	private boolean isBack; //��Release ת�� pull
	private int headerState = DONE;
	private RefreshCallBack callBack;
	public  LinearLayout subLayout;//ʵ��View�洢��
	
	static final private int RELEASE_To_REFRESH = 0;
	static final private int PULL_To_REFRESH = 1;
	static final private int REFRESHING = 2;
	static final private int DONE = 3;
	
	
	public DropLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
	
	private void init(Context context) {
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		sc = new ScrollView(context);
		sc.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//����ScrollView ֻ������һ��ChildView��������LinearLayout��������
		subLayout = new LinearLayout(context);
		subLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		subLayout.setOrientation(VERTICAL);
		sc.addView(subLayout);
		header = (LinearLayout) inflater.inflate(R.layout.drag_drop_header, null);
		measureView(header);
		headerHeight = header.getMeasuredHeight();
		lastHeaderPadding = (-1*headerHeight); //���һ�ε���Move Header��Padding
		header.setPadding(0, lastHeaderPadding, 0, 0);
		header.invalidate();
		this.addView(header,0);
		this.addView(sc,1);
		
		headProgress = (ProgressBar) findViewById(R.id.head_progressBar);
		arrowImg = (ImageView) findViewById(R.id.head_arrowImageView);
		arrowImg.setMinimumHeight(50);
		arrowImg.setMinimumWidth(50);
		tipsTxt = (TextView) findViewById(R.id.head_tipsTextView);
		lastUpdateTxt = (TextView) findViewById(R.id.head_lastUpdatedTextView);
		//��ͷת������
		tipsAnimation = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		tipsAnimation.setInterpolator(new LinearInterpolator());
		tipsAnimation.setDuration(200);		//��������ʱ��
		tipsAnimation.setFillAfter(true);	//���������󱣳ֶ���
		//��ͷ��ת����
		reverseAnimation = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(200);
		reverseAnimation.setFillAfter(true);
		//Ϊscrollview���¼�
		sc.setOnTouchListener(new OnTouchListener() {
			private int beginY=100;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				switch (event.getAction()) {
				case MotionEvent.ACTION_MOVE:
					//sc.getScrollY == 0  scrollview ������ͷ�� 
					//lastHeaderPadding > (-1*headerHeight) ��ʾheader��û��ȫ��������ʱ
					//headerState != REFRESHING ������ˢ��ʱ
					if((sc.getScrollY() == 0 || lastHeaderPadding > (-1*headerHeight)) && headerState != REFRESHING) {
						//�õ�������Y�����
						int interval = (int) (event.getY() - beginY);
						//�����»������������ϻ���
						if (interval > 100) {
							interval = interval/4;//�»�����
							lastHeaderPadding = interval + (-1*headerHeight);
							header.setPadding(0, lastHeaderPadding, 0, 0);
							if(lastHeaderPadding > 0) {
								//txView.setText("��Ҫˢ�¿�");
								headerState = RELEASE_To_REFRESH;
								//�Ƿ��Ѿ�������UI
								if(! isBack) {
									isBack = true;  //����Release״̬��������ػ�������pull����������
									changeHeaderViewByState();
								}
							} else {
								headerState = PULL_To_REFRESH;
								changeHeaderViewByState();
								//txView.setText("��������Ŷ");
								//sc.scrollTo(0, headerPadding);
							}
						}
					}
					break;
				case MotionEvent.ACTION_DOWN:
					//�����»�������ʵ�ʻ�������Ĳ���ֵ��
					beginY = (int) ((int) event.getY() + sc.getScrollY()*1.5);
					break;
				case MotionEvent.ACTION_UP:
					if (headerState != REFRESHING) {
						switch (headerState) {
						case DONE:
							//ʲôҲ����
							break;
						case PULL_To_REFRESH:
							headerState = DONE;
							lastHeaderPadding = -1*headerHeight;
							header.setPadding(0, lastHeaderPadding, 0, 0);
							changeHeaderViewByState();
							break;
						case RELEASE_To_REFRESH:
							isBack = false; //׼����ʼˢ�£���ʱ���������ػ���
							headerState = REFRESHING;
							changeHeaderViewByState();
							onRefresh();
							break;
						default:
							break;
						}
					}
					break;
				}
				//���Header����ȫ�����ص�����ScrollView�������������¼���������Ļ�������¼�
				if(lastHeaderPadding > (-1*headerHeight) && headerState != REFRESHING) {
					return true;
				} else {
					return false;
				}
			}
		});
	}
	
	@Override
	public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
		//��ȡXML�е�Ĭ�ϸ� -1 �Լ���ӵ�Ϊ0 �� 1
		if(index == -1) {
			subLayout.addView(child, params);
			return ;
		}
		super.addView(child, index, params);
	}
	
	public void setRefreshCallBack(RefreshCallBack callBack) {
		this.callBack = callBack;
	}
	
	private void changeHeaderViewByState() {
		switch (headerState) {
		case PULL_To_REFRESH:
			// ����RELEASE_To_REFRESH״̬ת������
			if (isBack) {
				isBack = false;
				arrowImg.startAnimation(reverseAnimation);
				tipsTxt.setText("����ˢ��");
			}
			tipsTxt.setText("����ˢ��");
			break;
		case RELEASE_To_REFRESH:
			arrowImg.setVisibility(View.VISIBLE);
			headProgress.setVisibility(View.GONE);
			tipsTxt.setVisibility(View.VISIBLE);
			lastUpdateTxt.setVisibility(View.VISIBLE);
			arrowImg.clearAnimation();
			arrowImg.startAnimation(tipsAnimation);
			tipsTxt.setText("�ɿ�ˢ��");
			break;
		case REFRESHING:
			lastHeaderPadding = 0;
			header.setPadding(0, lastHeaderPadding, 0, 0);
			header.invalidate();
			headProgress.setVisibility(View.VISIBLE);
			arrowImg.clearAnimation();
			arrowImg.setVisibility(View.INVISIBLE);
			tipsTxt.setText("����ˢ��...");
			lastUpdateTxt.setVisibility(View.VISIBLE);
			break;
		case DONE:
			lastHeaderPadding = -1 * headerHeight;
			header.setPadding(0, lastHeaderPadding, 0, 0);
			header.invalidate();
			headProgress.setVisibility(View.GONE);
			arrowImg.clearAnimation();
			arrowImg.setVisibility(View.VISIBLE);
			tipsTxt.setText("����ˢ��");
			lastUpdateTxt.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
	}
	
	private void onRefresh() {
		new RefreshAsyncTask().execute();
	}
		
 	private class RefreshAsyncTask extends AsyncTask<Void, Void, Void> {
		protected Void doInBackground(Void... params) {
			if(callBack != null) {
				callBack.doInBackground();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			onRefreshComplete();
			if(callBack != null) {
				callBack.complete();
			}
		}
	}
	
	/**
	 * ˢ�¼����ӿ�
	 */
	public interface RefreshCallBack {
		public void doInBackground();
		public void complete();
	}
	
	@SuppressWarnings("deprecation")
	public void onRefreshComplete() {
		headerState = DONE;
		lastUpdateTxt.setText("�������:" + new Date().toLocaleString());
		changeHeaderViewByState();
	}
	//����OnCreate�����ò���header�ĸ߶�������Ҫ�ֶ�����
	private void measureView(View childView) {
		android.view.ViewGroup.LayoutParams p = childView.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int height = p.height;
		int childHeightSpec;
		if (height > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED);
		}
		childView.measure(childWidthSpec, childHeightSpec);
	}
}

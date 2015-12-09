package com.esri.android.viewer.draft;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class PaintView extends View {
	private OnPaintListener listener;
	private Bitmap mBitmap;
	private Canvas mCanvas;
	private Path mPath;
	private Paint mBitmapPaint;
	private int penWidth;
	private int penColor;
	private int penStyle;
	private Paint mPaint;
	private int width;
	private int height;
	private Context context;
	
	public PaintView(Context c,AttributeSet attrs) {
		super(c,attrs);
		penWidth=6;
		penColor=0xFFFF0000;
		penStyle=0;
		mPaint = new Paint();//����������Ⱦ����   
		mPaint.setAntiAlias(true);//���ÿ���ݣ��û滭�Ƚ�ƽ��   
		mPaint.setDither(true);//���õ�ɫ   
		mPaint.setColor(penColor);//���û��ʵ���ɫ   
		mPaint.setStyle(Paint.Style.STROKE);//���ʵ����������֣�1.FILL 2.FILL_AND_STROKE 3.STROKE ��   
		mPaint.setStrokeJoin(Paint.Join.ROUND);//Ĭ��������MITER��1.BEVEL 2.MITER 3.ROUND ��   
		mPaint.setStrokeCap(Paint.Cap.ROUND);//Ĭ��������BUTT��1.BUTT 2.ROUND 3.SQUARE ��   
		mPaint.setStrokeWidth(penWidth);//������ߵĿ�ȣ�������õ�ֵΪ0��ô����һ����ϸ����   
		width=1600;//����Ĭ�ϻ�����
		height=1600;//����Ĭ�ϻ��峤��
		mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);//���̶���bitmap����Ƕ�뵽canvas������   
		mPath = new Path();//��������·��   
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		context=c;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(penColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(penWidth);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(0xFFAAAAAA);
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		canvas.drawPath(mPath, mPaint);
	}
	
	private float mX, mY;
	private static final float TOUCH_TOLERANCE = 4;
	
	// ��ʼ��ͼ
	private void touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
		listener.paint(x, y,MotionEvent.ACTION_DOWN);
	}// �ƶ�����
	
	private void touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
			listener.paint(x, y,MotionEvent.ACTION_MOVE);
		}
	}
	
	// ������Ļ
	private void touch_up() {
		mPath.lineTo(mX, mY);
		// �����ݻ�����Ļ��
		mCanvas.drawPath(mPath, mPaint);
		// ���軭�ʷ�ֹ�ظ����
		mPath.reset();
		listener.paint(mX, mY, MotionEvent.ACTION_UP);
		//Toast.makeText(context, myPathStore.tempath.lenth, Toast.LENGTH_LONG).show();
	}
	

	// ��ʼ��ͼ
	private void pre_touch_start(float x, float y) {
		mPath.reset();
		mPath.moveTo(x, y);
		mX = x;
		mY = y;
	}// �ƶ�����
	
	private void pre_touch_move(float x, float y) {
		float dx = Math.abs(x - mX);
		float dy = Math.abs(y - mY);
		if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
		}
	}
	
	// ������Ļ
	private void pre_touch_up() {
		mPath.lineTo(mX, mY);
		// �����ݻ�����Ļ��
		mCanvas.drawPath(mPath, mPaint);
		// ���軭�ʷ�ֹ�ظ����
		mPath.reset();
		//Toast.makeText(context, myPathStore.tempath.lenth, Toast.LENGTH_LONG).show();
	}
	
	// ����Ļ�Ĵ���ʱ�����Ӧ
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		
		switch (event.getAction()) {
			// �����¼���Ӧ
			case MotionEvent.ACTION_DOWN:
			touch_start(x, y);
			invalidate();
			break;
			// �ƶ��¼���Ӧ
			case MotionEvent.ACTION_MOVE:
			touch_move(x, y);
			invalidate();
			break;
			// �ɿ��¼���Ӧ
			case MotionEvent.ACTION_UP:
			touch_up();
			invalidate();
			break;
		}
		return true;
	}

	public void setColor(int color)
	{
		mPaint.setColor(color);
	}
	public void setPenStyle(int style)
	{
		
	}
	public void setPenWidth(int width)
	{
		mPaint.setStrokeWidth(width);
	}
	public int getPenWidth()
	{
		return (int)mPaint.getStrokeWidth();
	}
	public void clean()
	{
		mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		mCanvas.setBitmap(mBitmap);
		try {
			Message msg=new Message();
			msg.obj=PaintView.this;
			handler.sendMessage(msg);
			Thread.sleep(0);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void printScreen()
	{
		mCanvas.save();
	}
	public interface OnPaintListener{
		void paint(float x,float y,int action);
	}
	public void setOnPaintListener(OnPaintListener listener){
		this.listener=listener;
	}
	public void preview(ArrayList<PathStore.node> temPath)
	{
		
		PreviewThread previewThread=new PreviewThread(this, temPath);
		Thread thread=new Thread(previewThread);
		thread.start();
	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			((View)msg.obj).invalidate();
			super.handleMessage(msg);
		}
		
	};
	class PreviewThread implements Runnable{
		private long time;
		private ArrayList<PathStore.node> temPath;
		private View view;
		public PreviewThread(View view, ArrayList<PathStore.node> temPath)
		{
			this.view=view;
			this.temPath=temPath;
		}
		public void run() {
			// TODO Auto-generated method stub
			time=0;
			clean();
			for(int i=0;i<temPath.size();i++)
			{
				PathStore.node tempnode=temPath.get(i);
				float x = tempnode.x;
				float y = tempnode.y;
				if(i<temPath.size()-1)
				{
					time=temPath.get(i+1).time-tempnode.time;
				}
				switch (tempnode.action) {
					// �����¼���Ӧ
					case MotionEvent.ACTION_DOWN:
					pre_touch_start(x+10, y);
					break;
					// �ƶ��¼���Ӧ
					case MotionEvent.ACTION_MOVE:
					pre_touch_move(x+10, y);
					break;
					// �ɿ��¼���Ӧ
					case MotionEvent.ACTION_UP:
					pre_touch_up();
					break;
				}
				Log.v("mylog", i+"");
				
				try {
					Message msg=new Message();
					msg.obj=view;
					handler.sendMessage(msg);
					Thread.sleep(time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
}

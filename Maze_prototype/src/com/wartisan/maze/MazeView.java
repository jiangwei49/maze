package com.wartisan.maze;

import java.util.ArrayList;

import com.wartisan.maze.prepareCells.Cell;
import com.wartisan.maze.prepareCells.Prototype;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class MazeView extends View implements SurfaceHolder.Callback{

	private Context myContext;
	private SurfaceHolder mySurfaceHolder;
	private Bitmap backgroundImg;
	private int backgroundOrigW;
	private int backgroundOrigH;
	private float scaleW;
	private float scaleH;
	private float drawScaleW;
	private float drawScaleH;
	private Bitmap mask;
	private Bitmap mole;
	private int screenW = 1;
	private int screenH = 1;
	private boolean running = false;
	private boolean onTitle = true;
	private int myMode;
	private int activeMole = 0;
	private boolean moleRising = true;
	private boolean moleSinking = false;
	private int moleRate = 5;
	private int fingerX, fingerY;
	private static SoundPool sounds;
	private static int whackSound;
	private static int missSound;
	private boolean moleJustHit = false;
	private Bitmap whack;
	private boolean whacking = false;
	private int molesWhacked = 0;
	private int molesMissed = 0;
	private Paint blackPaint;
	public boolean soundOn = true;
	private boolean gameOver = false;
	private Bitmap gameOverDialog;

	public MazeView(Context context, AttributeSet attrs) {  
		super(context, attrs);  
	}  
	
	public void setCanvasSize(int w, int h) {
		screenW = w;
		screenH = h;
	}

	@Override  
	protected void onDraw(Canvas canvas) {  
		super.onDraw(canvas);

		int dimensionX = 13, dimensionY = 13;
		Prototype x = new Prototype(dimensionX,dimensionY);
		
		int cellWidth = (int) (screenW-163)/dimensionX;
		int cellHeight = (int) (screenH-40)/dimensionY;

		System.out.println("cellWidth=" + cellWidth);
		System.out.println("cellHeight=" + cellHeight);


		// 32x32 15 mins, 13x13 17 secs, 19x19 1.4 mins 
		ArrayList<Cell> cells = x.getConnections();

		System.out.println("Found " + cells.size()/2 + " connections.");
		/* 
		 * 方法 说明 drawRect 绘制矩形 drawCircle 绘制圆形 drawOval 绘制椭圆 drawPath 绘制任意多边形 
		 * drawLine 绘制直线 drawPoin 绘制点 
		 */  
		 // 创建画笔  
		Paint p = new Paint();
		p.setAntiAlias(true);
		p.setColor(Color.BLACK);
		canvas.drawColor(Color.CYAN);
		p.setStrokeWidth((float) 3.0);
		canvas.drawLine(0, 0, 0, dimensionY*cellHeight, p);
		canvas.drawLine(dimensionX*cellWidth, 0, dimensionX*cellWidth, dimensionY*cellHeight, p);
		for (int i=1; i< (dimensionX); i++) {
			canvas.drawLine(i*cellWidth, 0, i*cellWidth, dimensionY*cellHeight, p);
		}
		canvas.drawLine(0, 0, dimensionX*cellHeight, 0, p);
		canvas.drawLine(0, dimensionY*cellHeight, dimensionX*cellWidth, dimensionY*cellHeight, p);
		for (int i=1; i< (dimensionY); i++) {
			canvas.drawLine(0, i*cellHeight, dimensionX*cellWidth, i*cellHeight, p);
		}

		p.setStrokeWidth((float) 4.0);
		p.setColor(Color.CYAN);
		canvas.drawLine(0, 0, cellWidth-3, 0, p);
		canvas.drawLine(0, 0, 0, cellHeight-3, p);
		canvas.drawLine((dimensionX-1)*cellWidth+3, dimensionY*cellHeight, dimensionX*cellWidth, dimensionY*cellHeight, p);
		canvas.drawLine(dimensionX*cellWidth, (dimensionY-1)*cellHeight+3, dimensionX*cellWidth, dimensionY*cellHeight, p);

		// remove all the walls
		for (int i=0; i<cells.size(); i++) {
			Cell c1 = cells.get(i);
			Cell c2 = cells.get(i+1);
			i++;
			int x1 = c1.getX();
			int y1 = c1.getY();
			int x2 = c2.getX();
			int y2 = c2.getY();

			int xx1 = 0, yy1 = 0, xx2 = 0, yy2 = 0;
			
			if (x1==x2) { //vertical
				xx1 = x1 * cellWidth;
				xx2 = xx1 + cellWidth;
				yy1 = y1>y2 ? y1*cellHeight : y2*cellHeight;
				yy2 = yy1;
			}
			
			if (y1==y2) { //horizontal
				xx1 = x1>x2 ? x1* cellWidth : x2*cellWidth;
				xx2 = xx1;
				yy1 = y1 * cellHeight;
				yy2 = yy1 + cellHeight;
			}
			canvas.drawLine(xx1,yy1,xx2,yy2, p);

		}

		// 画表格
		// 去掉房间的墙

		//        canvas.drawText("画圆：", 10, 20, p);// 画文本  
		//        canvas.drawCircle(60, 20, 10, p);// 小圆  
		//        p.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白了  
		//        canvas.drawCircle(120, 20, 20, p);// 大圆  

		//        canvas.drawText("画线及弧线：", 10, 60, p);  
		//        p.setColor(Color.GREEN);// 设置绿色  
		//        canvas.drawLine(60, 40, 100, 40, p);// 画线  
		//        canvas.drawLine(110, 40, 190, 80, p);// 斜线  
		//        //画笑脸弧线  
		//        p.setStyle(Paint.Style.STROKE);//设置空心  
		//        RectF oval1=new RectF(150,20,180,40);  
		//        canvas.drawArc(oval1, 180, 180, false, p);//小弧形  
		//        oval1.set(190, 20, 220, 40);  
		//        canvas.drawArc(oval1, 180, 180, false, p);//小弧形  
		//        oval1.set(160, 30, 210, 60);  
		//        canvas.drawArc(oval1, 0, 180, false, p);//小弧形  
		//  
		//        canvas.drawText("画矩形：", 10, 80, p);  
		//        p.setColor(Color.GRAY);// 设置灰色  
		//        p.setStyle(Paint.Style.FILL);//设置填满  
		//        canvas.drawRect(60, 60, 80, 80, p);// 正方形  
		//        canvas.drawRect(60, 90, 160, 100, p);// 长方形  
		//  
		//        canvas.drawText("画扇形和椭圆:", 10, 120, p);  
		//        /* 设置渐变色 这个正方形的颜色是改变的 */  
		//        Shader mShader = new LinearGradient(0, 0, 100, 100,  
		//                new int[] { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW,  
		//                        Color.LTGRAY }, null, Shader.TileMode.REPEAT); // 一个材质,打造出一个线性梯度沿著一条线。  
		//        p.setShader(mShader);  
		//        // p.setColor(Color.BLUE);  
		//        RectF oval2 = new RectF(60, 100, 200, 240);// 设置个新的长方形，扫描测量  
		//        canvas.drawArc(oval2, 200, 130, true, p);  
		//        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线  
		//        //画椭圆，把oval改一下  
		//        oval2.set(210,100,250,130);  
		//        canvas.drawOval(oval2, p);  
		//  
		//        canvas.drawText("画三角形：", 10, 200, p);  
		//        // 绘制这个三角形,你可以绘制任意多边形  
		//        Path path = new Path();  
		//        path.moveTo(80, 200);// 此点为多边形的起点  
		//        path.lineTo(120, 250);  
		//        path.lineTo(80, 250);  
		//        path.close(); // 使这些点构成封闭的多边形  
		//        canvas.drawPath(path, p);  
		//  
		//        // 你可以绘制很多任意多边形，比如下面画六连形  
		//        p.reset();//重置  
		//        p.setColor(Color.LTGRAY);  
		//        p.setStyle(Paint.Style.STROKE);//设置空心  
		//        Path path1=new Path();  
		//        path1.moveTo(180, 200);  
		//        path1.lineTo(200, 200);  
		//        path1.lineTo(210, 210);  
		//        path1.lineTo(200, 220);  
		//        path1.lineTo(180, 220);  
		//        path1.lineTo(170, 210);  
		//        path1.close();//封闭  
		//        canvas.drawPath(path1, p);  
		//        /* 
		//         * Path类封装复合(多轮廓几何图形的路径 
		//         * 由直线段*、二次曲线,和三次方曲线，也可画以油画。drawPath(路径、油漆),要么已填充的或抚摸 
		//         * (基于油漆的风格),或者可以用于剪断或画画的文本在路径。 
		//         */  
		//          
		//        //画圆角矩形  
		//        p.setStyle(Paint.Style.FILL);//充满  
		//        p.setColor(Color.LTGRAY);  
		//        p.setAntiAlias(true);// 设置画笔的锯齿效果  
		//        canvas.drawText("画圆角矩形:", 10, 260, p);  
		//        RectF oval3 = new RectF(80, 260, 200, 300);// 设置个新的长方形  
		//        canvas.drawRoundRect(oval3, 20, 15, p);//第二个参数是x半径，第三个参数是y半径  
		//          
		//        //画贝塞尔曲线  
		//        canvas.drawText("画贝塞尔曲线:", 10, 310, p);  
		//        p.reset();  
		//        p.setStyle(Paint.Style.STROKE);  
		//        p.setColor(Color.GREEN);  
		//        Path path2=new Path();  
		//        path2.moveTo(100, 320);//设置Path的起点  
		//        path2.quadTo(150, 310, 170, 400); //设置贝塞尔曲线的控制点坐标和终点坐标  
		//        canvas.drawPath(path2, p);//画出贝塞尔曲线  
		//          
		//        //画点  
		//        p.setStyle(Paint.Style.FILL);  
		//        canvas.drawText("画点：", 10, 390, p);  
		//        canvas.drawPoint(60, 390, p);//画一个点  
		//        canvas.drawPoints(new float[]{60,400,65,400,70,400}, p);//画多个点  
		//          
		//        //画图片，就是贴图  
		//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);  
		//        canvas.drawBitmap(bitmap, 250,360, p);  
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		screenW = width;
		screenH = height;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}  














}

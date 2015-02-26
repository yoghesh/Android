package com.example.flareon;

import com.example.flareon.SliderViewPage.TabColorizer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SliderTab extends LinearLayout {
	
	private int DEFAULT_THEME_FOREGROUND_COLOR;
	private int[] DEFAULT_INDICATOR_COLOLRS = { 0x19A319, 0x0000FC };
	private int[] DEFAULT_DIVIDER_COLORs = { 0xC5C5C5 };
	private static int BOTTOM_BORDER_THICKNESS = 2;
	private static int INDICATOR_THICKNESS = 8;
	private static int DIVIDER_THICKNESS = 1;
	private static float DIVIDER_HEIGHT = 0.5f;
	
	private simpleTabColorizer m_DefaultColorizer;
	private float m_IndicatorThickness;
	private Paint m_IndicatorPaint;
	private float m_DividerHeight;
	private Paint m_DividerPaint;
	private float m_BorderThickness;
	private Paint m_BorderPaint;
	
	private int m_SelectedPosition;
	private float m_SelectedOffset;
	
	private SliderViewPage.TabColorizer m_CustomTabColorizer;
	
	public SliderTab(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		setWillNotDraw(false);
		TypedValue themeVal = new TypedValue();
		((Activity)context).getTheme().resolveAttribute(android.R.attr.colorForeground, themeVal, false);
		DEFAULT_THEME_FOREGROUND_COLOR = themeVal.data;
		
		m_DefaultColorizer = new simpleTabColorizer();
		m_DefaultColorizer.setIndicatorColors(DEFAULT_INDICATOR_COLOLRS);
		m_DefaultColorizer.setDividerColor(DEFAULT_DIVIDER_COLORs);
		
		DisplayMetrics dispMat = new DisplayMetrics();
		Display currentDisplay =  ((Activity)context).getWindowManager().getDefaultDisplay();
		
		currentDisplay.getMetrics(dispMat);
		float density = dispMat.density;
		
		m_IndicatorThickness = INDICATOR_THICKNESS*density;
		m_IndicatorPaint = new Paint();
		
		m_DividerHeight = DIVIDER_HEIGHT;
		m_DividerPaint = new Paint();
		m_DividerPaint.setStrokeWidth(DIVIDER_THICKNESS*density);
		
		m_BorderThickness = BOTTOM_BORDER_THICKNESS*density;
		m_BorderPaint = new Paint();
		m_BorderPaint.setColor(0xC5C5C5);
		this.setVisibility(View.VISIBLE);
		this.setOrientation(HORIZONTAL);
	}
	
	// dummy constructor to transfer it to another number
	public SliderTab(Context context, AttributeSet attrs)
	{this(context,attrs,0);}
	
	public SliderTab(Context context)
	{this(context,null,0);}

	
	public class simpleTabColorizer
	{
		private int[] m_indicatorColors;
		private int[] m_dividerColors;
		
		public int getIndicatorColor(int position)
		{
			return m_indicatorColors[position % m_indicatorColors.length];
		}
		
		public int getDividerColor(int position)
		{
			return m_dividerColors[position % m_dividerColors.length];
		}
		
		public void setIndicatorColors(int[] value)
		{
			m_indicatorColors = value;
		}
		
		public void setDividerColor(int[] value)
		{
			m_dividerColors = value;
		}
	}
	
	public void setTabColorizer(SliderViewPage.TabColorizer TabColor)
	{
		if (TabColor != null)
		{
			m_CustomTabColorizer = TabColor;
			invalidate();
		}
	}
	
	public void setIndicatorColor(int[] colors)
	{
		m_CustomTabColorizer = null;
		m_DefaultColorizer.setIndicatorColors(colors);
		invalidate();
	}
	
	public void setDividerColor(int[] colors)
	{
		m_CustomTabColorizer = null;
		m_DefaultColorizer.setDividerColor(colors);
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		int height = getHeight(); 
		int color;
		int tabCount = getChildCount();

		SliderViewPage.TabColorizer tabColorizer = (TabColorizer) (m_CustomTabColorizer != null ?m_CustomTabColorizer:m_DefaultColorizer);
		
		if(tabCount > 0)
		{
			View selectedView = getChildAt(m_SelectedPosition);
			int left = selectedView.getLeft();
			int right = selectedView.getRight();
			color = tabColorizer.getIndicatorColor(m_SelectedPosition);
			
			if (m_SelectedOffset > 0f && m_SelectedOffset < 1f)
			{
				View nextSelected = getChildAt(m_SelectedPosition+1);
				color = blendColor(tabColorizer.getIndicatorColor(m_SelectedPosition), tabColorizer.getIndicatorColor(m_SelectedPosition+1), m_SelectedOffset);
				left = (int)(left + m_SelectedOffset*(nextSelected.getLeft() - left));
				right = (int)(right + m_SelectedOffset*(nextSelected.getRight() - right)); 
			}
			m_IndicatorPaint.setColor(color);
			canvas.drawRect(selectedView.getLeft(), selectedView.getHeight()-m_IndicatorThickness, selectedView.getRight(), selectedView.getHeight(), m_IndicatorPaint);
			
			int dividerHeight = (int)(Math.min(Math.max(0f, m_DividerHeight), 1f)*height);
			for(int i = 0; i < getChildCount(); i++)
			{
				View child = getChildAt(i);
				m_DividerPaint.setColor(tabColorizer.getDividerColor(m_SelectedPosition));
				canvas.drawLine(child.getRight(), height - dividerHeight, child.getRight(), height, m_DividerPaint);
			}
			
			canvas.drawRect(getLeft(), getHeight() - m_BorderThickness, getRight(), getHeight(), m_BorderPaint);
		}
	}
	
	public int blendColor(int color1,int color2, float offset)
	{
		float inverOffset = 1f - offset;
		float r = Color.red(color1)*offset + Color.red(color2)*inverOffset;
		float g = Color.green(color1)*offset+Color.green(color2)*inverOffset;
		float b = Color.blue(color1)*offset+Color.blue(color2)*inverOffset;
		
		return Color.rgb((int)r, (int)g, (int)b);
	}
	
	public void onViewerPageChanged (int position, float offset)
	{
		m_SelectedPosition = position;
		m_SelectedOffset = offset;
		invalidate();
	}
	
}

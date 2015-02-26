package com.example.flareon;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Path.FillType;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SearchViewCompat.OnCloseListenerCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

public class SliderViewPage extends HorizontalScrollView {

	private static int TITLE_OFFSET = 24;
	private static int TAB_VIEW_PADDING = 16;
    private static int TAB_VIEW_TEXT_SIZE = 12;
    
	private int m_TitleOffset;
	private SliderTab m_TabStrip;
	private ViewPager m_Viewpager;
	private ViewPager.OnPageChangeListener m_Listener;
	private int m_ScrollState;
	
	public interface TabColorizer
	{
		public int getIndicatorColor(int position);
		public int getDividerColor(int position);
		
	}
	
	public SliderViewPage(Context context)
	{this(context,null);}
	

	public SliderViewPage (Context context, AttributeSet attrs)
	{this(context,attrs,0);}
	
	public SliderViewPage(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
		setWillNotDraw(false);
		// TODO Auto-generated constructor stub
		setHorizontalScrollBarEnabled(false);
		setFillViewport(true);
		
		DisplayMetrics dispMat = new DisplayMetrics();
		Display currentDisplay =  ((Activity)context).getWindowManager().getDefaultDisplay();
		
		currentDisplay.getMetrics(dispMat);
		m_TitleOffset = (int)(TITLE_OFFSET*dispMat.density);
		
		this.setBackgroundColor(0xE5E5E5);
		m_TabStrip = new SliderTab(context);
		this.addView(m_TabStrip, LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	}
	
	public void setTabColorizer(TabColorizer tabColorizer)
	{
		m_TabStrip.setTabColorizer(tabColorizer);
	}
	
	public void setIndicatorColor(int[] colors)
	{
		m_TabStrip.setIndicatorColor(colors);
	}
	
	public void setDividerColor(int[] colors)
	{
		m_TabStrip.setDividerColor(colors);
	}
	
	public void setViewPager(ViewPager view)
	{
		removeAllViews();
		m_Viewpager = view;
		if (view != null)
		{
			view.setOnPageChangeListener(new pageListener());
		}
		populatTabStrip();
	}
	
	public void setCustomListener(OnPageChangeListener listener)
	{
		m_Listener = listener;
	}
	
	public class pageListener implements ViewPager.OnPageChangeListener
	{

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub
			m_ScrollState = state;
			if (m_Listener != null)
			{
				m_Listener.onPageSelected(state);
			}
		}

		@Override
		public void onPageScrolled(int position, float offset, int offsetPixels) {
			// TODO Auto-generated method stub
			int tabCount = m_TabStrip.getChildCount();
			if (tabCount == 0 || position < 0 || position >= tabCount)
			{return;}
			m_TabStrip.onViewerPageChanged(position, offset);
			View selectedTab = m_TabStrip.getChildAt(position);
			int extraOffset = selectedTab!=null?(int)(selectedTab.getWidth()*offset):0;
			scrollTo(position,extraOffset);
			if (m_Listener != null)
			{m_Listener.onPageScrolled(position, offset, offsetPixels);}
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			if (m_ScrollState == ViewPager.SCROLL_STATE_IDLE)
			{
				m_TabStrip.onViewerPageChanged(position, 0);
				scrollTo(position, 0);
			}
		}
		
	}
	
	public void scrollTo(int position, int offset)
	{
		int tabCount = m_TabStrip.getChildCount();
		if(tabCount == 0 || position < 0 || position >=0)
		{return;}
		
		View selectedTab = m_TabStrip.getChildAt(position);
		int scrollAmount = selectedTab.getLeft()+offset;
		if (tabCount >0 || offset >0)
		{
			scrollAmount = scrollAmount - m_TitleOffset;
		}
		this.scrollTo(position, scrollAmount);
		
		
	}
	
	public void populatTabStrip()
	{
		PagerAdapter myAdapter = m_Viewpager.getAdapter();
		for(int i=0; i< myAdapter.getCount();i++)
		{
			TextView tabView = createTextView(getContext());
			tabView.setText((((SlidingTabFragment.sampleAdapter)myAdapter).datas.get(i).toCharArray()),0,((SlidingTabFragment.sampleAdapter)myAdapter).datas.get(i).toCharArray().length);
			tabView.setTextColor(android.graphics.Color.BLACK);
			tabView.setTag(i);
			tabView.setOnClickListener(new clickListen());
			m_TabStrip.addView(tabView);
		}
	}
	
	public class clickListen implements View.OnClickListener
	{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			TextView myTView = (TextView)v;
			Integer position = (Integer)(myTView.getTag());
			m_Viewpager.setCurrentItem(position);
		}
		
	}
	
	public TextView createTextView(Context context)
	{
		TextView myView = new TextView(context);
		myView.setGravity(android.view.Gravity.CENTER);
		myView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE);
		myView.setTypeface(Typeface.DEFAULT_BOLD);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
		{
			TypedValue typVal = new TypedValue();
			context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typVal, false);
			myView.setBackgroundResource(typVal.resourceId);
		}
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		{
			myView.setAllCaps(true);
		}
		
		DisplayMetrics dispMat = new DisplayMetrics();
		Display currentDisplay =  ((Activity)context).getWindowManager().getDefaultDisplay();
		
		currentDisplay.getMetrics(dispMat);
		int padding = (int)(dispMat.density*TAB_VIEW_PADDING);
		
		myView.setPadding(padding, padding, padding, padding);
		return myView;
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		if(m_Viewpager!=null)
		{
			scrollTo(m_Viewpager.getCurrentItem(), 0);
		}
	}
}

package com.example.flareon;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SlidingTabFragment extends Fragment {

	private SliderViewPage m_SliderPage;
	private ViewPager m_ViewPager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_sample, container,false);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		m_SliderPage = (SliderViewPage)view.findViewById(R.id.sliding_tabs);
		m_ViewPager = (ViewPager)view.findViewById(R.id.viewpager);
		m_ViewPager.setAdapter(new sampleAdapter());
		m_SliderPage.setViewPager(m_ViewPager);
	}
	
	public class sampleAdapter extends PagerAdapter
	{
		List<String> datas = new ArrayList<String>();

		public sampleAdapter() {
			// TODO Auto-generated constructor stub
		datas.add("HI");
		datas.add("Hello");
		datas.add("How are you");
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return (view == object);
		}
		
		public Object instantiateItem(ViewGroup container, int position) 
		{
			// TODO Auto-generated method stub
			View view = LayoutInflater.from(container.getContext()).inflate(R.layout.pager_item, container, false);
            container.addView(view);

            TextView txtTitle = (TextView)view.findViewById(R.id.item_title);
            Integer pos = position + 1;
            txtTitle.setText(pos.toString().toCharArray(),0,pos.toString().toCharArray().length);

            return view;
		}

      
     @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    	// TODO Auto-generated method stub
    	 container.removeView((View)object);
    }

        
	}
}

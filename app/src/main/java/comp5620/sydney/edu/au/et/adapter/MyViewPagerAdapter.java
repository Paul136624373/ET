package comp5620.sydney.edu.au.et.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

public class MyViewPagerAdapter extends PagerAdapter {
    private List<ListView> listView;
    private List<String> titleList;
    public MyViewPagerAdapter(List<ListView> mListView,List<String> titleList) {
        this.listView = mListView;
        this.titleList = titleList;
    }

    @Override
    public int getCount() {
        return listView.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(listView.get(position));
        return listView.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(listView.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
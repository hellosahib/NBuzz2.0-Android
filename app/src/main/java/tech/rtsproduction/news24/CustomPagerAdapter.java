package tech.rtsproduction.news24;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomPagerAdapter extends PagerAdapter {

    private ArrayList<NewsData> dataArrayList;
    private Context mContext;

    public CustomPagerAdapter(Context context, ArrayList<NewsData> newsData) {
        this.dataArrayList = newsData;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        if (dataArrayList != null) {
            return dataArrayList.size();
        }
        return 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        final View mView = LayoutInflater.from(mContext).inflate(R.layout.item_view, container, false);
        TextView titleView = mView.findViewById(R.id.titleTextItem);
        TextView sectionView = mView.findViewById(R.id.sectionNameTextItem);
        TextView dateView = mView.findViewById(R.id.dateTextItem);
        TextView authorView = mView.findViewById(R.id.authorTextItem);
        titleView.setText(dataArrayList.get(position).getmTitle());
        sectionView.setText(dataArrayList.get(position).getmSection());
        dateView.setText(dataArrayList.get(position).getmDate());
        authorView.setText(dataArrayList.get(position).getmAuthorName());
        container.addView(mView);

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri website = Uri.parse(dataArrayList.get(position).getmURL());
                Intent internetIntent = new Intent(Intent.ACTION_VIEW, website);
                mContext.startActivity(internetIntent);
            }
        });
        return mView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}

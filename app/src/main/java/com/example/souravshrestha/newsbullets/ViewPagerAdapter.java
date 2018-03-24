package com.example.souravshrestha.newsbullets;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<String> mImageUrls=new ArrayList<>();
    private ArrayList<String> mClickUrls=new ArrayList<>();

    public ViewPagerAdapter(Context context, ArrayList<String> imageUrls, ArrayList<String> clickUrl) {
        this.context = context;
        mImageUrls=imageUrls;
        mClickUrls=clickUrl;
    }



    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_layout, null);
        ImageView imageView=(ImageView) view.findViewById(R.id.imageView);

//        imageView.setImageResource(images[position]);
        // Glide.with(context).load(mImageUrls.get(position)).into(imageView);
        final Uri uri = Uri.parse(mImageUrls.get(position));
        final Uri uri1=Uri.parse(mClickUrls.get(position));
        Picasso.with(context).load(uri).into(imageView, new com.squareup.picasso.Callback()
        {

            @Override
            public void onSuccess() {
                //Toast.makeText(context, "succes", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                // Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();

            }
        });
        ViewPager vp=(ViewPager)container;
        vp.addView(view,0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(uri1);
                context.startActivity(intent);
                //Toast.makeText(context, "ajka", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ViewPager vp=(ViewPager) container;
        View view=(View) object;
        vp.removeView(view);
    }



}
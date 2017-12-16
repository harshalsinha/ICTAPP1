package com.example.mahe.ictapp1;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListAdapter extends ArrayAdapter{

    int[] images;
    String[] titles;
    String[] subtitles;
    Context c;
    LayoutInflater inflater;

    public ListAdapter(Context context,String[] titles,String[] subtitles,int[] images)
    {
        super(context,R.layout.list_row,titles);

        this.c = context;
        this.images = images;
        this.titles = titles;
        this.subtitles = subtitles;
    }

    public class ViewHolder
    {
        TextView title;
        TextView subtitle;
        ImageView img;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent)
    {
        if(convertView==null)
        {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row,null);
        }

        final ViewHolder holder = new ViewHolder();

        holder.title = (TextView) convertView.findViewById(R.id.title);
        holder.subtitle = (TextView) convertView.findViewById(R.id.subtitle);
        holder.img = (ImageView) convertView.findViewById(R.id.list_image);

        holder.img.setImageResource(images[position]);
        holder.title.setText(titles[position]);
        holder.subtitle.setText(subtitles[position]);

        return convertView;
    }
}

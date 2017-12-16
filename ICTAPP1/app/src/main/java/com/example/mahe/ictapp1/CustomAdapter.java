package com.example.mahe.ictapp1;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter{

        Context context;
        int images[];
        String[] appointmentTypes;
        LayoutInflater inflater;

        public CustomAdapter(Context applicationContext, int[] images, String[] appointmentTypes) {
            this.context = applicationContext;
            this.images = images;
            this.appointmentTypes = appointmentTypes;
            inflater = (LayoutInflater.from(applicationContext));
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = inflater.inflate(R.layout.appointment_spinner, null);
            ImageView icon = (ImageView) view.findViewById(R.id.imageView);
            TextView names = (TextView) view.findViewById(R.id.textView);
            icon.setImageResource(images[i]);
            names.setText(appointmentTypes[i]);
            return view;
        }
    }



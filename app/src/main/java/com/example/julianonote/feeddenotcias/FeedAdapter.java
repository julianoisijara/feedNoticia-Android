package com.example.julianonote.feeddenotcias;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class FeedAdapter extends ArrayAdapter<Feed> {

    private static final String DATA_SEPARATOR = "T";
    private static final String TIME_SEPARATOR = "Z";


    public FeedAdapter(Context context, List<Feed> feeds) {
        super(context, 0, feeds);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.activity_list_item, parent, false);
        }

        Feed currentFeed = getItem(position);


        String temaObjeto = currentFeed.getTema();
        TextView temaView = listItemView.findViewById(R.id.tema);
        temaView.setText(temaObjeto);


        String tituloObject = currentFeed.getTitulo();
        TextView tituloView = listItemView.findViewById(R.id.titulo);
        tituloView.setText(tituloObject);


        String originalDateTime = currentFeed.getTime();
        String data;
        String time;

        if (originalDateTime.contains(DATA_SEPARATOR)) {
            String[] partsData = originalDateTime.split(DATA_SEPARATOR);
            data = partsData[0];
            TextView dateView = listItemView.findViewById(R.id.date);
            dateView.setText(data);
        }

        if (originalDateTime.contains(TIME_SEPARATOR)) {
            String[] partsDateTime = originalDateTime.split(TIME_SEPARATOR);
            String dateTime = partsDateTime[0];
            if (dateTime.contains(DATA_SEPARATOR)) {
                String[] partsTime = dateTime.split(DATA_SEPARATOR);
                time = partsTime[1];
                TextView timeView = listItemView.findViewById(R.id.time);
                timeView.setText(time);
            }
        }

        return listItemView;

    }

}

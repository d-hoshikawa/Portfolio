package com.example.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeckListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<DeckView> data;
    private int resource;
    private LayoutInflater inflater;

    public DeckListAdapter(Context context, ArrayList<DeckView> data, int resource) {
        this.context = context;
        this.data = data;
        this.resource = resource;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DeckView getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DeckView item = getItem(position);
        View view = (convertView != null) ? convertView : inflater.inflate(resource, null);
        TextView tvTitle = view.findViewById(R.id.tvDeckTitleDeckList);
        TextView tvId = view.findViewById(R.id.tvDeckIdDeckList);
        tvTitle.setText(item.getD_name());
        tvId.setText(String.valueOf(item.getD_id()));
        for (int i = 0; i < item.getCards().size(); i++) {
            int rank = item.getCards().get(i).getRank();
            if (rank == 1)
                ((CardView) view.findViewById(R.id.cv1DeckList)).AtoB(item.getCards().get(i));
            else if (rank == 2)
                ((CardView) view.findViewById(R.id.cv2DeckList)).AtoB(item.getCards().get(i));
            else if (rank == 3)
                ((CardView) view.findViewById(R.id.cv3DeckList)).AtoB(item.getCards().get(i));
        }
        return view;
    }
}

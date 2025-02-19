package com.example.test;


import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CardListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CardView> data;
    private int resource;
    private LayoutInflater inflater;

    public CardListAdapter(Context context, ArrayList<CardView> data, int resource) {
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
    public CardView getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CardView item = getItem(position);
        View view = (convertView != null) ? convertView : inflater.inflate(resource, null);
        ((CardView) view.findViewById(R.id.cvCardList)).AtoB(item);
        ((TextView) view.findViewById(R.id.tvNameCardList)).setText(item.getName());
        ((TextView) view.findViewById(R.id.tvAttackNumberCardList)).setText(String.valueOf(item.getAttack()));
        ((TextView) view.findViewById(R.id.tvHPNumberCardList)).setText(String.valueOf(item.getHitpoint()));
        ((TextView) view.findViewById(R.id.tvSpeedNumberCardList)).setText(String.valueOf(item.getSpeed()));
        ((TextView) view.findViewById(R.id.tvRankNumberCardList)).setText(String.valueOf(item.getRank()));
        int[] abilities = {R.drawable.no_monster, R.drawable.bouncing_sword, R.drawable.poison_bottle, R.drawable.healing, R.drawable.backstab
                , R.drawable.spiked_shield, R.drawable.confrontation};
        Resources res = context.getResources();
        String[] ability = res.getStringArray(R.array.ability);
        String[] abilityTitle = res.getStringArray(R.array.ability_title);
        ((ImageView) view.findViewById(R.id.ivAbilityCardList)).setImageResource(abilities[item.getAbility()]);
        TextView tvAbilityCardList = view.findViewById(R.id.tvAbilityCardList);
        TextView tvAbilityTitleCardList = view.findViewById(R.id.tvAbilityTitleCardList);
        tvAbilityCardList.setText(ability[item.getAbility()]);
        tvAbilityTitleCardList.setText(abilityTitle[item.getAbility()]);
        return view;
    }
}

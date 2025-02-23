package com.example.test;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Battle_Select_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Battle_Select_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Battle_Select_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Battle_Select_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Battle_Select_Fragment newInstance(String param1, String param2) {
        Battle_Select_Fragment fragment = new Battle_Select_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_battle__select_, container, false);
        CardViewModel viewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);

        //各種取得
        TextView tvWin = view.findViewById(R.id.tvWinCount);
        TextView tvLose = view.findViewById(R.id.tvLoseCount);
        TextView tvDraw = view.findViewById(R.id.tvDrawCount);
        MyCardView cvMine1 = view.findViewById(R.id.cvMine1);
        MyCardView cvMine2 = view.findViewById(R.id.cvMine2);
        MyCardView cvMine3 = view.findViewById(R.id.cvMine3);
        MyCardView cvComp1 = view.findViewById(R.id.cvComp1);
        MyCardView cvComp2 = view.findViewById(R.id.cvComp2);
        MyCardView cvComp3 = view.findViewById(R.id.cvComp3);
        MyCardView[] cards = {cvMine1, cvMine2, cvMine3, cvComp1, cvComp2, cvComp3};
        MyCardView[] comps = {cvComp1, cvComp2, cvComp3};
        int[] vids = {R.id.cvMine1, R.id.cvMine2, R.id.cvMine3};

        //勝敗表示
        tvWin.setText(String.valueOf(viewModel.getPoints()));
        tvLose.setText(String.valueOf(viewModel.getPointsComp()));
        tvDraw.setText(String.valueOf(viewModel.getDraw()));

        //compのカードを取得するネットワーク構築
        Network gccs = new Network() {
            @Override
            public void onResponse(String string) {
                try {
                    JSONArray ary = new JSONArray(string);
                    for (int i = 0; i < ary.length(); i++) {
                        JSONObject ln = ary.getJSONObject(i);
                        comps[i].setCardId(ln.getInt("id"));
                        comps[i].changeName(ln.getString("name"));
                        comps[i].changeAttack(ln.getInt("attack"));
                        comps[i].changeHP(ln.getInt("hp"));
                        comps[i].changeSpeed(ln.getInt("speed"));
                        comps[i].changeRank(ln.getInt("crank"));
                        comps[i].changeAbility(ln.getInt("ability"));
                        comps[i].changeMonster(ln.getInt("monster"));
                        comps[i].changeBack(ln.getInt("back"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public String onSend() {
                return "";
            }

            @Override
            public String accessURI() {
                return "http://10.0.2.2/comp_card_state.php";
            }
        };
        gccs.execute();

        //カードをセット
        cvMine1.AtoB(viewModel.getCardMine1());
        cvMine2.AtoB(viewModel.getCardMine2());
        cvMine3.AtoB(viewModel.getCardMine3());
        if (viewModel.getCardComp1() == null) {
            viewModel.setCardComp1(cvComp1);
            viewModel.setCardComp2(cvComp2);
            viewModel.setCardComp3(cvComp3);
        }

        //使用済みカードは薄暗く
        int[] ids = {viewModel.getCardMine1().getCardId(), viewModel.getCardMine2().getCardId(), viewModel.getCardMine3().getCardId()
                , viewModel.getCardComp1().getCardId(), viewModel.getCardComp2().getCardId(), viewModel.getCardComp3().getCardId()};
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == -1)
                cards[i].setAlpha(0.15f);
        }

        MyCardView[] cids = {viewModel.getCardMine1(), viewModel.getCardMine2(), viewModel.getCardMine3()};

        //カード選択処理
        View.OnClickListener listener = v -> {
            for (int i = 0; i < vids.length; i++) {
                if (v.getId() == vids[i]) {
                    if (cids[i].getCardId() == -1) {
                        Toast.makeText(requireActivity(), getString(R.string.battled_monster), Toast.LENGTH_SHORT).show();
                    } else {
                        cids[i].setCardId(0);
                        Navigation.findNavController(v).navigate(R.id.battleFragment);
                    }
                }
            }

        };

        cvMine1.setOnClickListener(listener);
        cvMine2.setOnClickListener(listener);
        cvMine3.setOnClickListener(listener);

        return view;
    }
}
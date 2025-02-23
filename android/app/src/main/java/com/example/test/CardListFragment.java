package com.example.test;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CardListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardListFragment newInstance(String param1, String param2) {
        CardListFragment fragment = new CardListFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_list, container, false);

        //各種取得
        TextView tvCard = view.findViewById(R.id.tvCardList);
        ListView lvCardList = view.findViewById(R.id.lvCardList);
        Button btnNewCard = view.findViewById(R.id.btnNewCardList);
        Button btnBackCard = view.findViewById(R.id.btnBackCardList);
        CardViewModel viewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        viewModel.setCardMine1(null);

        //バトルを選択してきたかどうか
        if (getArguments() != null) {
            String source = getArguments().getString("source");
            if ("Deck".equals(source))
                tvCard.setText(getString(R.string.select_card_deck_make));
            else
                tvCard.setText(getString(R.string.select_card_remake));
        }

        //カードのデータ受信用のネットワーク構築＆実行
        Network gcs = new Network() {
            @Override
            public void onResponse(String string) {
                ArrayList<MyCardView> list = new ArrayList<>();
                try {
                    JSONArray ary = new JSONArray(string);
                    for (int i = 0; i < ary.length(); i++) {
                        JSONObject ln = ary.getJSONObject(i);
                        MyCardView item = new MyCardView(requireContext());
                        item.setCardId(ln.getInt("id"));
                        item.changeName(ln.getString("name"));
                        item.changeAttack(ln.getInt("attack"));
                        item.changeHP(ln.getInt("hp"));
                        item.changeSpeed(ln.getInt("speed"));
                        item.setCost(ln.getInt("cost"));
                        item.changeAbility(ln.getInt("ability"));
                        item.changeMonster(ln.getInt("monster"));
                        item.setBack(ln.getInt("back"));
                        item.changeRank(ln.getInt("crank"));
                        list.add(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                CardListAdapter adapter = new CardListAdapter(requireContext(), list, R.layout.view_card_list);
                lvCardList.setAdapter(adapter);
            }

            @Override
            public String onSend() {
                int crank = 0;
                JSONObject json = new JSONObject();
                if (getArguments() != null) {
                    String source = getArguments().getString("source");
                    if ("Deck".equals(source)) {
                        crank = getArguments().getInt("crank");
                    }
                }
                try {
                    json.put("crank", crank);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json.toString();
            }

            @Override
            public String accessURI() {
                return "http://10.0.2.2/card_state.php";
            }
        };
        gcs.execute();

        //リスト押下時の処理
        lvCardList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyCardView cardView = view.findViewById(R.id.cvCardList);
                CardViewModel viewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
                if (getArguments() != null) {
                    String source = getArguments().getString("source");
                    int rank = getArguments().getInt("crank");
                    //もしデッキ作成から来ているなら
                    if ("Deck".equals(source)) {
                        if (rank == 1) {
                            MyCardView cv = new MyCardView(requireContext());
                            viewModel.setCardMine1(cv);
                            viewModel.getCardMine1().AtoB(cardView);
                        } else if (rank == 2) {
                            MyCardView cv = new MyCardView(requireContext());
                            viewModel.setCardMine2(cv);
                            viewModel.getCardMine2().AtoB(cardView);
                        } else {
                            MyCardView cv = new MyCardView(requireContext());
                            viewModel.setCardMine3(cv);
                            viewModel.getCardMine3().AtoB(cardView);
                        }
                        getParentFragmentManager().popBackStack();
                    } else if ("mainCard".equals(source)) {
                        MyCardView cv = new MyCardView(requireContext());
                        viewModel.setCardMine1(cv);
                        viewModel.getCardMine1().AtoB(cardView);
                        Bundle bundle = new Bundle();
                        bundle.putString("source", "cardList");
                        Navigation.findNavController(view).navigate(R.id.cardFragment, bundle);
                    }
                }
            }
        });

        //新規作成ボタンの処理
        btnNewCard.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("source", "new");
            Navigation.findNavController(v).navigate(R.id.cardFragment, bundle);
        });

        //戻るボタンの処理
        btnBackCard.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
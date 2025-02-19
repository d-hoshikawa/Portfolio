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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeckListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeckListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeckListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeckListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeckListFragment newInstance(String param1, String param2) {
        DeckListFragment fragment = new DeckListFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deck_list, container, false);

        //各種取得
        TextView tvDeck = view.findViewById(R.id.tvDeckList);
        ListView lvDeckList = view.findViewById(R.id.lvDeckList);
        Button btnNewDeck = view.findViewById(R.id.btnNewDeckList);
        Button btnBackDeck = view.findViewById(R.id.btnBackDeckList);
        CardViewModel viewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        viewModel.setCardMine1(null);
        viewModel.setCardMine2(null);
        viewModel.setCardMine3(null);
        viewModel.setCardComp1(null);
        viewModel.setCardComp2(null);
        viewModel.setCardComp3(null);
        viewModel.setBattlePhase(0);
        viewModel.setPoints(0);
        viewModel.setDraw(0);

        //バトルを選択してきたかどうか
        if (getArguments() != null) {
            String source = getArguments().getString("source");
            if ("mainBattle".equals(source))
                tvDeck.setText(getString(R.string.select_deck_battle));
            else
                tvDeck.setText(getString(R.string.select_deck_remake));
        }

        //デッキ情報取得ネットワーク構築
        Network gds = new Network() {
            @Override
            public void onResponse(String string) {
                ArrayList<DeckView> list = new ArrayList<>();
                HashMap<Integer, DeckView> deckMap = new HashMap<>();
                try {
                    JSONArray ary = new JSONArray(string);
                    for (int i = 0; i < ary.length(); i++) {
                        JSONObject ln = ary.getJSONObject(i);
                        CardView card = new CardView(requireContext());
                        card.changeCard(ln.getInt("id"), ln.getString("name"), ln.getInt("attack"), ln.getInt("hp")
                                , ln.getInt("speed"), ln.getInt("crank"), ln.getInt("monster"), ln.getInt("ability"));
                        int d_id = ln.getInt("d_id");
                        if (deckMap.containsKey(d_id)) {
                            deckMap.get(d_id).addCard(card);
                        } else {
                            DeckView deck = new DeckView(d_id, ln.getString("d_name"));
                            deck.addCard(card);
                            deckMap.put(d_id, deck);
                        }
                    }
                    list.addAll(deckMap.values());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                DeckListAdapter adapter = new DeckListAdapter(requireContext(), list, R.layout.view_deck_list);
                lvDeckList.setAdapter(adapter);
            }

            @Override
            public String onSend() {
                return "";
            }

            @Override
            public String accessURI() {
                return "http://10.0.2.2/deck_state.php";
            }
        };
        gds.execute();

        //リスト押下時の処理
        lvDeckList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                viewModel.setCardMine1(new CardView(requireContext()));
                viewModel.getCardMine1().AtoB(view.findViewById(R.id.cv1DeckList));
                viewModel.setCardMine2(new CardView(requireContext()));
                viewModel.getCardMine2().AtoB(view.findViewById(R.id.cv2DeckList));
                viewModel.setCardMine3(new CardView(requireContext()));
                viewModel.getCardMine3().AtoB(view.findViewById(R.id.cv3DeckList));
                if (getArguments() != null) {
                    String source = getArguments().getString("source");
                    if ("mainBattle".equals(source)) {
                        Navigation.findNavController(view).navigate(R.id.battle_Select_Fragment);
                    }else {
                        TextView tvTitle = view.findViewById(R.id.tvDeckTitleDeckList);
                        TextView tvId = view.findViewById(R.id.tvDeckIdDeckList);
                        Bundle bundle = new Bundle();
                        bundle.putString("source", "deckList");
                        bundle.putString("title", tvTitle.getText().toString());
                        bundle.putString("d_id", tvId.getText().toString());
                        Navigation.findNavController(view).navigate(R.id.deckFragment, bundle);
                    }
                }
            }
        });

        //新規作成の処理
        btnNewDeck.setOnClickListener(v -> {
            viewModel.setCardMine1(null);
            viewModel.setCardMine2(null);
            viewModel.setCardMine3(null);
            Bundle bundle = new Bundle();
            bundle.putString("source", "newDeck");
            Navigation.findNavController(v).navigate(R.id.deckFragment);
        });

        //戻るボタンの処理
        btnBackDeck.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
package com.example.test;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeckFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeckFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeckFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeckFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeckFragment newInstance(String param1, String param2) {
        DeckFragment fragment = new DeckFragment();
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
        View view = inflater.inflate(R.layout.fragment_deck, container, false);

        //各種取得
        Button btnSave = view.findViewById(R.id.btnSaveDeck);
        Button btnBack = view.findViewById(R.id.btnBackDeck);
        Button btnSelect1 = view.findViewById(R.id.btnSelect1Deck);
        Button btnSelect2 = view.findViewById(R.id.btnSelect2Deck);
        Button btnSelect3 = view.findViewById(R.id.btnSelect3Deck);
        Button btnSaveTitle = view.findViewById(R.id.btnTitleSaveDeck);
        Button btnDelete = view.findViewById(R.id.btnDeleteDeck);
        TextView tvTitle = view.findViewById(R.id.tvDeckTitle);
        EditText etTitle = view.findViewById(R.id.etDeckTitle);
        CardView cv1 = view.findViewById(R.id.cv1Deck);
        CardView cv2 = view.findViewById(R.id.cv2Deck);
        CardView cv3 = view.findViewById(R.id.cv3Deck);
        CardViewModel viewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);

        //デッキ登録用ネットワーク構築
        Network sds = new Network() {
            @Override
            public void onResponse(String result) {
            }

            @Override
            public String onSend() {
                JSONObject json = new JSONObject();
                try {
                    if (getArguments() != null) {
                        String source = getArguments().getString("source");
                        if ("deckList".equals(source))
                            json.put("d_id", Integer.valueOf(getArguments().getString("d_id")));
                        else
                            json.put("d_id", 0);
                    }
                    json.put("name", tvTitle.getText());
                    json.put("card1", cv1.getCardId());
                    json.put("card2", cv2.getCardId());
                    json.put("card3", cv3.getCardId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json.toString();
            }

            @Override
            public String accessURI() {
                return "http://10.0.2.2/receive_deck_state.php";
            }
        };

        //デッキ削除用ネットワーク構築
        Network dds = new Network() {
            @Override
            public void onResponse(String result) {
            }

            @Override
            public String onSend() {
                JSONObject json = new JSONObject();
                try {
                    if (getArguments() != null) {
                        String source = getArguments().getString("source");
                        if ("deckList".equals(source))
                            json.put("d_id", Integer.valueOf(getArguments().getString("d_id")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json.toString();
            }

            @Override
            public String accessURI() {
                return "http://10.0.2.2/delete_deck_state.php";
            }
        };

        //デッキリストのリストからか新規からか
        //削除ボタンを非表示に(編集時のみ表示)
        btnDelete.setVisibility(View.INVISIBLE);
        if (getArguments() != null) {
            String source = getArguments().getString("source");
            if ("deckList".equals(source)) {
                tvTitle.setText(getArguments().getString("title"));
                btnDelete.setVisibility(View.VISIBLE);
            }
        }

        //作成中は選択済みカードを設置
        if (viewModel.getCardMine1() != null) {
            cv1.AtoB(viewModel.getCardMine1());
        }
        if (viewModel.getCardMine2() != null) {
            cv2.AtoB(viewModel.getCardMine2());
        }
        if (viewModel.getCardMine3() != null) {
            cv3.AtoB(viewModel.getCardMine3());
        }

        //カードを選ぶボタンの処理
        View.OnClickListener listener = v -> {
            int[] btns = {R.id.btnSelect1Deck, R.id.btnSelect2Deck, R.id.btnSelect3Deck};
            for (int i = 0; i < btns.length; i++) {
                if (v.getId() == btns[i]) {
                    Bundle bundle = new Bundle();
                    bundle.putString("source", "Deck");
                    bundle.putInt("crank", i + 1);
                    Navigation.findNavController(v).navigate(R.id.cardListFragment, bundle);
                }
            }
        };
        btnSelect1.setOnClickListener(listener);
        btnSelect2.setOnClickListener(listener);
        btnSelect3.setOnClickListener(listener);

        //デッキ名決定ボタンの処理
        btnSaveTitle.setOnClickListener(v -> {
            tvTitle.setText(etTitle.getText().toString());
        });

        //保存ボタンの処理
        btnSave.setOnClickListener(v -> {
            CardView[] cvs = {cv1, cv2, cv3};
            boolean deck = true;
            //選択されてないカードが無いかどうか
            for (int i = 0; i < cvs.length; i++) {
                if (cvs[i].getCardId() == 0) {
                    //未完成ダイアログ
                    new AlertDialog.Builder(getActivity())
                            .setMessage(getString(R.string.deck_non_dialog))
                            .setPositiveButton(R.string.ok, null)
                            .show();
                    deck = false;
                    break;
                }
            }
            if (deck) {
                String next = "";
                //新規作成かどうか
                if (getArguments() != null) {
                    String source = getArguments().getString("source");
                    if ("deckList".equals(source)) {
                        next = getString(R.string.next_deck_remake);
                    }
                } else {
                    next = getString(R.string.next_deck_make);
                }
                sds.execute();
                //確認ダイアログ
                new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.save_completed)
                        .setPositiveButton(next, null)
                        .setNegativeButton(R.string.back_deck_list, (dialog, which) -> {
                            getParentFragmentManager().popBackStack();
                        }).show();
            }
        });

        //削除ボタンの処理
        btnDelete.setOnClickListener(v -> {
            dds.execute();
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.delete_deck_completed)
                    .setPositiveButton(getString(R.string.back_deck_list), (dialog, which) -> {
                        getParentFragmentManager().popBackStack();
                    }).show();
        });

        //戻るボタンの処理
        btnBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
package com.example.test;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardFragment newInstance(String param1, String param2) {
        CardFragment fragment = new CardFragment();
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
        View view = inflater.inflate(R.layout.fragment_card, container, false);

        //各種取得
        MyCardView cv = view.findViewById(R.id.cardView);
        EditText etName = view.findViewById(R.id.etName);
        Spinner spAttack = view.findViewById(R.id.spAttack);
        Spinner spHitPoint = view.findViewById(R.id.spHitPoint);
        Spinner spSpeed = view.findViewById(R.id.spSpeed);
        Spinner spCost = view.findViewById(R.id.spCost);
        Button btnName = view.findViewById(R.id.btnName);
        Button btnMonster = view.findViewById(R.id.btnMonster);
        Button btnBack = view.findViewById(R.id.btnBack);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnDelete = view.findViewById(R.id.btnDelete);
        ImageView ivAbility = view.findViewById(R.id.ivAbilityCard);
        ImageView ivRight = view.findViewById(R.id.ivRight);
        ImageView ivLeft = view.findViewById(R.id.ivLeft);
        TextView tvCost = view.findViewById(R.id.tvUsableCostNumber);
        TextView tvAbility = view.findViewById(R.id.tvAbility);
        TextView tvAbilityTitle = view.findViewById(R.id.tvAbilityTitle);
        ArrayList<Integer> abilities = new ArrayList<>(Arrays.asList(
                R.drawable.no_monster,
                R.drawable.bouncing_sword,
                R.drawable.poison_bottle,
                R.drawable.healing,
                R.drawable.backstab,
                R.drawable.spiked_shield,
                R.drawable.confrontation
        ));
        Resources res = getResources();
        String[] ability = res.getStringArray(R.array.ability);
        String[] abilityTitle = res.getStringArray(R.array.ability_title);

        //モンスターのイラストセット
        CardViewModel viewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        if (viewModel.getCardMine1() == null) {
            viewModel.setCardMine1(new MyCardView(requireContext()));
        } else {
            cv.AtoB(viewModel.getCardMine1());
        }

        //新規ではなくカードリストから選択された場合
        //削除ボタンを非表示に(編集時のみ表示)
        btnDelete.setVisibility(View.INVISIBLE);
        if (getArguments() != null) {
            String source = getArguments().getString("source");
            if ("cardList".equals(source)) {
                cv.AtoB(viewModel.getCardMine1());
                spAttack.setSelection(cv.getAttack() - 1);
                spHitPoint.setSelection(cv.getHitpoint() - 1);
                spSpeed.setSelection(cv.getSpeed() - 1);
                spCost.setSelection(cv.getRank() - 1);
                btnDelete.setVisibility(View.VISIBLE);
            }
        }
        int cvAb = cv.getAbility();
        tvAbilityTitle.setText(abilityTitle[cvAb]);
        tvAbility.setText(ability[cvAb]);
        ivAbility.setImageResource(abilities.get(cvAb));

        //カード情報の登録用ネットワーク構築
        Network scs = new Network() {
            @Override
            public void onResponse(String result) {
            }

            @Override
            public String onSend() {
                JSONObject json = new JSONObject();
                try {
                    //新規登録かどうかの確認
                    if (getArguments() != null) {
                        String source = getArguments().getString("source");
                        //新規でない場合は上書き
                        if ("cardList".equals(source)) {
                            json.put("id", cv.getCardId());
                            json.put("overRide", "on");
                        } else {

                            json.put("overRide", "off");
                        }
                    }
                    json.put("name", cv.getName());
                    json.put("attack", cv.getAttack());
                    json.put("hp", cv.getHitpoint());
                    json.put("speed", cv.getSpeed());
                    json.put("cost", cv.getCost());
                    json.put("ability", cv.getAbility());
                    json.put("monster", cv.getMonster());
                    json.put("back", cv.getBack());
                    json.put("crank", cv.getRank());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json.toString();
            }

            @Override
            public String accessURI() {
                return "http://10.0.2.2/receive_card_state.php";
            }
        };

        //カード情報の削除用ネットワーク構築
        Network dcs = new Network() {
            @Override
            public void onResponse(String result) {
            }

            @Override
            public String onSend() {
                JSONObject json = new JSONObject();
                try {
                    //新規登録かどうかの確認
                    if (getArguments() != null) {
                        String source = getArguments().getString("source");
                        //新規でない場合は上書き
                        if ("cardList".equals(source)) {
                            json.put("id", cv.getCardId());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return json.toString();
            }

            @Override
            public String accessURI() {
                return "http://10.0.2.2/delete_card_state.php";
            }
        };

        //攻撃力選択
        spAttack.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cv.changeAttack(position + 1);
                cv.costNowDisplay(tvCost);
                viewModel.getCardMine1().setAttack(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //体力選択
        spHitPoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cv.changeHP(position + 1);
                cv.costNowDisplay(tvCost);
                viewModel.getCardMine1().setHitpoint(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //素早さ選択
        spSpeed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cv.changeSpeed(position + 1);
                cv.costNowDisplay(tvCost);
                viewModel.getCardMine1().setSpeed(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //能力選択
        View.OnClickListener listener = v -> {
            int ab = cv.getAbility();
            if (v.getId() == R.id.ivRight) {
                if (ab + 1 == abilities.size()) {
                    cv.changeAbility(0);
                } else {
                    cv.changeAbility(ab + 1);
                }
            } else {
                if (ab == 0) {
                    cv.changeAbility(abilities.size() - 1);
                } else {
                    cv.changeAbility(ab - 1);
                }
            }
            int ab2 = cv.getAbility();
            cv.costNowDisplay(tvCost);
            ivAbility.setImageResource(abilities.get(ab2));
            tvAbility.setText(ability[ab2]);
            tvAbilityTitle.setText(abilityTitle[ab2]);
            viewModel.getCardMine1().setAbility(ab2);
        };
        ivRight.setOnClickListener(listener);
        ivLeft.setOnClickListener(listener);

        //カードランク選択
        spCost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cv.changeRank(position + 1);
                cv.costNowDisplay(tvCost);
                viewModel.getCardMine1().setRank(position + 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //名前入力後決定ボタン
        btnName.setOnClickListener(v -> {
            cv.changeName(etName.getText().toString());
            viewModel.getCardMine1().setName(etName.getText().toString());
        });

        //イラスト変更ボタン
        btnMonster.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.cardIllustFragment);
        });

        //戻るボタン
        btnBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        //保存ボタン
        btnSave.setOnClickListener(v -> {
            int c = cv.getCostNow();
            if (c < 0) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.cost_over_dialog))
                        .setPositiveButton(R.string.ok, null)
                        .show();
            } else if (c == 0) {
                scs.execute();
                new AlertDialog.Builder(getActivity())
                        .setMessage(R.string.save_completed)
                        .setPositiveButton(R.string.next_card_make, null)
                        .setNegativeButton(R.string.back_card_list, (dialog, which) -> {
                            getParentFragmentManager().popBackStack();
                        }).show();
            } else {
                new AlertDialog.Builder(getActivity())
                        .setMessage(getString(R.string.cost_under_dialog))
                        .setPositiveButton(R.string.ok, (dialog, which) -> {
                            cv.setCostNow(0);
                            btnSave.performClick();
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            }
        });

        //削除ボタン
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.delete_card_alert)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        dcs.execute();
                        new AlertDialog.Builder(getActivity())
                                .setMessage(R.string.delete_card_completed)
                                .setPositiveButton(R.string.back_card_list, (dialog2, which2) -> {
                                    getParentFragmentManager().popBackStack();
                                }).show();
                    }).setNegativeButton(R.string.no, null)
                    .show();
        });


        return view;
    }


}
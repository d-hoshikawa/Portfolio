package com.example.test;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardIllustFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardIllustFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CardIllustFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardIllustFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardIllustFragment newInstance(String param1, String param2) {
        CardIllustFragment fragment = new CardIllustFragment();
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
        View view = inflater.inflate(R.layout.fragment_card_illust, container, false);

        //各種取得
        Button btnBack = view.findViewById(R.id.btnBackMonsterImage);
        CardViewModel viewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.rvCardIllust);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        // 画像リソースIDをArrayListに追加
        ArrayList<Integer> imageIds = new ArrayList<>(Arrays.asList(
                R.drawable.fantasy_leviathan,
                R.drawable.fantasy_golem,
                R.drawable.setsubun_oni_kowai,
                R.drawable.fantasy_game_character_slime,
                R.drawable.fantasy_dark_elf,
                R.drawable.fantasy_elf2,
                R.drawable.mahoutsukai_wind,
                R.drawable.mahoutsukai_fire,
                R.drawable.mahoutsukai_thunder,
                R.drawable.mahoutsukai_water,
                R.drawable.mahoutsukai_necromancer,
                R.drawable.character_game_mimic,
                R.drawable.fantasy_behemoth,
                R.drawable.fantasy_berserker,
                R.drawable.fantasy_dragon_wyvern,
                R.drawable.fantasy_dullahan,
                R.drawable.fantasy_golem,
                R.drawable.fantasy_harpy,
                R.drawable.fantasy_hippogriff,
                R.drawable.fantasy_kerberos,
                R.drawable.mahoutsukai_necromancer,
                R.drawable.fantasy_miiraotoko,
                R.drawable.fantasy_minotaur,
                R.drawable.fantasy_ocean_kraken,
                R.drawable.fantasy_orc,
                R.drawable.fantasy_ryuukishi,
                R.drawable.fantasy_zombie_dog,
                R.drawable.fantasy_pixy2,
                R.drawable.youkai_umibouzu,
                R.drawable.fantasy_lamia
        ));

        // アダプターのセット（クリックリスナーも設定）
        CardIllustAdapter adapter = new CardIllustAdapter(requireContext(), imageIds, position -> {
            viewModel.getCardMine1().setMonster(position);
            getParentFragmentManager().popBackStack();
        });
        recyclerView.setAdapter(adapter);

        //戻るボタン
        btnBack.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        return view;
    }
}
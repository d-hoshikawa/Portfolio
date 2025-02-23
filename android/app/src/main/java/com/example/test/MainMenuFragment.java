package com.example.test;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMenuFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainMenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainMenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMenuFragment newInstance(String param1, String param2) {
        MainMenuFragment fragment = new MainMenuFragment();
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
        View view = inflater.inflate(R.layout.fragment_main_menu, container, false);

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

        view.findViewById(R.id.btnBattle).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("source", "mainBattle");
            Navigation.findNavController(v).navigate(R.id.deckListFragment, bundle);
        });

        view.findViewById(R.id.btnCard).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("source", "mainCard");
            Navigation.findNavController(v).navigate(R.id.cardListFragment, bundle);
        });

        view.findViewById(R.id.btnDeck).setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("source", "mainDeck");
            Navigation.findNavController(v).navigate(R.id.deckListFragment, bundle);
        });

        view.findViewById(R.id.btnTutorial).setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.tutorialFragment);
        });

        return view;
    }
}
package com.example.test;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BattleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BattleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BattleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BattleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BattleFragment newInstance(String param1, String param2) {
        BattleFragment fragment = new BattleFragment();
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

    private ArrayList<CardView> cards = new ArrayList<>();
    private int turn = -1;
    private int poisonSelf = 0;
    private int poisonOpp = 0;
    private int gutsSelf = 0;
    private int gutsOpp = 0;
    int turnCount = 0;
    private CardView battleAT, battleDF;
    private View view;
    private CardViewModel viewModel;
    private TextView anser;
    private Button btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_battle, container, false);

        //各種取得
        Random rand = new Random();
        anser = view.findViewById(R.id.anser);
        btnNext = view.findViewById(R.id.btnNext);
        viewModel = new ViewModelProvider(requireActivity()).get(CardViewModel.class);
        cards = new ArrayList<>();
        CardView card = view.findViewById(R.id.cvMineBattle);
        CardView card2 = view.findViewById(R.id.cvCompBattle);

        //最終バトルの場合次へボタンの表示変更
        if (viewModel.getBattlePhase() == 2) {
            btnNext.setText(getString(R.string.battle_end));
        }

        //バトルが終わるまではボタンは無効に
        btnNext.setVisibility(View.INVISIBLE);

        //カードデータ登録
        //ユーザー側
        CardView[] cids = {viewModel.getCardMine1(), viewModel.getCardMine2(), viewModel.getCardMine3()};
        for (int i = 0; i < cids.length; i++) {
            if (cids[i].getCardId() == 0) {
                card.AtoB(cids[i]);
                cids[i].setCardId(-1);
                card.setCardId(-1);
            }
        }

        //コンピューター側
        CardView[] cidsComp = {viewModel.getCardComp1(), viewModel.getCardComp2(), viewModel.getCardComp3()};
        int cnt = 0;
        while (true) {
            if (cidsComp[cnt].getCardId() != -1 && rand.nextInt(3) == cnt) {
                card2.AtoB(cidsComp[cnt]);
                cidsComp[cnt].setCardId(-1);
                break;
            } else if (cnt < cidsComp.length - 1) {
                cnt += 1;
            } else {
                cnt = 0;
            }
        }

        int cs0 = card.getSpeed();
        int cs1 = card2.getSpeed();

        //先攻後攻決定
        if (cs0 > cs1) {
            cards.add(card);
            cards.add(card2);
        } else if (cs0 < cs1) {
            cards.add(card2);
            cards.add(card);
        } else {
            if (rand.nextInt(2) == 0) {
                cards.add(card);
                cards.add(card2);
            } else {
                cards.add(card2);
                cards.add(card);
            }
        }

        // 戦闘開始（非同期でターンを進める）
        runBattleTurn();

        // 次へボタンの処理（戦闘終了後）
        btnNext.setOnClickListener(v -> {
            if (viewModel.getBattlePhase() == 3) {
                viewModel.setBattlePhase(0);
                viewModel.setPoints(0);
                getParentFragmentManager().popBackStack();
                getParentFragmentManager().popBackStack();
                getParentFragmentManager().popBackStack();
            } else {
                getParentFragmentManager().popBackStack();
            }
        });

        return view;
    }

    // 1ターン分の処理を非同期に行うメソッド
    private void runBattleTurn() {
        // 戦闘終了チェック
        if (battleDF != null && battleDF.getHitpoint() <= 0) {
            showBattleResult();
            return;
        } else if (battleDF != null && battleAT.getHitpoint() <= 0) {
            // 攻撃者と防御者を交代
            CardView temp = battleAT;
            battleAT = battleDF;
            battleDF = temp;
            showBattleResult();
            return;
        }

        //勝負がつかずに20ターン経過したら終了
        if (turnCount == 20) {
            showBattleResult();
            return;
        }

        // ターンプレイヤーの決定（初回はcardsの先頭2枚を設定）
        if (turn == -1) {
            battleAT = cards.get(0);
            battleDF = cards.get(1);
            turn = 1;
            ability1();
        } else {
            // 攻撃者と防御者を交代
            CardView temp = battleAT;
            battleAT = battleDF;
            battleDF = temp;
        }

        // ① 攻撃側のアニメーション：攻撃者が前に出て相手にぶつかる動き
        ObjectAnimator attackAnim;
        if (battleDF.getCardId() == -1) {
            attackAnim = ObjectAnimator.ofFloat(battleAT, "translationY", 0f, 100f, 0f);
        } else {
            attackAnim = ObjectAnimator.ofFloat(battleAT, "translationY", 0f, -100f, 0f);
        }
        attackAnim.setDuration(500); // 500ミリ秒
        attackAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // ② 防御側のアニメーション：防御者が揺れる（ぶつかった衝撃を表現）
                ObjectAnimator defenseShake = ObjectAnimator.ofFloat(battleDF, "translationX", 0f, 10f, -10f, 10f, -10f, 10f, 0f);
                defenseShake.setDuration(300); // 300ミリ秒
                defenseShake.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // ③ ダメージ計算：防御側のHPから攻撃側の攻撃力分を引く
                        int newHP = battleDF.getHitpoint() - battleAT.getAttack();
                        battleDF.changeHP(newHP); // HP表示の更新（各CardViewで実装済みの場合）
                        //「くいしばり」の判定
                        ability4();
                        //能力「自己再生」の判定
                        ability3();
                        //能力「猛毒」の判定
                        ability2();

                        // HPが0以下なら戦闘終了、そうでなければ次のターンへ
                        if (battleDF.getHitpoint() <= 0) {
                            showBattleResult();
                        } else {
                            //「トゲ」の判定
                            ability5();
                            //「くいしばり」の判定
                            ability4();
                            // 少しの遅延後に次のターンを開始（500ミリ秒のディレイ）
                            battleAT.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    turn = 1 - turn;
                                    turnCount += 1;
                                    runBattleTurn();
                                }
                            }, 500);
                        }
                    }
                });
                defenseShake.start();
            }
        });
        attackAnim.start();
    }

    //各能力の処理
    //1,先制攻撃
    private void ability1() {
        if (battleAT.getAbility() != 6 && battleDF.getAbility() != 6) {
            if (battleAT.getAbility() == 1)
                if (battleDF.getHitpoint() != 1) {
                    battleDF.triggerAbilityEffect(1);
                    battleDF.changeHP(battleDF.getHitpoint() - (battleAT.getAttack() / 2));
                }
            if (battleDF.getAbility() == 1) {
                if (battleAT.getHitpoint() != 1) {
                    battleAT.triggerAbilityEffect(1);
                    battleAT.changeHP(battleAT.getHitpoint() - (battleDF.getAttack() / 2));
                }
            }
        }
    }

    //2,猛毒
    private void ability2() {
        // 防御側が能力6（無効化）を持っていなければ
        if (battleDF.getAbility() != 6) {
            // 攻撃側が猛毒（能力2）を持っている場合
            if (battleAT.getAbility() == 2) {
                battleDF.triggerAbilityEffect(2);
                // 自分のカードなら (id == -1)
                if (battleAT.getCardId() == -1) {
                    poisonSelf++;  // 自分の猛毒カウンターを増加
                    battleDF.changeHP(battleDF.getHitpoint() - poisonSelf);
                } else {  // そうでなければ相手のカード
                    poisonOpp++;   // 相手の猛毒カウンターを増加
                    battleDF.changeHP(battleDF.getHitpoint() - poisonOpp);
                }
            }
        }
    }

    //3,自己再生
    private void ability3() {
        if (battleAT.getAbility() != 6) {
            if (battleDF.getAbility() == 3) {
                battleDF.triggerAbilityEffect(3);
                battleDF.changeHP(battleDF.getHitpoint() + 1);
            }
        }
    }

    //くいしばり
    private void ability4() {
        // もしどちらかのカードが能力6（無効化効果）を持っていれば、くいしばり効果は発動しない
        if (battleAT.getAbility() == 6 || battleDF.getAbility() == 6) {
            return;
        }

        // 攻撃側（battleAT）のくいしばり判定
        if (battleAT.getAbility() == 4 && battleAT.getHitpoint() <= 0) {
            if (battleAT.getCardId() == -1) {
                // 自分のカードの場合
                if (gutsSelf == 0) {
                    battleAT.triggerAbilityEffect(4);
                    battleAT.changeHP(1);
                    gutsSelf = 1;
                }
            } else {
                // 相手のカードの場合
                if (gutsOpp == 0) {
                    battleAT.triggerAbilityEffect(4);
                    battleAT.changeHP(1);
                    gutsOpp = 1;
                }
            }
        }

        // 防御側（battleDF）のくいしばり判定
        if (battleDF.getAbility() == 4 && battleDF.getHitpoint() <= 0) {
            if (battleDF.getCardId() == -1) {
                // 自分のカードの場合
                if (gutsSelf == 0) {
                    battleDF.triggerAbilityEffect(4);
                    battleDF.changeHP(1);
                    gutsSelf = 1;
                }
            } else {
                // 相手のカードの場合
                if (gutsOpp == 0) {
                    battleDF.triggerAbilityEffect(4);
                    battleDF.changeHP(1);
                    gutsOpp = 1;
                }
            }
        }
    }

    //トゲ
    private void ability5() {
        if (battleAT.getAbility() != 6) {
            if (battleDF.getAbility() == 5) {
                battleDF.triggerAbilityEffect(5);
                battleAT.changeHP(battleAT.getHitpoint() - 1);
            }
        }
    }

    //ガチンコ勝負
    private void ability6() {
    }

    //ここまで能力

    //戦闘結果表示
    private void showBattleResult() {
        // 戦闘結果の表示処理
        if (turnCount == 20) {
            anser.setText(getString(R.string.draw));
            viewModel.setDraw(viewModel.getDraw() + 1);
        } else if (battleDF.getCardId() == -1) {
            anser.setText(battleDF.getName() + getString(R.string.monster_lose));
            viewModel.setPointsComp(viewModel.getPointsComp() + 1);
        } else {
            anser.setText(battleAT.getName() + getString(R.string.monster_win));
            viewModel.setPoints(viewModel.getPoints() + 1);
        }

        //次へボタンを有効に
        btnNext.setVisibility(View.VISIBLE);

        // 戦闘フェーズの更新
        viewModel.setBattlePhase(viewModel.getBattlePhase() + 1);
        //バトルの結果発表
        int points = viewModel.getPoints();
        int pointsComp = viewModel.getPointsComp();
        if (viewModel.getBattlePhase() == 3) {
            TextView tvWinLose = view.findViewById(R.id.tvYouWinLose);
            if (points > pointsComp)
                tvWinLose.setText(getString(R.string.you_win));
            else if (points < pointsComp)
                tvWinLose.setText(getString(R.string.you_lose));
            else if (points == pointsComp)
                tvWinLose.setText(getString(R.string.draw));
        }
    }
}
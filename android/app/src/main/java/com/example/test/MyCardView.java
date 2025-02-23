package com.example.test;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class MyCardView extends ConstraintLayout {

    //攻撃力
    private int attack = 1;

    //体力
    private int hitpoint = 1;

    //素早さ
    private int speed = 1;

    private int rank = 1;

    //コスト上限
    private int cost = 10;

    //現在コスト
    private int costNow = 10;

    //所持能力
    private int ability = 0;

    //イラスト
    private int monster = 0;

    //背景
    private int back = 0;

    private String name = "";

    private int cardId = 0;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public int getCostNow() {
        return costNow;
    }

    public void setCostNow(int costNow) {
        this.costNow = costNow;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getHitpoint() {
        return hitpoint;
    }

    public void setHitpoint(int hitpoint) {
        this.hitpoint = hitpoint;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getAbility() {
        return ability;
    }

    public void setAbility(int ability) {
        this.ability = ability;
    }

    public int getMonster() {
        return monster;
    }

    public void setMonster(int monster) {
        this.monster = monster;
    }

    public int getBack() {
        return back;
    }

    public void setBack(int back) {
        this.back = back;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MyCardView(@NonNull Context context) {
        this(context, null);
    }

    public MyCardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCardView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(getContext(), R.layout.view_card, this);
    }

    int[] numbers = {R.drawable.number0, R.drawable.number1, R.drawable.number2, R.drawable.number3, R.drawable.number4
            , R.drawable.number5, R.drawable.number6, R.drawable.number7, R.drawable.number8, R.drawable.number9};

    ArrayList<Integer> monsters = new ArrayList<>(Arrays.asList(
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

    int[] frames = {R.drawable.cardframe3, R.drawable.cardframe2, R.drawable.cardframe};

    int[] costs = {10, 15, 20};
    int[] abilityCosts = {0, 4, 3, 2, 5, 2, 3};

    ArrayList<Integer> abilities = new ArrayList<>(Arrays.asList(
            R.drawable.no_monster,
            R.drawable.bouncing_sword,
            R.drawable.poison_bottle,
            R.drawable.healing,
            R.drawable.backstab,
            R.drawable.spiked_shield,
            R.drawable.confrontation
    ));
    int[] backs = {R.drawable.background_bkue, R.drawable.background_red};

    public void changeAttack(int at) {
        ImageView ivAttack = findViewById(R.id.ivAttack);
        setAttack(at);
        costNow();
        ivAttack.setImageResource(numbers[at]);
    }

    public void changeHP(int hp) {
        ImageView ivHitPoint = findViewById(R.id.ivHitPoint);
        setHitpoint(hp);
        costNow();
        if (hp < 0) {
            ivHitPoint.setImageResource(numbers[0]);
        } else {
            ivHitPoint.setImageResource(numbers[hp]);
        }
    }

    public void changeSpeed(int sp) {
        ImageView ivSpeed = findViewById(R.id.ivSpeed);
        setSpeed(sp);
        costNow();
        ivSpeed.setImageResource(numbers[sp]);
    }

    public void changeMonster(int mn) {
        ImageView ivMonster = findViewById(R.id.ivMonster);
        setMonster(mn);
        ivMonster.setImageResource(monsters.get(mn));
    }

    public void changeRank(int rn) {
        ImageView ivFrame = findViewById(R.id.ivFrame);
        setRank(rn);
        setCost(costs[rn - 1]);
        costNow();
        ivFrame.setImageResource(frames[rn - 1]);
    }

    public void changeAbility(int ab) {
        ImageView ivAbility = findViewById(R.id.ivAbility);
        setAbility(ab);
        costNow();
        ivAbility.setImageResource(abilities.get(ab));
    }

    public void changeName(String nm) {
        TextView tvName = findViewById(R.id.tvName);
        setName(nm);
        tvName.setText(nm);
    }

    public void changeBack(int bk) {
        setBack(bk);
        ImageView ivBack = findViewById(R.id.ivBack);
        ivBack.setImageResource(backs[bk]);
    }

    public void costNow() {
        setCostNow(getCost() - getHitpoint() - (getAttack() * 2) - getSpeed() - abilityCosts[getAbility()] + 4);
    }

    public void changeCard(int id, String nm, int at, int hp, int sp, int rn, int mn, int ab) {
        setCardId(id);
        changeName(nm);
        changeAttack(at);
        changeHP(hp);
        changeSpeed(sp);
        changeRank(rn);
        changeMonster(mn);
        changeAbility(ab);
        setCostNow(getCost() - getHitpoint() - (getAttack() * 2) - getSpeed() + 4);
    }

    public void AtoB(MyCardView a) {
        this.setCardId(a.getCardId());
        this.changeName(a.getName());
        this.changeAttack(a.getAttack());
        this.changeHP(a.getHitpoint());
        this.changeSpeed(a.getSpeed());
        this.changeRank(a.getRank());
        this.changeMonster(a.getMonster());
        this.changeAbility(a.getAbility());
        this.changeBack(a.getBack());
        setCostNow(getCost() - getHitpoint() - (getAttack() * 2) - getSpeed() + 4);
    }

    public void costNowDisplay(TextView tv) {
        int c = this.getCostNow();
        tv.setText(String.format("%s", c));
        if (c < 0) {
            tv.setTextColor(Color.RED);
        } else {
            tv.setTextColor(Color.BLACK);
        }
    }

    public void triggerAbilityEffect(int abilityId) {
        // エフェクト表示用のImageViewを取得
        ImageView effectView = findViewById(R.id.effectView);
        int effectResId = 0;

        // 能力IDに応じたエフェクト画像を選択
        switch (abilityId) {
            case 1:
                // 能力1：攻撃側にエフェクト
                effectResId = R.drawable.bouncing_sword;
                break;
            case 2:
                // 能力2：防御側にエフェクト
                effectResId = R.drawable.poison_bottle;
                break;
            case 3:
                // 能力3：防御側にエフェクト
                effectResId = R.drawable.healing;
                break;
            case 4:
                // 能力4：発動した側にエフェクト
                effectResId = R.drawable.backstab;
                break;
            case 5:
                // 能力5：防御側にエフェクト
                effectResId = R.drawable.spiked_shield;
                break;
            default:
                return;
        }

        // エフェクト画像をセットし、表示（最初は透明状態からフェードイン→フェードアウト）
        effectView.setImageResource(effectResId);
        effectView.setAlpha(0f);
        effectView.setVisibility(View.VISIBLE);
        // フェードイン
        effectView.animate().alpha(1f).setDuration(300).withEndAction(() -> {
            // 少し表示後にフェードアウト
            effectView.animate().alpha(0f).setDuration(300).withEndAction(() -> {
                effectView.setVisibility(View.INVISIBLE);
            }).start();
        }).start();
    }

}
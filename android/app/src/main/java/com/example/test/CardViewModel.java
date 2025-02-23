package com.example.test;

import androidx.lifecycle.ViewModel;

public class CardViewModel extends ViewModel {
    private int points = 0;
    private int pointsComp = 0;
    private int draw = 0;
    private int battlePhase = 0;
    private MyCardView cardMine1;
    private MyCardView cardMine2;
    private MyCardView cardMine3;
    private MyCardView cardComp1;
    private MyCardView cardComp2;
    private MyCardView cardComp3;

    public int getPointsComp() {
        return pointsComp;
    }

    public void setPointsComp(int pointsComp) {
        this.pointsComp = pointsComp;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getBattlePhase() {
        return battlePhase;
    }

    public void setBattlePhase(int battlePhase) {
        this.battlePhase = battlePhase;
    }

    public MyCardView getCardMine1() {
        return cardMine1;
    }

    public void setCardMine1(MyCardView cardMine1) {
        this.cardMine1 = cardMine1;
    }

    public MyCardView getCardMine2() {
        return cardMine2;
    }

    public void setCardMine2(MyCardView cardMine2) {
        this.cardMine2 = cardMine2;
    }

    public MyCardView getCardMine3() {
        return cardMine3;
    }

    public void setCardMine3(MyCardView cardMine3) {
        this.cardMine3 = cardMine3;
    }

    public MyCardView getCardComp1() {
        return cardComp1;
    }

    public void setCardComp1(MyCardView cardComp1) {
        this.cardComp1 = cardComp1;
    }

    public MyCardView getCardComp2() {
        return cardComp2;
    }

    public void setCardComp2(MyCardView cardComp2) {
        this.cardComp2 = cardComp2;
    }

    public MyCardView getCardComp3() {
        return cardComp3;
    }

    public void setCardComp3(MyCardView cardComp3) {
        this.cardComp3 = cardComp3;
    }
}

package com.example.test;

import androidx.lifecycle.ViewModel;

public class CardViewModel extends ViewModel {
    private int points = 0;
    private int pointsComp = 0;
    private int draw = 0;
    private int battlePhase = 0;
    private CardView cardMine1;
    private CardView cardMine2;
    private CardView cardMine3;
    private CardView cardComp1;
    private CardView cardComp2;
    private CardView cardComp3;

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

    public CardView getCardMine1() {
        return cardMine1;
    }

    public void setCardMine1(CardView cardMine1) {
        this.cardMine1 = cardMine1;
    }

    public CardView getCardMine2() {
        return cardMine2;
    }

    public void setCardMine2(CardView cardMine2) {
        this.cardMine2 = cardMine2;
    }

    public CardView getCardMine3() {
        return cardMine3;
    }

    public void setCardMine3(CardView cardMine3) {
        this.cardMine3 = cardMine3;
    }

    public CardView getCardComp1() {
        return cardComp1;
    }

    public void setCardComp1(CardView cardComp1) {
        this.cardComp1 = cardComp1;
    }

    public CardView getCardComp2() {
        return cardComp2;
    }

    public void setCardComp2(CardView cardComp2) {
        this.cardComp2 = cardComp2;
    }

    public CardView getCardComp3() {
        return cardComp3;
    }

    public void setCardComp3(CardView cardComp3) {
        this.cardComp3 = cardComp3;
    }
}

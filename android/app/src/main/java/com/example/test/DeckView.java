package com.example.test;

import java.util.ArrayList;

public class DeckView {
    private int id;
    private int d_id;
    private String d_name;
    private ArrayList<MyCardView> cards;

    public DeckView(int d_id, String d_name) {
        this.d_id = d_id;
        this.d_name = d_name;
        this.cards = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getD_id() {
        return d_id;
    }

    public void setD_id(int d_id) {
        this.d_id = d_id;
    }

    public String getD_name() {
        return d_name;
    }

    public void setD_name(String d_name) {
        this.d_name = d_name;
    }

    public ArrayList<MyCardView> getCards() {
        return cards;
    }

    public void setCards(ArrayList<MyCardView> cards) {
        this.cards = cards;
    }

    public void addCard(MyCardView card) {
        cards.add(card);
    }
}

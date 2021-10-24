package com.tema1.Player;

import com.tema1.goods.Goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player {
    private int id;
    private final int money = 80;
    private final int cards = 8;
    private boolean isSheriff;
    private int allMoney = 0;
    private int NumberOfRound = 0;
    private boolean bribe;
    public boolean IsVerified = false;
    public Integer declared = -1;
    public Integer bribeMoney = 0;
    private ArrayList<Integer> cardsInHands = new ArrayList<Integer>();
    private HashMap<Integer, Integer> Bag = new HashMap<Integer, Integer>();
    private HashMap<Integer, Integer> Stand = new HashMap<Integer, Integer>();

    public Player(int id, boolean issheriff, boolean bribe) {
        this.id = id;
        this.isSheriff = issheriff;
        this.bribe = bribe;
    }

    public Player(int id, boolean isSheriff, int allMoney, ArrayList<Integer> cardsInHands, HashMap<Integer, Integer> bag, HashMap<Integer, Integer> stand) {
        this.id = id;
        this.isSheriff = isSheriff;
        this.allMoney = allMoney;
        this.cardsInHands = cardsInHands;
        Bag = bag;
        Stand = stand;
    }

    public boolean getBribe() { return bribe; }

    public Integer getMoney() {
        return money;
    }

    public Integer getGetNumberOfRounds() {
        return NumberOfRound;
    }

    public void setNumberOfRounds(int NumberOfRound) { this .NumberOfRound = NumberOfRound; }

    public void setBag(HashMap<Integer, Integer> Bag) { this.Bag = Bag; }

    public void setStand(HashMap<Integer, Integer> Stand) { this.Stand = Stand; }

    public void setCards(ArrayList<Integer> cardsInHands) { this.cardsInHands = cardsInHands; }

    public Integer getId() {
        return id;
    }

    public void setAllMoney(int allMoney) {
        this.allMoney = allMoney;
    }

    public Integer getAllMoney() {
        return allMoney;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Integer, Integer> getStand() {
        return Stand;
    }

    public boolean isIssheriff() {
        return isSheriff;
    }

    public void setIssheriff(boolean issheriff) {
        this.isSheriff = issheriff;
    }

    public ArrayList<Integer> getCardsInHands() {
        return cardsInHands;
    }

    public HashMap<Integer, Integer> getBag() {
        return Bag;
    }

    public void getCardsInHand(List<Integer> totalCards) {
        if (totalCards.size() >= 10) {
            for (int i = 0; i <= 9; ++i) {
                getCardsInHands().add(totalCards.get(i));
            }
            for (Integer i : getCardsInHands()) {
                totalCards.remove(i);
            }
        }
    }

    public void putCardsInBag(Map<Integer, Goods> myHash, boolean doesTheSheriff) {}

    public void putCardsInStand() {
        if (!isIssheriff()) {
            for (Integer key : Bag.keySet()) {
                if (!Stand.containsKey(key)) {
                    Stand.put(key, Bag.get(key));
                } else {
                    int counter = Stand.get(key);
                    Stand.put(key, counter + Bag.get(key));
                }
            }
        }
    }

    public void calculateAllMoney(Map<Integer, Goods> myGoods) {
        for(Integer key : Stand.keySet()) {
            setAllMoney(getAllMoney() + Stand.get(key) * myGoods.get(key).getProfit());
        }
        setAllMoney(getAllMoney() + getMoney());
    }

    public void  doTheSheriff(List<Integer> assets, Player player, Map<Integer, Goods> myGoods,
                              ArrayList<Player> myPlayers) {}

}

package com.tema1.Player;

import com.tema1.goods.Goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Greedy extends Player {

    public Greedy(int id, boolean issheriff, boolean bribe) { super(id, issheriff, bribe); }

    public void playAsBasicForBag(Map<Integer, Goods> myHash, boolean doesTheSheriff) {
        Basic Player = new Basic(this.getId(), this.isIssheriff(), this.getAllMoney(), this.getCardsInHands(),
                this.getBag(), this.getStand());
        Player.putCardsInBag(myHash, doesTheSheriff);
        this.setAllMoney(Player.getAllMoney());
        this.setBag(Player.getBag());
        this.setCards(Player.getCardsInHands());
        this.setStand(Player.getStand());
        declared = Player.declared;
    }

    public void deleteCardsWhichAreInBag() {
        ArrayList<Integer> forRemove = new ArrayList<Integer>();
        for (Integer key : getBag().keySet()) {
            for (Integer iterator2 : getCardsInHands()) {
                if (key == iterator2) {
                    forRemove.add(iterator2);
                    break;
                }
            }
        }
        for (Integer iterator : forRemove) {
            getCardsInHands().remove(iterator);
        }
    }

    public Integer findMaxIllegal(ArrayList<Integer> cardsInHand, Map<Integer, Goods> myGoods) {
        ArrayList<Integer> Illegal = new ArrayList<Integer>();
        for(Integer iterator : cardsInHand) {
            if(iterator > 10) {
                Illegal.add(iterator);
            }
        }
        Integer IllegalCard = -1;
        Integer maxProfit = -1;
        for(Integer iterator : Illegal) {
            if(myGoods.get(iterator).getProfit() > maxProfit) {
                maxProfit = myGoods.get(iterator).getProfit();
                IllegalCard = iterator;
            }
        }
        return IllegalCard;
    }

    public boolean isAnyIllegalCard(ArrayList<Integer> myCards) {
        for(Integer iterator : myCards) {
            if(iterator > 10) {
                return true;
            }
        }
        return false;
    }

    public void putCardsInBag(Map<Integer, Goods> myHash, boolean doesTheSheriff) {
        if(this.getGetNumberOfRounds() % 2 != 0) {
           playAsBasicForBag(myHash, doesTheSheriff);
        } else {
            if (isAnyIllegalCard(getCardsInHands())) {
                playAsBasicForBag(myHash, doesTheSheriff);
                if (getBag().size() < 8) {
                    deleteCardsWhichAreInBag();
                    Integer IllegalCard = findMaxIllegal(getCardsInHands(), myHash);
                    if (getBag().containsKey(IllegalCard)) {
                        Integer counter = getBag().get(IllegalCard);
                        getBag().put(IllegalCard, counter + 1);
                    } else {
                        getBag().put(IllegalCard, 1);
                    }
                }
            } else {
                playAsBasicForBag(myHash, doesTheSheriff);
            }
        }
    }

    public void playAsBasicForSheriff(Map<Integer, Goods> myHash, List<Integer> assets, Player player,
                                      ArrayList<Player> myPlayer) {
        Basic Player = new Basic(this.getId(), this.isIssheriff(), this.getAllMoney(), this.getCardsInHands(),
                this.getBag(), this.getStand());
        Player.doTheSheriff(assets, player, myHash, myPlayer);
        this.setAllMoney(Player.getAllMoney());
        this.setBag(Player.getBag());
        this.setCards(Player.getCardsInHands());
        this.setStand(Player.getStand());
    }

    public void doTheSheriff(List<Integer> assets, Player player, Map<Integer,
            Goods> myGoods, ArrayList<Player> myPlayer) {
        if(player.bribeMoney == 0) {
            playAsBasicForSheriff(myGoods, assets, player, myPlayer);
        } else if (player.bribeMoney != 0) {
            this.setAllMoney(this.getAllMoney() + player.bribeMoney);
            player.bribeMoney = 0;
        }
    }
}

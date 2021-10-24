package com.tema1.Player;

import com.tema1.goods.Goods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Basic extends Player {

    public Basic(int id, boolean issheriff, boolean bribe) {
        super(id, issheriff, bribe);
    }

    public Basic(int id, boolean isSheriff, int allMoney, ArrayList<Integer> cardsInHands, HashMap<Integer, Integer> bag, HashMap<Integer, Integer> stand) {
        super(id, isSheriff, allMoney, cardsInHands, bag, stand);
    }

    public boolean areIllegal() {
        for (Integer i : getCardsInHands()) {
            if (i <= 10) {
                return false;
            }
        }
        return true;
    }

    public Integer findMaxPointinCard(ArrayList<Integer> cardsInHand, Map<Integer, Goods> myGoods) {
        int max = 0;
        int card = -1;
        for (Integer i : cardsInHand) {
            if (myGoods.get(i).getProfit() > max) {
                max = myGoods.get(i).getProfit();
                card = i;
            }
        }
        return card;
    }

    public Integer howManyTimes(ArrayList<Integer> myCards, int card) {
        int counter = 0;
        for (Integer i : myCards) {
            if (i.equals(card)) {
                counter += 1;
            }
        }
        return counter;
    }

    public void put(int Card, int times) {
        if (getBag().size() < 8) {
            if (!getBag().containsKey(Card)) {
                getBag().put(Card, times);
            } else {
                int counter = getBag().get(Card);
                getBag().put(Card, counter + times);
            }
        }
    }

    public void putIllegal(Map<Integer, Goods> myGoods) {
        int illegalCard = findMaxPointinCard(getCardsInHands(), myGoods);
        int times = 1;
        put(illegalCard, times);
        declared = 0;
    }

    public Integer findMax(Integer[] frequency) {
        int max = 0;
        for (int i = 0; i < frequency.length; ++i) {
            if (frequency[i] > max) {
                max = frequency[i];
            }
        }
        return max;
    }

    public void putLegal(Map<Integer, Goods> myGoods) {
        Integer[] frequency = new Integer[10];
        for (int i = 0; i < 10; ++i) {
            frequency[i] = howManyTimes(getCardsInHands(), i);
        }
        int maximallyCardApparition = findMax(frequency);
        int maxProfit = 0;
        int maximallyCard = -1;
        Integer[] cards = new Integer[10];
        Integer k = 0;
        for (int i = 0; i < frequency.length; ++i) {
            if (frequency[i] == maximallyCardApparition) {
                cards[k] = frequency[i];
                k += 1;
            }
            if (frequency[i] == maximallyCardApparition && maxProfit < myGoods.get(i).getProfit()) {
                maximallyCard = i;
                maxProfit = myGoods.get(i).getProfit();
            }
            if (frequency[i] == maximallyCardApparition && maxProfit == myGoods.get(i).getProfit()) {
                if (myGoods.get(i).getId() > myGoods.get(maximallyCard).getId()) {
                    maximallyCard = myGoods.get(i).getId();
                    maxProfit = myGoods.get(i).getProfit();
                }
            }
        }
        declared = maximallyCard;
        put(maximallyCard, maximallyCardApparition);
    }

    public void putCardsInBag(Map<Integer, Goods> myGoods, boolean doesTheSheriff) {
        if (areIllegal()) {
            putIllegal(myGoods);
        } else {
            putLegal(myGoods);
        }
    }

    public boolean isKind(HashMap<Integer, Integer> myHas) {
        for (Integer key : myHas.keySet()) {
            if (key > 10) {
                return false;
            }
        }
        return true;
    }

    public void doTheSheriff(List<Integer> assets, Player player, Map<Integer, Goods> myGoods,
                             ArrayList<Player> myPlayers) {
        ArrayList<Integer> forRemove = new ArrayList<Integer>();
        if (player.bribeMoney != 0) {
            player.setAllMoney(player.getAllMoney() + player.bribeMoney);
            player.bribeMoney = 0;
        }
        if(this.getAllMoney() + 80 > 16) {
            if (isKind(player.getBag())) {
                for (Integer key : player.getBag().keySet()) {
                    setAllMoney(getAllMoney() - myGoods.get(key).getPenalty() * player.getBag().get(key));
                    player.setAllMoney(player.getAllMoney() + myGoods.get(key).getPenalty() * player.getBag().get(key));
                }
            } else {
                for (Integer key : player.getBag().keySet()) {
                    if (key > 10 || key != player.declared) {
                        setAllMoney(getAllMoney() + myGoods.get(key).getPenalty() * player.getBag().get(key));
                        player.setAllMoney(player.getAllMoney() - myGoods.get(key).getPenalty() * player.getBag().get(key));
                        assets.add(key);
                        forRemove.add(key);
                    }
                }
                for (Integer i : forRemove) {
                    if (player.getBag().containsKey(i)) {
                        player.getBag().remove(i);
                    }
                }
            }
        }
    }
}

package com.tema1.Player;

import com.tema1.goods.Goods;

import java.util.*;

public class Bribe extends Player {

    public Bribe(int id, boolean issheriff, boolean bribe) {
        super(id, issheriff, bribe);
    }

    public HashMap<Integer, Integer> putIllegalGood() {
        HashMap<Integer, Integer> IllegalGoodsHash = new HashMap<Integer, Integer>();
        for(Integer illegalGood : getCardsInHands()) {
            if(illegalGood > 10) {
                if(IllegalGoodsHash.containsKey(illegalGood)) {
                    Integer counter = IllegalGoodsHash.get(illegalGood);
                    IllegalGoodsHash.put(illegalGood, counter + 1);
                } else {
                    IllegalGoodsHash.put(illegalGood, 1);
                }
            }
        }
        return IllegalGoodsHash;
    }

    public void putInVectors(ArrayList<Integer> frequency, ArrayList<Integer> Goods,
                             HashMap<Integer, Integer> IllegalGoods) {
        for(Integer key : IllegalGoods.keySet()) {
            Goods.add(key);
            frequency.add(IllegalGoods.get(key));
        }
    }

    public void sortVectors(ArrayList<Integer> frequency, Map<Integer, Goods> myHash, ArrayList<Integer> Goods) {
        for(Integer i = 0; i < frequency.size() - 1; ++i) {
            for(Integer j = i + 1; j < frequency.size(); ++j) {
                if(myHash.get(Goods.get(i)).getProfit() < myHash.get(Goods.get(j)).getProfit()) {
                    Collections.swap(frequency, i, j);
                    Collections.swap(Goods, i, j);
                }
                if(myHash.get(Goods.get(i)).getProfit() == myHash.get(Goods.get(j)).getProfit()) {
                    if(myHash.get(frequency.get(i)).getId() < myHash.get(frequency.get(j)).getId()) {
                        Collections.swap(frequency, i, j);
                        Collections.swap(Goods, i, j);
                    }
                }
            }
        }
    }

    public boolean IsAnyIllegal(ArrayList<Integer> cardsInHand) {
        for(Integer iterator : cardsInHand) {
            if(iterator > 10) {
                return true;
            }
        }
        return false;
    }

    public void putInBag( ArrayList<Integer> myBag) {
       for(Integer iterator : myBag) {
           if(getBag().containsKey(iterator)) {
               Integer counter = getBag().get(iterator);
               getBag().put(iterator, counter + 1);
           } else {
               getBag().put(iterator, 1);
           }
       }
    }

    public void deleteCards(ArrayList<Integer> Goods, Integer position) {
        for(Iterator<Integer> iterator = getCardsInHands().iterator(); iterator.hasNext();) {
            Integer val = iterator.next();
            if(val == Goods.get(position)) {
                iterator.remove();
            }
        }
    }

    public void playAsBasic(Map<Integer, Goods> myHash, boolean doesTheSheriff) {
        Basic Player = new Basic(this.getId(), this.isIssheriff(), this.getAllMoney(), this.getCardsInHands(),
                this.getBag(), this.getStand());
        Player.putCardsInBag(myHash, doesTheSheriff);
        this.setAllMoney(Player.getAllMoney());
        this.setBag(Player.getBag());
        this.setCards(Player.getCardsInHands());
        this.setStand(Player.getStand());
        declared = Player.declared;
    }

    public void putLegalCards(ArrayList<Integer> myCards, ArrayList<Integer> myBag, Map<Integer, Goods> myGoods, Integer AllMoney) {
        for(Integer i = 0; i < myCards.size() - 1; ++i) {
            for(Integer j = i + 1; j < myCards.size(); ++j) {
                if(myGoods.get(myCards.get(i)).getProfit() < myGoods.get(myCards.get(j)).getProfit()) {
                    Collections.swap(myCards, i, j);
                }
                if(myGoods.get(myCards.get(i)).getProfit() == myGoods.get(myCards.get(j)).getProfit()) {
                    if(myGoods.get(myCards.get(i)).getId() < myGoods.get(myCards.get(j)).getId()) {
                        Collections.swap(myCards, i, j);
                    }
                }
            }
        }
        for(Integer i : myCards) {
            if(myBag.size() < 8 && i < 10 && (AllMoney > 2)) {
                myBag.add(i);
                AllMoney -= 2;
            }
        }
    }

    public Integer max(Integer numero1, Integer numero2) {
        if(numero1 <= numero2) {
            return numero2;
        } else {
            return numero1;
        }
    }
    public void putCardsInBag(Map<Integer, Goods> myHash, boolean doesTheSheriff) {
        if (IsAnyIllegal(getCardsInHands())) {
            HashMap<Integer, Integer> IllegalGoods = putIllegalGood();
            ArrayList<Integer> frequency = new ArrayList<Integer>(IllegalGoods.size());
            ArrayList<Integer> Goods = new ArrayList<Integer>(IllegalGoods.size());
            ArrayList<Integer> myBag = new ArrayList<Integer>();

            final Integer requiredMinimalBribe = 5;
            final Integer requiredMaximalBribe = 10;

            putInVectors(frequency, Goods, IllegalGoods);
            sortVectors(frequency, myHash, Goods);
            Integer penalty = 0;
            Integer AllMoney = this.getAllMoney() + 80;
            Integer HowManyCard = 0;
            for(Integer i =0 ; i < frequency.size(); ++i) {
                Integer counter = frequency.get(i);
                while ((AllMoney > myHash.get(Goods.get(i)).getPenalty()) && (myBag.size() < 8) && (counter > 0)) {
                    myBag.add(Goods.get(i));
                    deleteCards(Goods, i);
                    counter -= 1;
                    AllMoney -= myHash.get(Goods.get(i)).getPenalty();
                    penalty += myHash.get(Goods.get(i)).getPenalty();
                    HowManyCard += 1;
                }
            }
            if (HowManyCard > 2) {
                bribeMoney = requiredMaximalBribe;

            } else if (HowManyCard > 0) {
                bribeMoney = requiredMinimalBribe;
            }
            System.out.println(myBag);
            System.out.println(getCardsInHands());
            if (this.getAllMoney() + 80 > 4) {
                if(this.getAllMoney() + 80 - requiredMinimalBribe > 0) {
                    putLegalCards(getCardsInHands(), myBag, myHash, AllMoney);
                    putInBag(myBag);
                    declared = 0;
                    this.setAllMoney(this.getAllMoney() - bribeMoney);
                } else {
                    playAsBasic(myHash, doesTheSheriff);
                    bribeMoney = 0;
                }
            } else {
                this.setAllMoney(this.getAllMoney() + bribeMoney);
                playAsBasic(myHash, doesTheSheriff);
            }
        } else {
            playAsBasic(myHash, doesTheSheriff);
            declared = -1;
        }
    }

    public void playAsSHeriff(ArrayList<Player> myPlayers, List<Integer> assets,
                              Map<Integer, Goods> myGoods, Player playerNew) {
        Basic Player = new Basic(this.getId(), this.isIssheriff(), this.getAllMoney(), this.getCardsInHands(),
                this.getBag(), this.getStand());
        Player.doTheSheriff(assets, playerNew, myGoods, myPlayers);
        this.setAllMoney(Player.getAllMoney());
        this.setBag(Player.getBag());
        this.setCards(Player.getCardsInHands());
        this.setStand(Player.getStand());
    }

    public void  doTheSheriff(List<Integer> assets, Player player, Map<Integer, Goods> myGoods,
                              ArrayList<Player> myPlayers) {
        if(myPlayers.size() == 2) {
            playAsSHeriff(myPlayers, assets, myGoods, player);
            player.IsVerified = true;
        } else {
            if(this.getId() == 1) {
                if(player.getId() == 2) {
                    playAsSHeriff(myPlayers, assets, myGoods, player);
                    player.IsVerified = true;
                } else if(player.getId() == myPlayers.size()) {
                    playAsSHeriff(myPlayers, assets, myGoods, player);
                    player.IsVerified = true;
                }
            } else if(this.getId() == myPlayers.size()) {
                    if(player.getId() == myPlayers.size() - 1) {
                        playAsSHeriff(myPlayers, assets, myGoods, player);
                        player.IsVerified = true;
                    } else if(player.getId() == 1) {
                        playAsSHeriff(myPlayers, assets, myGoods, player);
                        player.IsVerified = true;
                    }
                } else {
                if(player.getId() == this.getId() + 1) {
                    playAsSHeriff(myPlayers, assets, myGoods, player);
                    player.IsVerified = true;
                } else {
                    if(player.getId() == this.getId() - 1) {
                        playAsSHeriff(myPlayers, assets, myGoods, player);
                        player.IsVerified = true;
                    }
                }
            }
        }
    }
}

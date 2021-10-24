package com.tema1.main;

import com.tema1.Player.Basic;
import com.tema1.Player.Bribe;
import com.tema1.Player.Greedy;
import com.tema1.Player.Player;
import com.tema1.goods.Goods;
import com.tema1.goods.GoodsFactory;
import com.tema1.goods.IllegalGoods;
import com.tema1.goods.LegalGoods;

import java.util.*;

public class GameEngine {

    public void putCardsInHands(ArrayList<Player> myPlayers, List<Integer> totalCards)  {
        for (Player player : myPlayers) {
            if(!player.isIssheriff()) {
                player.getCardsInHand(totalCards);
            }
        }
    }

    private ArrayList<Player> getAllPlayers(List<String> playersOrders) {
        ArrayList<Player> myPlayers = new ArrayList<Player>();
        int i = 0;
        for(String player : playersOrders) {
            if(player.equals("basic")) {
                Player myPlayer = new Basic(i + 1, false, false);
                myPlayers.add(myPlayer);
            }
            if(player.equals("bribed")) {
                Player myPlayer = new Bribe(i + 1, false, true);
                myPlayers.add(myPlayer);
            }
            if(player.equals("greedy")) {
                Player myPlayer = new Greedy(i + 1, false, false);
                myPlayers.add(myPlayer);
            }
            i += 1;
        }
        return myPlayers;
    }

    public void putCardsinBag(ArrayList<Player> myPlayers, Map<Integer, Goods> myGoods, boolean sheriffBribe) {
        for(Player player : myPlayers) {
            if(!player.isIssheriff()) {
                player.putCardsInBag(myGoods, sheriffBribe);
            }
        }
    }

    public void calculateAllMoney(ArrayList<Player> myPlayers, Map<Integer, Goods> myGoods) {
        for(Player player : myPlayers) {
            player.calculateAllMoney(myGoods);
        }
    }

    public void calculateKingBonus(ArrayList<Player> myPlayer, GoodsFactory Factory) {
        for (int i = 0; i < 10; ++i) {
            Integer k = 0;
            Integer frequency[] = new Integer[10];
            Integer playerOrder[] = new Integer[10];
            Integer Assets[] = new Integer[10];
            for (Player player : myPlayer) {
                for (Integer key : player.getStand().keySet()) {
                    if (key == i) {
                        frequency[k] = player.getStand().get(key);
                        playerOrder[k] = player.getId();
                        Assets[k] = key;
                        k += 1;
                    }
                }
            }
            if (frequency.length > 0) {
                for (Integer l = 0; l < k - 1; ++l) {
                    for (Integer j = l + 1; j < k; ++j) {
                        if (frequency[l] < frequency[j]) {
                            Integer aux = frequency[l];
                            frequency[l] = frequency[j];
                            frequency[j] = aux;
                            aux = playerOrder[l];
                            playerOrder[l] = playerOrder[j];
                            playerOrder[j] = aux;
                            aux = Assets[l];
                            Assets[l] = Assets[j];
                            Assets[j] = aux;
                        }
                    }
                }
            }
            for (Integer p = 0; p < k - 1; ++p) {
                for (Integer h = p + 1; h < k; ++h) {
                    if (frequency[p] == frequency[h]) {
                        if (playerOrder[p] > playerOrder[h]) {
                            Integer aux = playerOrder[p];
                            playerOrder[p] = playerOrder[h];
                            playerOrder[h] = aux;
                        }
                    }
                }
            }

            if (playerOrder[0] != null) {
                for (Player newPlayer : myPlayer) {
                    if (newPlayer.getId() == playerOrder[0]) {
                        newPlayer.setAllMoney(newPlayer.getAllMoney() +
                                ((LegalGoods) Factory.getGoodsById(Assets[0])).getKingBonus());
                    } else if (playerOrder[1] != null) {
                        if (newPlayer.getId() == playerOrder[1]) {
                            newPlayer.setAllMoney(newPlayer.getAllMoney() +
                                    ((LegalGoods) Factory.getGoodsById(Assets[1])).getQueenBonus());
                        }
                    }
                }
            }
        }
    }

    public void calculateIllegalBonus(ArrayList<Player> myPlayer, GoodsFactory Factory){
        for(Player player : myPlayer) {
            Integer profitInMOrtiimatii = 0;
            HashMap<Integer, Integer> auxiliar = new HashMap<Integer, Integer>();
            for(Integer key : player.getStand().keySet()) {
                if(key > 10) {
                    for(Goods good : ((IllegalGoods)Factory.getGoodsById(key)).getIllegalBonus().keySet()) {
                        profitInMOrtiimatii += ((IllegalGoods)Factory.getGoodsById(key)).getIllegalBonus().get(good) *
                                good.getProfit() * player.getStand().get(key);

                        if(auxiliar.containsKey(good.getId())) {
                            Integer counter = auxiliar.get(good.getId());
                            auxiliar.put(good.getId(), ((IllegalGoods)Factory.getGoodsById(key)).getIllegalBonus().get(good) * player.getStand().get(key) + counter);
                        } else {
                            auxiliar.put(good.getId(), ((IllegalGoods)Factory.getGoodsById(key)).getIllegalBonus().get(good) * player.getStand().get(key));
                        }
                    }
                }
            }
            for(Integer key : auxiliar.keySet()) {
                if(player.getStand().containsKey(key)) {
                    Integer aux = player.getStand().get(key);
                    Integer aux2 = auxiliar.get(key);
                    player.getStand().put(key, aux + aux2);
                } else {
                    player.getStand().put(key, auxiliar.get(key));
                }
            }
            player.setAllMoney(player.getAllMoney() + profitInMOrtiimatii);
        }
    }

    public void deleteAllTheCardsFromHand(ArrayList<Player> myPlayers) {
        for(Player player : myPlayers) {
            if(!player.isIssheriff()) {
                ArrayList<Integer> newArray = player.getCardsInHands();
                player.getCardsInHands().removeAll(newArray);
            }
        }
    }

    public void deleteAllTheCardsFromBag(ArrayList<Player> myPlayers) {
        for(Player player : myPlayers) {
            if(!player.isIssheriff()) {
                List<Integer> toRemove = new ArrayList<>();
                Set<Integer> keys = player.getBag().keySet();
                for(int iter : keys) {
                    toRemove.add(iter);
                }
                for(Integer key : toRemove) {
                    player.getBag().remove(key);
                }
                keys.removeAll(toRemove);
            }
        }
    }

    public void increaseNumberOfRounds(ArrayList<Player> myPlayers, int round) {
        for(Player player : myPlayers) {
            player.setNumberOfRounds(round + 1);
        }
    }

    public ArrayList<Integer> putTheLeaderBoard(ArrayList<Player> myPlayers) {
        ArrayList<Integer> LeaderBoard = new ArrayList<Integer>();
        for(Player player : myPlayers) {
            LeaderBoard.add(player.getAllMoney());
        }
        return LeaderBoard;
    }

    public boolean doesTheSheriffBribe(ArrayList<Player> myPlayers) {
        for(Player player : myPlayers) {
            if(player.isIssheriff()) {
                if(player instanceof Greedy) {
                    return true;
                }
            }
        }
        return false;
    }

    public void StartGame(Integer numberOfRounds, List<Integer> totalGoods,
                           List<String> totalPlayers, Map<Integer, Goods> myGoods, GoodsFactory Factory) {
        ArrayList<Player> myPlayers = getAllPlayers(totalPlayers);
        for(int i = 0; i < numberOfRounds; ++i) {
            increaseNumberOfRounds(myPlayers, i);

            for(Player player : myPlayers) {
                player.setIssheriff(true);
                putCardsInHands(myPlayers, totalGoods);
                putCardsinBag(myPlayers, myGoods, doesTheSheriffBribe(myPlayers));
                System.out.println("RUNDA " + i);
                for(Player newPlayer : myPlayers) {
                    if(!newPlayer.equals(player)) {
                        player.doTheSheriff(totalGoods, newPlayer, myGoods, myPlayers);
                        newPlayer.putCardsInStand();
                    }
                }
                if(player.getBribe()) {
                    for(Player nPlayer : myPlayers) {
                        if(nPlayer.bribeMoney != 0) {
                            player.setAllMoney(player.getAllMoney() + nPlayer.bribeMoney);
                            nPlayer.bribeMoney = 0;
                        }
                    }
                }
                System.out.println("MONEY");
                for(Player player1 : myPlayers) {
                    int aux = player1.getAllMoney() + 80;
                    System.out.println( player1.getId() + " " + aux);
                }
                deleteAllTheCardsFromHand(myPlayers);
                deleteAllTheCardsFromBag(myPlayers);
                player.setIssheriff(false);
            }
        }
        calculateAllMoney(myPlayers, myGoods);
        System.out.println("MONEY");
        for(Player player1 : myPlayers) {
            System.out.println( player1.getId() + " " + player1.getAllMoney());
        }
        calculateIllegalBonus(myPlayers, Factory);
        calculateKingBonus(myPlayers, Factory);
        ArrayList<Integer> LeaderBoard = putTheLeaderBoard(myPlayers);
        ArrayList<Integer> Ids = new ArrayList<Integer>();
        Integer k = 0;
        for(Integer i : LeaderBoard) {
            Ids.add(k);
            k++;
        }
        for(Integer i = 0; i < LeaderBoard.size() - 1; ++i) {
            for(Integer j = i + 1; j < LeaderBoard.size(); ++j) {
                if(LeaderBoard.get(i) < LeaderBoard.get(j)) {
                    Collections.swap(LeaderBoard, i, j);
                    Collections.swap(Ids, i, j);
                }
            }
        }
        for(Integer i = 0; i < LeaderBoard.size(); ++i) {
            System.out.println(Ids.get(i) + " " + LeaderBoard.get(i));
        }
    }
}

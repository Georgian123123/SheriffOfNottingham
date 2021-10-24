package com.tema1.main;

import com.tema1.goods.GoodsFactory;

public final class Main {
    private Main() {
        // just to trick checkstyle
    }

    public static void main(final String[] args) {
        GameInputLoader gameInputLoader = new GameInputLoader(args[0], args[1]);
        GameInput gameInput = gameInputLoader.load();
        //TODO implement homework logic
        GameEngine myGame = new GameEngine();
        GoodsFactory myGoods = GoodsFactory.getInstance();
        myGame.StartGame(gameInput.getRounds(), gameInput.getAssetIds(), gameInput.getPlayerNames(), myGoods.getAllGoods(), myGoods);
    }
}
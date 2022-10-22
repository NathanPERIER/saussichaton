package fr.nperier.saussichaton.engine;

public enum GameState {
    INIT(null);

    private final Class<? extends StateAction> gsClass;

    GameState(Class<? extends StateAction> gsClass) {
        this.gsClass = gsClass;
    }

    public Class<? extends StateAction> getGameState() {
        return gsClass;
    }

}

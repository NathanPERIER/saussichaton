package fr.nperier.saussichaton.engine;

public enum State {
    INIT(null);

    private final Class<? extends GameState> gsClass;

    State(Class<? extends GameState> gsClass) {
        this.gsClass = gsClass;
    }

    public Class<? extends GameState> getGameState() {
        return gsClass;
    }

}

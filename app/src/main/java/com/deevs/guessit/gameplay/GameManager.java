package com.deevs.guessit.gameplay;

import android.content.Context;
import android.content.SharedPreferences;

public enum GameManager {
    INSTANCE;

    private static final String GAME_PREF_FILE_NAME = "GamePreferences";
    private static final String GAME_PREF_KEY_STATE = "GameStatePref";

    public enum STATE {
        NOT_IN_GAME,
        CREATING_LOBBY,
        IN_GAME_PHRASE_SELECTION,
        IN_GAME_ACTING,
        IN_GAME_GUESSING,
        IN_GAME_WAITING,
        GAME_OVER
    }

    private STATE mGameState;

    private GameManager() {
        mGameState = STATE.NOT_IN_GAME;
    }

    public void setGameState(final Context context, final STATE curState) {
        mGameState = curState;
        saveGameStateToSharedPrefs(context);
    }

    public STATE getGameState() {
        return mGameState;
    }

    public void saveGameStateToSharedPrefs(final Context context) {
        final SharedPreferences prefs =
                context.getApplicationContext().getSharedPreferences(GAME_PREF_FILE_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(GAME_PREF_KEY_STATE, mGameState.toString());
    }

    /**
     * Returns the game state if it was previously saved, null otherwise
     *
     * @param context: a valid application context.
     * @return String: The current game state if saved, null if there is no game
     * */
    public String getSavedGameState(final Context context) {
        return context.getApplicationContext()
                .getSharedPreferences(GAME_PREF_FILE_NAME, Context.MODE_PRIVATE)
                .getString(GAME_PREF_KEY_STATE, STATE.NOT_IN_GAME.toString());
    }
}

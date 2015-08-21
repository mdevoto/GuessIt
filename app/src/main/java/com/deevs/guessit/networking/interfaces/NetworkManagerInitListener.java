package com.deevs.guessit.networking.interfaces;

public interface NetworkManagerInitListener {
    void initSuccess();
    void initFailure(final String errorMsg);
}

package ru.traiwy.storage.database.effect;

import ru.traiwy.data.EffectData;

import java.util.concurrent.CompletableFuture;

public interface StorageEffect {

    CompletableFuture<EffectData> getEffectPVP(int clanId);
    void addEffectPVP(EffectData effect);
    void removeEffectPVP(int id);
    void updateEffectPVP(EffectData effect);

     CompletableFuture<EffectData> getEffectPVE(int clanId);
    void addEffectPVE(EffectData effect);
    void removeEffectPVE(int id);
    void updateEffectPVE(EffectData effect);
}

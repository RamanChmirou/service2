package com.mpie.service2;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

public class KeySecretStore {

    private ConcurrentHashMap<String, String> storageString = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, String> storageInteger = new ConcurrentHashMap<>();

    public String storage(String key, String secret) {
        validateKeyConstraints(key);
        validateSecret(secret);
        isAllowedToAdd(storageString, key.toLowerCase());
        storageString.put(key.toLowerCase(), secret);
        return key;
    }

    public Integer storage(Integer key, String secret) {
        validateSecret(secret);
        isAllowedToAdd(storageInteger, key);
        storageInteger.put(key, secret);
        return key;
    }

    public Optional<String> retrieve(String key) {
        return ofNullable(storageString.get(key.toLowerCase()));
    }

    public Optional<String> retrieve(Integer key) {
        return ofNullable(storageInteger.get(key));
    }

    private void validateKeyConstraints(String key) {
        if (key == null || key.isEmpty() || key.length() > 20) {
            throw new IllegalArgumentException("Key cannot be null, empty. or longer than 20 characters");
        }
    }

    private void validateSecret(String secret) {
        if (secret == null || secret.isEmpty()) {
            throw new IllegalArgumentException("Secret cannot be null, or empty");
        }
    }

    private <T> void isAllowedToAdd(ConcurrentHashMap<T, String> storage, T key) {
        if (storage.containsKey(key)) {
            throw new IllegalArgumentException(String.format("Key is duplicated: %s", key));
        }
    }

}

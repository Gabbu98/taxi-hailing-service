package com.pickmeup.taxi_service.configurations.transactions;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/* A buffer for transactions which are ready to be committed or rolledback
    - Each transaction has its own TransactionalContext
    - on commit -> flush staged changes into the real map
    - on rollback -> discard changes
 */
public class TransactionalContext {
    private final Map<ConcurrentHashMap<?,?>, Map<Object,Object>> stagedWrites = new HashMap<>();
    private final Map<ConcurrentHashMap<?,?>, Set<Object>> stagedDeletes = new HashMap<>();


    public <K,V> List<V> values(ConcurrentHashMap<K,V> store) {
        Map<K,V> snapshot = new HashMap<>(store);

        if (stagedDeletes.containsKey(store)) {
            for (Object key : stagedDeletes.get(store)) {
                snapshot.remove(key);
            }
        }

        if (stagedWrites.containsKey(store)) {
            for (Map.Entry<Object,Object> e : stagedWrites.get(store).entrySet()) {
                snapshot.put((K) e.getKey(), (V) e.getValue());
            }
        }

        return new ArrayList<>(snapshot.values());
    }

    public <K,V> V get(ConcurrentHashMap<K,V> store, K key) {
        if (stagedWrites.containsKey(store) && stagedWrites.get(store).containsKey(key)) {
            return (V) stagedWrites.get(store).get(key);
        }
        if (stagedDeletes.containsKey(store) && stagedDeletes.get(store).contains(key)) {
            return null;
        }
        return store.get(key);
    }

    public <K,V> void put(ConcurrentHashMap<K,V> store, K key, V value) {
        stagedWrites.computeIfAbsent(store, s -> new HashMap<>()).put(key, value);
        stagedDeletes.computeIfAbsent(store, s -> new HashSet<>()).remove(key);
    }

    public <K,V> void remove(ConcurrentHashMap<K,V> store, K key) {
        stagedDeletes.computeIfAbsent(store, s -> new HashSet<>()).add(key);
        stagedWrites.computeIfAbsent(store, s -> new HashMap<>()).remove(key);
    }

    public void commit() {
        stagedWrites.forEach((store, entries) -> entries.forEach((k, v) -> ((ConcurrentHashMap<Object,Object>) store).put(k, v)));
        stagedDeletes.forEach((store, keys) -> keys.forEach(((ConcurrentHashMap<Object,Object>) store)::remove));
    }

    public void rollback() {
        stagedWrites.clear();
        stagedDeletes.clear();
    }
}


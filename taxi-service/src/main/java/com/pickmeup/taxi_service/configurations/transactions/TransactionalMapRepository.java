package com.pickmeup.taxi_service.configurations.transactions;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionalMapRepository<K, V> {
    private final ConcurrentHashMap<K, V> store = new ConcurrentHashMap<>();

    // returns staged value if inside tx
    public V get(K key) {
        TransactionalContext ctx = TransactionManager.getCurrentTransaction();
        if (ctx != null) {
            return ctx.<K,V>get(store, key);
        }
        return store.get(key);
    }

    // stage or commit immediately
    public void put(K key, V value) {
        TransactionalContext ctx = TransactionManager.getCurrentTransaction();
        if (ctx != null) {
            ctx.put(store, key, value);
        } else {
            store.put(key, value);
        }
    }

    // stage or commit immediately
    public void remove(K key) {
        TransactionalContext ctx = TransactionManager.getCurrentTransaction();
        if (ctx != null) {
            ctx.remove(store, key);
        } else {
            store.remove(key);
        }
    }

    // returns results from staged writes / deletes
    public List<V> values() {
        TransactionalContext ctx = TransactionManager.getCurrentTransaction();
        if (ctx != null) {
            return ctx.values(store);
        }
        return List.copyOf(store.values());
    }
}


package com.pickmeup.taxi_service.configurations.transactions;

// A helper emulating Spring's TransactionSynchronizationManager, to commit or rollback transactions
public class TransactionManager {
    private static final ThreadLocal<TransactionalContext> currentTx = new ThreadLocal<>();

    public static void begin() {
        if (currentTx.get() != null) {
            throw new IllegalStateException("Transaction already active");
        }
        currentTx.set(new TransactionalContext());
    }

    public static void commit() {
        TransactionalContext ctx = currentTx.get();
        if (ctx == null) throw new IllegalStateException("No active transaction");
        ctx.commit();
        currentTx.remove();
    }

    public static void rollback() {
        TransactionalContext ctx = currentTx.get();
        if (ctx == null) throw new IllegalStateException("No active transaction");
        ctx.rollback();
        currentTx.remove();
    }

    public static TransactionalContext getCurrentTransaction() {
        return currentTx.get();
    }
}


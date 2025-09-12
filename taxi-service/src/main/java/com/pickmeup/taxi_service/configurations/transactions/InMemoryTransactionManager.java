package com.pickmeup.taxi_service.configurations.transactions;

import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

// implementation of how Spring  commits/rollbacks the in memory transactions when @Transactional is used, this is configured part of Spring's context
public class InMemoryTransactionManager extends AbstractPlatformTransactionManager {

    @Override
    protected Object doGetTransaction() throws TransactionException {
        // We don’t have transaction objects like JDBC does,
        // just return a marker object (could be a simple String or custom class)
        return new Object();
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
        TransactionManager.begin();
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
        try {
            TransactionManager.commit();
        } catch (Exception e) {
            throw new TransactionException("Commit failed", e) {};
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
        try {
            TransactionManager.rollback();
        } catch (Exception e) {
            throw new TransactionException("Rollback failed", e) {};
        }
    }
}



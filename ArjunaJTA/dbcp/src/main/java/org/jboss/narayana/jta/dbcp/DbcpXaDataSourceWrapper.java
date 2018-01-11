package org.jboss.narayana.jta.dbcp;

import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.managed.DataSourceXAConnectionFactory;
import org.apache.commons.dbcp2.managed.ManagedDataSource;
import org.apache.commons.pool2.impl.GenericObjectPool;

import javax.sql.DataSource;
import javax.sql.XADataSource;
import javax.transaction.TransactionManager;

/**
 * @author <a href="mailto:gytis@redhat.com">Gytis Trikleris</a>
 */
public class DbcpXaDataSourceWrapper {

    private final TransactionManager transactionManager;

    public DbcpXaDataSourceWrapper(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public DataSource wrap(XADataSource originalDataSource) {
        DataSourceXAConnectionFactory dataSourceXAConnectionFactory =
                new DataSourceXAConnectionFactory(transactionManager, originalDataSource);
        PoolableConnectionFactory poolableConnectionFactory =
                new PoolableConnectionFactory(dataSourceXAConnectionFactory, null);
        GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnectionFactory);
        return new ManagedDataSource<>(connectionPool, dataSourceXAConnectionFactory.getTransactionRegistry());
    }

}

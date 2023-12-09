package com.imhero.config.datasource

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.support.TransactionSynchronizationManager
import spock.lang.Specification

@ActiveProfiles(profiles = "test")
@SpringBootTest
class ReplicationRoutingSourceTest extends Specification {

    def "master data source"() {
        setup:
        def targetDataSources = [
                slave1: "slaveDataSource1",
                slave2: "slaveDataSource2",
                master: "masterDataSource"
        ]

        def routingSource = new ReplicationRoutingSource()
        routingSource.setTargetDataSources(targetDataSources)

        when:
        TransactionSynchronizationManager.setActualTransactionActive(true)
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(false)
        String currentLookupKey = routingSource.determineCurrentLookupKey().toString()

        then:
        currentLookupKey.startsWith(DataSourceType.MASTER.getName())

        cleanup:
        TransactionSynchronizationManager.clear()
    }

    def "slave data source"() {
        setup:
        def targetDataSources = [
                slave1: "slaveDataSource1",
                slave2: "slaveDataSource2",
                master: "masterDataSource"
        ]

        def routingSource = new ReplicationRoutingSource()
        routingSource.setTargetDataSources(targetDataSources)

        when:
        TransactionSynchronizationManager.setActualTransactionActive(true)
        TransactionSynchronizationManager.setCurrentTransactionReadOnly(true)
        String currentLookupKey = routingSource.determineCurrentLookupKey().toString()

        then:
        currentLookupKey.startsWith(DataSourceType.SLAVE.getName())

        cleanup:
        TransactionSynchronizationManager.clear()
    }
}

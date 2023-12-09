package com.imhero.config.datasource;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Profile("local")
@Slf4j
public class ReplicationRoutingSource extends AbstractRoutingDataSource {
    @Getter
    private SlaveNames<String> slaveNames;

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        List<String> slaveNames = targetDataSources.keySet()
                .stream()
                .map(Object::toString)
                .filter(str -> str.contains(DataSourceType.SLAVE.getName()))
                .collect(Collectors.toList());
        this.slaveNames = new SlaveNames<>(slaveNames);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        if (isReadOnly) {
            String nextSlaveName = slaveNames.getNext();
            return nextSlaveName;
        }
        return DataSourceType.MASTER.getName();
    }

    private static class SlaveNames<T> {
        private final List<T> values;
        private int index = 0;

        private SlaveNames(List<T> values) {
            this.values = values;
        }

        public T getNext() {
            if (index >= values.size() - 1) {
                index = -1;
            }
            return values.get(++index);
        }
    }
}

package com.vdobrikov.kafkaconsumer.processor.subprocessor;

import com.vdobrikov.kafkaconsumer.model.Employee;

@FunctionalInterface
public interface EmployeeSubProcessor extends Comparable<EmployeeSubProcessor> {
    Employee process(Employee employee);

    default int getOrder() {
        return 0;
    }

    @Override
    default int compareTo(EmployeeSubProcessor other) {
        return Integer.compare(getOrder(), other.getOrder());
    }
}

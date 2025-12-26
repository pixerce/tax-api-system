package com.example.tax.application.port.out;

import com.example.tax.domain.valueobject.TransactionRecord;

import java.util.List;

public interface TransactionRecordPort {
    void saveAll(List<TransactionRecord> record);
    void flush();
}
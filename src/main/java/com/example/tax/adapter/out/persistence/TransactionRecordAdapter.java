package com.example.tax.adapter.out.persistence;

import com.example.tax.application.port.out.TransactionRecordPort;
import com.example.tax.domain.valueobject.TransactionRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TransactionRecordAdapter implements TransactionRecordPort {

    @Override
    public void saveAll(List<TransactionRecord> record) {
    }

    @Override
    public void flush() {
    }

}

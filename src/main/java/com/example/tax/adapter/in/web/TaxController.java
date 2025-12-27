package com.example.tax.adapter.in.web;

import com.example.tax.application.VatCollectionCoordinator;
import com.example.tax.application.dto.DataCollectionRequest;
import com.example.tax.application.dto.DataCollectionResponse;
import com.example.tax.application.dto.VatResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/v1/tax")
@RequiredArgsConstructor
public class TaxController {

    private final VatCollectionCoordinator vatCollectionCoordinator;
    // 수집 요청
    @PostMapping
    public ResponseEntity<DataCollectionResponse> placeTaxDataProcess(@RequestBody DataCollectionRequest request) {
        final DataCollectionResponse response = vatCollectionCoordinator.requestDataProcess(request);
        return ResponseEntity.ok(response);
    }

    // 수집 상태 조회
    @GetMapping("/{storeId}/state")
    public ResponseEntity<DataCollectionResponse> getTaxDataProcessCurrentState(@PathVariable String storeId
            , @RequestParam(name = "yearMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        final DataCollectionResponse response = vatCollectionCoordinator.getState(storeId, yearMonth);
        return ResponseEntity.ok(response);
    }

    // 부가세 조회
    @GetMapping("/{storeId}/vat")
    public ResponseEntity<Object> getVat(@PathVariable String storeId
            , @RequestParam(name = "yearMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        final VatResultResponse response = vatCollectionCoordinator.getVat(storeId, yearMonth);
        return ResponseEntity.ok(response);
    }
}

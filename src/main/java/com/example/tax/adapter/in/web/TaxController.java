package com.example.tax.adapter.in.web;

import com.example.tax.adapter.in.web.dto.ApiResponse;
import com.example.tax.adapter.in.web.dto.DataCollectionRequest;
import com.example.tax.adapter.in.web.dto.DataCollectionResponse;
import com.example.tax.adapter.in.web.dto.VatResultResponse;
import com.example.tax.application.VatCollectionCoordinator;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/v1/tax")
@RequiredArgsConstructor
public class TaxController {

    private final VatCollectionCoordinator vatCollectionCoordinator;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ApiResponse<DataCollectionResponse> placeTaxDataProcess(@RequestBody DataCollectionRequest request) {
        final DataCollectionResponse response = vatCollectionCoordinator.requestDataProcess(request);
        return ApiResponse.of(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{storeId}/state")
    public ApiResponse<DataCollectionResponse> getTaxDataProcessCurrentState(@PathVariable String storeId
            , @RequestParam(name = "yearMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        final DataCollectionResponse response = vatCollectionCoordinator.getState(storeId, yearMonth);
        return ApiResponse.of(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{storeId}/vat")
    public ApiResponse<Object> getVat(@PathVariable String storeId
            , @RequestParam(name = "yearMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        final VatResultResponse response = vatCollectionCoordinator.getVat(storeId, yearMonth);
        return ApiResponse.of(response);
    }
}

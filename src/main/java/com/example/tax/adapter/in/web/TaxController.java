package com.example.tax.adapter.in.web;

import com.example.tax.adapter.in.web.dto.ApiResponse;
import com.example.tax.adapter.in.web.dto.DataCollectionRequest;
import com.example.tax.adapter.in.web.dto.DataCollectionResponse;
import com.example.tax.adapter.in.web.dto.VatResultResponse;
import com.example.tax.application.usecase.GetTaxStateUseCase;
import com.example.tax.application.usecase.GetVatUseCase;
import com.example.tax.application.usecase.RequestDataProcessUseCase;
import com.example.tax.domain.valueobject.StoreVat;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequestMapping("/api/v1/tax")
@RequiredArgsConstructor
public class TaxController {

    private final GetVatUseCase getVatUseCase;
    private final GetTaxStateUseCase getTaxStateUseCase;
    private final RequestDataProcessUseCase requestDataProcessUseCase;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ApiResponse<DataCollectionResponse> placeTaxDataProcess(@RequestBody DataCollectionRequest request) {
        final DataCollectionResponse response = requestDataProcessUseCase.requestDataProcess(request);
        return ApiResponse.of(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{storeId}/state")
    public ApiResponse<DataCollectionResponse> getTaxDataProcessCurrentState(@PathVariable String storeId
            , @RequestParam(name = "yearMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        final DataCollectionResponse response = getTaxStateUseCase.getState(storeId, yearMonth);
        return ApiResponse.of(response);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{storeId}/vat")
    public ApiResponse<VatResultResponse> getVat(@PathVariable String storeId
            , @RequestParam(name = "yearMonth") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        final StoreVat storeVat = getVatUseCase.getVat(storeId, yearMonth);
        final VatResultResponse response = VatResultResponse.builder()
                .vat(storeVat.getVat().getAmount().longValue())
                .storeId(storeVat.getStoreId())
                .yearMonth(storeVat.getTargetYearMonth())
                .build();
        return ApiResponse.of(response);
    }
}

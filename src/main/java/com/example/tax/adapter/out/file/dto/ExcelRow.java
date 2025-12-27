package com.example.tax.adapter.out.file.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ExcelRow {
    @ExcelProperty(index = 0)
    private BigDecimal amount;

    @ExcelProperty(index = 1)
    @DateTimeFormat(pattern = "yyyy-MM-dd") // 엑셀 날짜 형식에 맞춰 조정
    private LocalDate date;
}
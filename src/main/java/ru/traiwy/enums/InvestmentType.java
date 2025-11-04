package ru.traiwy.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum InvestmentType {
    ONE_MILLION(1000000),
    ONE_HUNDRED_K(100000),
    TEN_K(10000),
    ONE_K(1000);

    private final int amount;
    InvestmentType(int amount) { this.amount = amount; }
}

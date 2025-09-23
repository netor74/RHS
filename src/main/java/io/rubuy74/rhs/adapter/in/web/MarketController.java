package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.MarketRequest;
import io.rubuy74.rhs.domain.OperationType;
import io.rubuy74.rhs.port.in.MarketChangeUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/market-change")
public class MarketController {
    private final MarketChangeUseCase marketChangeUseCase;

    public MarketController(MarketChangeUseCase marketChangeUseCase) {
        this.marketChangeUseCase = marketChangeUseCase;
    }

    @PostMapping
    public void addMarkets(@RequestBody MarketRequest marketRequest) {
        MarketOperation marketOperation = new MarketOperation(
                marketRequest,
                OperationType.ADD
                );
        marketChangeUseCase.handle(marketOperation);
    }

    @PutMapping
    public void editMarkets(@RequestBody MarketRequest marketRequest) {
        MarketOperation marketOperation = new MarketOperation(
                marketRequest,
                OperationType.EDIT
        );
        marketChangeUseCase.handle(marketOperation);
    }

    @DeleteMapping
    public void deleteMarkets(@RequestBody MarketRequest marketRequest) {
        MarketOperation marketOperation = new MarketOperation(
                marketRequest,
                OperationType.DELETE
        );
        marketChangeUseCase.handle(marketOperation);
    }
}

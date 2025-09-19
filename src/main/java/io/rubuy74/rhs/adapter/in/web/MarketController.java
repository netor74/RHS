package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.MarketRequest;
import io.rubuy74.rhs.port.in.MarketChangeUseCase;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/market-change")
public class MarketController {
    private final MarketChangeUseCase marketChangeUseCase;

    public MarketController(MarketChangeUseCase marketChangeUseCase) {
        this.marketChangeUseCase = marketChangeUseCase;
    }

    @PostMapping()
    public void addMarkets(@RequestBody MarketRequest marketRequest) {
        marketChangeUseCase.handle(marketRequest);
    }

    @PutMapping()
    public void editMarkets(@RequestBody MarketRequest marketRequest) {
        marketChangeUseCase.handle(marketRequest);
    }

    @DeleteMapping()
    public void deleteMarkets(@RequestBody MarketRequest marketRequest) {
        marketChangeUseCase.handle(marketRequest);
    }
}

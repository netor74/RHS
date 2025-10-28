package io.rubuy74.rhs.adapter.in.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.http.MarketRequest;
import io.rubuy74.rhs.domain.http.OperationType;
import io.rubuy74.rhs.port.in.MarketChangeUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/market-change")
public class MarketController {
    private final MarketChangeUseCase marketChangeUseCase;

    public MarketController(MarketChangeUseCase marketChangeUseCase) {
        this.marketChangeUseCase = marketChangeUseCase;
    }

    @PostMapping
    public ResponseEntity<Void> addMarkets(@RequestBody MarketRequest marketRequest) throws JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        MarketOperation marketOperation = new MarketOperation(
                requestId,
                marketRequest,
                OperationType.ADD
                );
        marketChangeUseCase.handle(marketOperation);
        return ResponseEntity.accepted().header("Location", "/api/v1/market-change/status/" + requestId).build();
    }

    @PutMapping
    public ResponseEntity<Void> editMarkets(@RequestBody MarketRequest marketRequest) throws JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        MarketOperation marketOperation = new MarketOperation(
                requestId,
                marketRequest,
                OperationType.EDIT
        );
        marketChangeUseCase.handle(marketOperation);
        return ResponseEntity.accepted().header("Location", "/api/v1/market-change/status/" + requestId).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteMarkets(@RequestBody MarketRequest marketRequest) throws JsonProcessingException {
        String requestId = UUID.randomUUID().toString();
        MarketOperation marketOperation = new MarketOperation(
                requestId,
                marketRequest,
                OperationType.DELETE
        );
        marketChangeUseCase.handle(marketOperation);
        return ResponseEntity.accepted().header("Location", "/api/v1/market-change/status/" + requestId).build();
    }
}

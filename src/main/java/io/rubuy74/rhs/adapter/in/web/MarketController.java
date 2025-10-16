package io.rubuy74.rhs.adapter.in.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.rubuy74.rhs.domain.MarketOperation;
import io.rubuy74.rhs.domain.http.MarketRequest;
import io.rubuy74.rhs.domain.http.OperationType;
import io.rubuy74.rhs.port.in.MarketChangeUseCase;
import org.apache.kafka.shaded.io.opentelemetry.proto.trace.v1.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/market-change")
public class MarketController {
    private final MarketChangeUseCase marketChangeUseCase;

    public MarketController(MarketChangeUseCase marketChangeUseCase) {
        this.marketChangeUseCase = marketChangeUseCase;
    }

    @PostMapping
    public ResponseEntity<Object> addMarkets(@RequestBody MarketRequest marketRequest) throws JsonProcessingException {
        MarketOperation marketOperation = new MarketOperation(
                marketRequest,
                OperationType.ADD
                );
        marketChangeUseCase.handle(marketOperation);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @PutMapping
    public ResponseEntity<Object> editMarkets(@RequestBody MarketRequest marketRequest) throws JsonProcessingException {
        MarketOperation marketOperation = new MarketOperation(
                marketRequest,
                OperationType.EDIT
        );
        marketChangeUseCase.handle(marketOperation);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteMarkets(@RequestBody MarketRequest marketRequest) throws JsonProcessingException {
        MarketOperation marketOperation = new MarketOperation(
                marketRequest,
                OperationType.DELETE
        );
        marketChangeUseCase.handle(marketOperation);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}

package io.rubuy74.rhs.adapter.in.web;

import io.rubuy74.rhs.domain.MarketOperationResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/market-change/status")
public class MarketStatusController {

    private final ConcurrentHashMap<String, MarketOperationResult> cache;

    public MarketStatusController(ConcurrentHashMap<String, MarketOperationResult> cache) {
        this.cache = cache;
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Map<String, Object>> getStatus(@PathVariable String requestId) {
        MarketOperationResult result = cache.get(requestId);
        if (result == null) {
            return ResponseEntity.ok(Map.of("status", "PENDING", "message", "Request is still processing"));
        }
        return ResponseEntity.ok(Map.of("status", result.resultType(), "message", result.message(), "marketRequest", result.marketOperation().getMarketRequest()));
    }
}
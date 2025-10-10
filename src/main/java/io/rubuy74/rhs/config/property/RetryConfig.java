package io.rubuy74.rhs.config.property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@ManagedResource
public class RetryConfig {

    private AtomicInteger maxRetries;
    private AtomicInteger backoffTimeMs;
    private AtomicInteger timeoutMs;

    private static final Logger LOGGER = LoggerFactory.getLogger(RetryConfig.class);

    @ManagedAttribute
    public int getMaxRetries() {
        return maxRetries.get();
    }

    @ManagedAttribute
    public void setMaxRetries(int maxRetries) {
        LOGGER.debug("operation=set_max_retries, msg=Set max retries to {}", maxRetries);
        this.maxRetries.set(maxRetries);
    }

    @ManagedAttribute
    public int getBackoffTimeMs() {
        return backoffTimeMs.get();
    }

    @ManagedAttribute
    public void setBackoff(int backoff) {
        LOGGER.debug("operation=set_backoff, msg=Set Backoff limit to {}", backoff);
        this.backoffTimeMs.set(backoff);
    }
    @ManagedAttribute
    public int getTimeoutMs() {
        return timeoutMs.get();
    }

    @ManagedAttribute
    public void setTimeout(int timeout) {
        LOGGER.debug("operation=set_timeout, msg=Set timeout to {}", timeout);
        this.timeoutMs.set(timeout);
    }

}

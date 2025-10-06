package io.rubuy74.rhs.config.property;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

@Component
@ManagedResource
public class RetryConfig {

    private int maxRetries;
    private int backoffTimeMs;
    private int timeoutMs;

    private static final Logger logger = LoggerFactory.getLogger(RetryConfig.class);

    @ManagedAttribute
    public int getMaxRetries() {
        return maxRetries;
    }

    @ManagedAttribute
    public void setMaxRetries(int maxRetries) {
        logger.debug("operation=set_max_retries, msg=Set max retries to {}", maxRetries);
        this.maxRetries = maxRetries;
    }

    @ManagedAttribute
    public int getBackoffTimeMs() {
        return backoffTimeMs;
    }

    @ManagedAttribute
    public void setBackoff(int backoff) {
        logger.debug("operation=set_backoff, msg=Set Backoff limit to {}", backoff);
        this.backoffTimeMs = backoff;
    }
    @ManagedAttribute
    public int getTimeoutMs() {
        return timeoutMs;
    }

    @ManagedAttribute
    public void setTimeout(int timeout) {
        logger.debug("operation=set_timeout, msg=Set timeout to {}", timeout);
        this.timeoutMs = timeout;
    }

}

package com.gamusdev.servletreactive.performance.meter.executor;

/**
 * IMeterExecutor interface
 */
public interface IMeterExecutor {

    /**
     * Execute the test
     * @throws InterruptedException InterruptedException
     */
    void execute() throws InterruptedException;
}

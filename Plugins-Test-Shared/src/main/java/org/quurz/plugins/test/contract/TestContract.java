package org.quurz.plugins.test.contract;

public interface TestContract {

    default String echo(final String call) {
        return "Echo from contract: " + call;
    }

}

module foomp.plugins.runtime {
    exports org.quurz.plugins;
    exports org.quurz.plugins.events;
    exports org.quurz.plugins.data;
    exports org.quurz.plugins.localisation;

    requires foomp.base;
    requires net.bytebuddy;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.networknt.schema;

    requires static org.checkerframework.checker.qual;
}
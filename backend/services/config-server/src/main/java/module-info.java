import org.jspecify.annotations.NullMarked;

@NullMarked module vn.edu.uit.msshop.config {
    requires static lombok;

    requires org.jspecify;

    requires spring.beans;

    requires spring.boot;
    requires spring.boot.autoconfigure;

    requires spring.cloud.config.server;

    opens configurations;

    opens vn.edu.uit.msshop.config to
            spring.core,
            spring.beans,
            spring.context;
}

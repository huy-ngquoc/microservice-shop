import org.jspecify.annotations.NullMarked;

@NullMarked module vn.edu.uit.msshop.discovery {
    requires static lombok;

    requires java.sql;

    requires org.jspecify;

    requires spring.beans;

    requires spring.boot;
    requires spring.boot.autoconfigure;

    requires spring.cloud.config.client;
    requires spring.cloud.netflix.eureka.server;

    opens vn.edu.uit.msshop.discovery to
            spring.core,
            spring.beans,
            spring.context;
}

import org.jspecify.annotations.NullMarked;

@NullMarked module vn.edu.uit.msshop.gateway {
    requires static lombok;

    requires org.jspecify;

    requires spring.beans;

    requires java.sql;
    requires jdk.net;

    requires spring.boot;
    requires spring.boot.autoconfigure;

    requires spring.cloud.config.client;
    requires spring.cloud.netflix.eureka.client;

    opens vn.edu.uit.msshop.gateway to
            spring.core,
            spring.beans,
            spring.context;
}

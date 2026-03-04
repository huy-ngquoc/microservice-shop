import org.jspecify.annotations.NullMarked;

@NullMarked open module vn.uit.edu.msshop.product {

    // ========= Compile-time only =========
    requires static org.jspecify;
    requires static lombok;

    // ========= JDK =========
    requires java.sql;
    requires java.naming;
    requires java.management;
    requires jdk.unsupported;

    // ========= Spring Boot / Spring Framework =========
    requires spring.boot;
    requires spring.boot.autoconfigure;

    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.aop;
    requires spring.expression;

    // ========= Web (Spring MVC + Servlet) =========
    requires spring.web;
    requires spring.webmvc;

    // requires jakarta.servlet;
    requires jakarta.validation;

    // ========= Persistence (Spring Data MongoDB) =========
    requires spring.tx;
    requires spring.data.commons;
    requires spring.data.mongodb;

    // Nếu bạn có import trực tiếp class của Mongo driver/BSON trong code thì bật
    // thêm:
    // requires org.mongodb.bson;
    // requires org.mongodb.driver.core;
    // requires org.mongodb.driver.sync.client;

    // ========= Validation provider =========
    requires org.hibernate.validator;

    // ========= JSON (Jackson) =========
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    // requires com.fasterxml.jackson.datatype.jsr310; // nếu bạn register
    // JavaTimeModule

    // ========= Logging =========
    requires org.slf4j;

    // ========= OpenAPI (springdoc) - tùy bạn có dùng =========
    // requires org.springdoc.openapi.common;
    // requires org.springdoc.openapi.webmvc.core;
    // requires org.springdoc.openapi.ui;

    // ========= Security - nếu có SecurityConfig / starter-security =========
    // requires spring.security.core;
    // requires spring.security.config;
    // requires spring.security.web;

    // ========= Messaging (Kafka/RabbitMQ/...) - nếu có =========
    // requires spring.messaging;
    // requires spring.kafka;
    // requires org.apache.kafka.clients;

    // ========= Cloudinary =========
    requires cloudinary.core;
    requires cloudinary.http5;
}

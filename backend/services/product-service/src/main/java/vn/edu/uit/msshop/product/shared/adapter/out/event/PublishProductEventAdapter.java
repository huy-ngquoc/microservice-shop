package vn.edu.uit.msshop.product.shared.adapter.out.event;

import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vn.edu.uit.msshop.product.shared.application.port.out.PublishProductEventPort;
import vn.edu.uit.msshop.product.shared.event.document.ProductCreatedDocument;
import vn.edu.uit.msshop.product.shared.event.document.ProductDeletedDocument;
import vn.edu.uit.msshop.product.shared.event.document.ProductUpdateDocument;
import vn.edu.uit.msshop.product.shared.event.document.VariantDeletedDocument;
import vn.edu.uit.msshop.product.shared.event.document.VariantUpdateDocument;
import vn.edu.uit.msshop.product.shared.event.domain.ProductCreated;
import vn.edu.uit.msshop.product.shared.event.domain.ProductDeleted;
import vn.edu.uit.msshop.product.shared.event.domain.ProductUpdate;
import vn.edu.uit.msshop.product.shared.event.domain.VariantCreated;
import vn.edu.uit.msshop.product.shared.event.domain.VariantDeleted;
import vn.edu.uit.msshop.product.shared.event.domain.VariantUpdate;
import vn.edu.uit.msshop.product.shared.event.publisher.ProductCreatedOutboxPublisher;
import vn.edu.uit.msshop.product.shared.event.publisher.ProductDeletedOutboxPublisher;
import vn.edu.uit.msshop.product.shared.event.publisher.ProductUpdateOutboxPublisher;
import vn.edu.uit.msshop.product.shared.event.publisher.VariantDeletedOutboxPublisher;
import vn.edu.uit.msshop.product.shared.event.publisher.VariantUpdateOutboxPublisher;

@Component
@RequiredArgsConstructor
@Slf4j
public class PublishProductEventAdapter implements PublishProductEventPort {
    private static final String PUBLISH_TOPIC="product-topic";
    private final KafkaTemplate<String,ProductCreated> productCreatedTemplate;
    private final ProductCreatedOutboxPublisher productCreatedOutboxPublisher;
    private final KafkaTemplate<String,ProductUpdate> productUpdateTemplate;
    private final ProductUpdateOutboxPublisher productUpdateOutboxPublisher;
    private final KafkaTemplate<String,VariantUpdate> variantUpdateTemplate;
    private final VariantUpdateOutboxPublisher variantUpdateOutboxPublisher;
    private final KafkaTemplate<String,VariantDeleted> variantDeletedTemplate;
    private final VariantDeletedOutboxPublisher variantDeletedOutboxPublisher;
    private final KafkaTemplate<String,ProductDeleted> productDeletedTemplate;
    private final ProductDeletedOutboxPublisher productDeletedOutboxPublisher;
    @Override
    public void publishProductCreated(
            ProductCreatedDocument eventDocument) {
                System.out.println("Send product created");
        List<VariantCreated> variantCreateds = eventDocument.getVariantCreateds().stream().map(item->new VariantCreated(item.getVariantId(), eventDocument.getProductId(), eventDocument.getProductName(), item.getPrice(), item.getTraits(), item.getImageKey())).toList();
            ProductCreated productCreated = new ProductCreated(eventDocument.getEventId(), eventDocument.getProductId(), eventDocument.getProductName(), variantCreateds);
            Message<ProductCreated> message = MessageBuilder.withPayload(productCreated).setHeader(KafkaHeaders.TOPIC, PUBLISH_TOPIC).build();

        try {
        productCreatedTemplate.send(message).whenComplete((result,ex)->{
            if(ex==null) {
                productCreatedOutboxPublisher.markAsSent(eventDocument);
            }
            else {
                System.out.println("Send fail");
            }
        });
    }
    catch(Exception e) {
        log.error("Error sending event");
        e.printStackTrace();
    }
    }
    @Override
    public void publishProductUpdated(
            ProductUpdateDocument eventDocument) {
        
        ProductUpdate productUpdate = new ProductUpdate(eventDocument.getEventId(), eventDocument.getProductId(),eventDocument.getName());
        Message<ProductUpdate> message = MessageBuilder.withPayload(productUpdate).setHeader(KafkaHeaders.TOPIC, PUBLISH_TOPIC).build();

        try {
        variantUpdateTemplate.send(message).whenComplete((result,ex)->{
            if(ex==null) {
                productUpdateOutboxPublisher.markAsSent(eventDocument);
            }
            else {
                System.out.println("Send fail");
            }
        });
    }
    catch(Exception e) {
        log.error("Error sending event");
    }
    }
    @Override
    public void publishVariantUpdated(
            VariantUpdateDocument eventDocument) {
    VariantUpdate variantUpdate = new VariantUpdate(eventDocument.getEventId(), eventDocument.getVariantId(), eventDocument.getProductId(), eventDocument.getProductName(), eventDocument.getPrice(), eventDocument.getTraits(), eventDocument.getImageKey()==null?"":eventDocument.getImageKey());
    Message<VariantUpdate> message = MessageBuilder.withPayload(variantUpdate).setHeader(KafkaHeaders.TOPIC, PUBLISH_TOPIC).build();

        try {
        productUpdateTemplate.send(message).whenComplete((result,ex)->{
            if(ex==null) {
                variantUpdateOutboxPublisher.markAsSent(eventDocument);
            }
            else {
                System.out.println("Send fail");
            }
        });
    }
    catch(Exception e) {
        log.error("Error sending event");
    }
    }
    @Override
    public void publishVariantDeleted(
            VariantDeletedDocument eventDocument) {
    VariantDeleted variantDeleted = new VariantDeleted(eventDocument.getEventId(), eventDocument.getVariantId());
    Message<VariantDeleted> message = MessageBuilder.withPayload(variantDeleted).setHeader(KafkaHeaders.TOPIC, PUBLISH_TOPIC).build();

        try {
        variantDeletedTemplate.send(message).whenComplete((result,ex)->{
            if(ex==null) {
                variantDeletedOutboxPublisher.markAsSent(eventDocument);
            }
            else {
                System.out.println("Send fail");
            }
        });
    }
    catch(Exception e) {
        log.error("Error sending event");
    }
    }
    @Override
    public void publishProductDeleted(
            ProductDeletedDocument eventDocument) {
       ProductDeleted productDeleted = new ProductDeleted(eventDocument.getEventId(), eventDocument.getProductId());
    Message<ProductDeleted> message = MessageBuilder.withPayload(productDeleted).setHeader(KafkaHeaders.TOPIC, PUBLISH_TOPIC).build();

        try {
        productDeletedTemplate.send(message).whenComplete((result,ex)->{
            if(ex==null) {
                productDeletedOutboxPublisher.markAsSent(eventDocument);
            }
            else {
                System.out.println("Send fail");
            }
        });
    }
    catch(Exception e) {
        log.error("Error sending event");
    }
    }
    

}


package com.example.dynamadbenhanced.event;

import com.example.dynamadbenhanced.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Component
@Slf4j
public class ApplicationStartUp {

    @Value("${person.table.name}")
    private String tableName;

    @Autowired
    DynamoDbEnhancedClient dynamoDbEnhancedClient;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            log.info(String.format("Trying to create dynamodb table %s", tableName));
            dynamoDbEnhancedClient.table(tableName, TableSchema.fromBean(Person.class)).createTable();
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }

    }
}

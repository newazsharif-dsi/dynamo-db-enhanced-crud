package com.example.dynamadbenhanced.service;

import com.example.dynamadbenhanced.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.*;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonService {

    @Value("${person.table.name}")
    private String personTableName;

    @Autowired
    DynamoDbEnhancedClient dynamoDbEnhancedClient;


    public Person getPersonByCompositePrimaryKey(String personId, Integer age) {
        return this.getTable(Person.class).getItem(getItemEnhancedRequest(personId, age));
    }

    public List<Person> getPersonsByPersonId(String personId) {
        var pageIterable = this.getTable(Person.class).query(
                QueryConditional.keyEqualTo(
                        Key.builder()
                                .partitionValue(personId)
                                .build()
                )
        );
        return this.getListFromPageIterable(pageIterable);
    }

    public void createPerson(Person person) {
        this.getTable(Person.class).putItem(person);
    }

    public void updatePerson(Person person) {
        this.getTable(Person.class).updateItem(person);
    }

    public void deletePersonByCompositePrimaryKey(String personId, Integer age) {
        this.getTable(Person.class).deleteItem(
                Key.builder()
                        .partitionValue(personId)
                        .sortValue(age)
                        .build()
        );
    }

    public void deletePersonsByPersonId(String personId) {
        DynamoDbTable<Person> personTable = this.getTable(Person.class);
        PageIterable<Person> pageIterable = personTable.query(
                QueryConditional.keyEqualTo(
                        Key.builder()
                                .partitionValue(personId)
                                .build()
                )
        );
        if (!ObjectUtils.isEmpty(pageIterable)) {
            pageIterable.items().stream().forEach(person -> {
                personTable.deleteItem(person);
            });
        }

    }

    public List<Person> getAllPersonUsingScan() {
        PageIterable<Person> personPageIterable = this.getTable(Person.class).scan();
        return getListFromPageIterable(personPageIterable);
    }

    public List<Person> searchPersonsContainsFirstName(String firstName) {
        String filterExpression = "contains(first_name,:first_name)";
        Map<String, AttributeValue> expressionAttributeValueMap = Map.of(":first_name", AttributeValue.builder().s(firstName).build());
        var table = this.getTable(Person.class);
        var pageIterable = table.scan(
                ScanEnhancedRequest.builder().
                        filterExpression(
                                Expression.builder()
                                        .expression(filterExpression)
                                        .expressionValues(expressionAttributeValueMap)
                                        .build()
                        ).build()
        );
        return getListFromPageIterable(pageIterable);
    }

    public List<Person> searchPersonsByFirstName(String firstName) {
        String filterExpression = "first_name = :first_name";
        Map<String, AttributeValue> expressionAttributeValueMap = Map.of(":first_name", AttributeValue.builder().s(firstName).build());
        var table = this.getTable(Person.class);
        var pageIterable = table.scan(
                ScanEnhancedRequest.builder().
                        filterExpression(
                                Expression.builder()
                                        .expression(filterExpression)
                                        .expressionValues(expressionAttributeValueMap)
                                        .build()
                        ).build()
        );
        return getListFromPageIterable(pageIterable);
    }


    private GetItemEnhancedRequest getItemEnhancedRequest(String personId, Integer age) {
        return GetItemEnhancedRequest.builder().key(
                Key.builder()
                        .partitionValue(personId)
                        .sortValue(age)
                        .build()
        ).build();
    }


    //Utility Methods For Dynamodb
    private <T> DynamoDbTable<T> getTable(Class<T> beanClass) {
        return dynamoDbEnhancedClient.table(personTableName, TableSchema.fromBean(beanClass));
    }

    private <T> List<T> getListFromPageIterable(PageIterable<T> pageIterable) {
        if (null == pageIterable) {
            return new ArrayList<T>();
        }
        return pageIterable.items().stream().collect(Collectors.toList());
    }
}
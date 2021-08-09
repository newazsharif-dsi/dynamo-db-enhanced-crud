package com.example.dynamadbenhanced.entity;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamoDbBean
public class Person {

    @Getter(onMethod_ = {@DynamoDbPartitionKey, @DynamoDbAttribute("person_id")})
    private String personId;

    @Getter(onMethod_ = {@DynamoDbSortKey, @DynamoDbAttribute("age")})
    private Integer age;

    @Getter(onMethod_ = {@DynamoDbAttribute("first_name")})
    private String firstName;

    @Getter(onMethod_ = {@DynamoDbAttribute("last_name")})
    private String lastName;

    @Getter(onMethod_ = {@DynamoDbAttribute("address")})
    private String address;


}

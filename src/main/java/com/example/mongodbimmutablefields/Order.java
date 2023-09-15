package com.example.mongodbimmutablefields;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document
public class Order extends CommonEntity{

    @ImmutableField
    private String address;

    private String phoneNumber;
}

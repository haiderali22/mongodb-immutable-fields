package com.example.mongodbimmutablefields;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

import java.util.Date;

@Setter
@Getter
public class CommonEntity {
    @Id
    private String id;

    @CreatedDate
    @ImmutableField
    private Date createdDate;
    @LastModifiedDate
    private Date lastModifiedDate;

    @Version
    private Long version;
}

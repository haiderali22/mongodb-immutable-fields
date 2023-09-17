package com.example.mongodbimmutablefields;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Reference {
    @ImmutableField
    private String identifier;
    private String hint;
}

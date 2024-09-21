package com.example.flashcardtool.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RolesConverter implements DynamoDBTypeConverter<String, List<String>> {

    @Override
    public String convert(List<String> roles) {
        return String.join(",", roles);
    }

    @Override
    public List<String> unconvert(String rolesString) {
        return Arrays.stream(rolesString.split(",")).collect(Collectors.toList());
    }
}

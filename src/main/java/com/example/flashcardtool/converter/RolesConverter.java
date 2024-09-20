package com.example.flashcardtool.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RolesConverter implements DynamoDBTypeConverter<String, List<String>> {

    public String convert(List<String> roles) {
        System.out.println("Converting roles to string: " + roles);
        return String.join(",", roles);
    }

    public List<String> unconvert(String rolesString) {
        System.out.println("Unconverting roles from string: " + rolesString);
        return Arrays.stream(rolesString.split(",")).collect(Collectors.toList());
    }
}

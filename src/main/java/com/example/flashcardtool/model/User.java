package com.example.flashcardtool.model;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import java.util.*;

import com.example.flashcardtool.converter.RolesConverter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@DynamoDBTable(tableName = "Users")
public class User implements UserDetails {

    @DynamoDBHashKey
    private String id;

    @DynamoDBAttribute
    private String username;

    @DynamoDBAttribute
    private String password;

    @DynamoDBAttribute
    private String email;

 //@DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.SS)
    @DynamoDBTypeConverted(converter = RolesConverter.class)
   // @DynamoDBAttribute
//    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.L)
  private List<String> roles;


    @DynamoDBAttribute
    private String firstName;

    @DynamoDBAttribute
    private String lastName;

    @DynamoDBAttribute
    private String passwordResetToken;

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.L)
    private List<String> assignedDeckIds = new ArrayList<>();

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.M)
    private Map<String, String> deckAssignments = new HashMap<>();

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPasswordResetToken() {
        return passwordResetToken;
    }

    public void setPasswordResetToken(String passwordResetToken) {
        this.passwordResetToken = passwordResetToken;
    }

    public List<String> getAssignedDeckIds() {
        return assignedDeckIds;
    }

    public void setAssignedDeckIds(List<String> assignedDeckIds) {
        this.assignedDeckIds = assignedDeckIds;
    }

    public Map<String, String> getDeckAssignments() {
        return deckAssignments;
    }

    public void setDeckAssignments(Map<String, String> deckAssignments) {
        this.deckAssignments = deckAssignments;
    }

    // UserDetails interface methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> role)
                .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

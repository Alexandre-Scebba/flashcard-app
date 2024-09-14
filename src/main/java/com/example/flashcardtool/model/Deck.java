package com.example.flashcardtool.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@NoArgsConstructor
public class Deck {

    @Id
    private String id;

    private String name;

    private String description;

    private String createdBy;

    private List<String> flashcardIds;

    private String createdAt;

}

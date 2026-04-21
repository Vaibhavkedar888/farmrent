package com.farming.rental.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "schemes")
public class Scheme {
    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("description")
    private String description;

    @Indexed
    @Field("category")
    private String category;

    @Field("benefits")
    private String benefits;

    @Field("eligibility")
    private String eligibility;

    @Field("apply_link")
    private String applyLink;

    @CreatedDate
    @Field("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;
}

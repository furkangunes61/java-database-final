package com.project.code.Model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
public class Review {

    @Id
    private Long id;

    @NotNull(message = "Customer cannot be null")
    private Long customerid;

    @NotNull(message = "Product cannot be null")
    private Long productId;

    @NotNull(message = "Store cannot be null")
    private Long storeId;

    @NotNull(message = "Rating cannot be null")
    private Integer rating;

    private String comment;

    public Review() {}

    public Review(Long customerid, Long productId, Long storeId, Integer rating, String comment) {
        this.customerid = customerid;
        this.productId = productId;
        this.storeId = storeId;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerid() {
        return customerid;
    }

    public void setCustomerid(Long customerid) {
        this.customerid = customerid;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStoreId() {
        return storeId;
    }

    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

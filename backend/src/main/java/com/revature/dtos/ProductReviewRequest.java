package com.revature.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewRequest {

    private int productId;
    private int reviewerId;
    private String reviewerName;

    private int rating;
    private String title;
    private String review;
}

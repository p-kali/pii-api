package com.piiencrypt.pii.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUnmaskedDTO {
    private Long id;
    private String name;
    private String ssn;
    private String dob;
}

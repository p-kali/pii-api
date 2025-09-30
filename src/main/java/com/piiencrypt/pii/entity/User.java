package com.piiencrypt.pii.entity;

import com.piiencrypt.pii.security.Sensitive;
import com.piiencrypt.pii.security.SensitiveType;
import com.piiencrypt.pii.security.AttributeEncryptor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@ToString(exclude = {"ssn", "dob"}) // Prevents accidental leaks in logs
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Sensitive(type = SensitiveType.SSN)
    @Convert(converter = AttributeEncryptor.class)
    private String ssn;

    @Sensitive(type = SensitiveType.DOB)
    private String dob;
}

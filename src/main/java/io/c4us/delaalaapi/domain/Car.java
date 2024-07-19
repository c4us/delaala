package io.c4us.delaalaapi.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cars")
public class Car {
    @Id
    @UuidGenerator
    @Column(name = "id", unique = true,updatable = false)
    private String id;
    private String carId;
    private String carPhotoUrl;
    private String customerId;
    private Date createdDate;
}

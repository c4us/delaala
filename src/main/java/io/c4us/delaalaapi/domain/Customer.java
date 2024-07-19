package io.c4us.delaalaapi.domain;

//import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.c4us.delaalaapi.constant.CustomersStatus;
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
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@Table(name = "customers")
public class Customer {
    @Id
    @UuidGenerator
    @Column(name = "id", unique = true,updatable = false)
    private String id;
    private Date createdDate;
    private String carPhotoUrl;
    private String customerType;
    private String customerName;
    private String customerPhoneNumber;
    private CustomersStatus isActive;
    private double solde;



}

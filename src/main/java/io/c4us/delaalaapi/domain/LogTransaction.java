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
@Table(name = "log")
public class LogTransaction {
    @Id
    @UuidGenerator
    @Column(name = "id", unique = true,updatable = false)
    private String id;
    private Date transactionDate;
    private double amountCharge;
    private String idCar;
    private String idCustomer;
    private String transactionStatus;
    private String rootCause;
}

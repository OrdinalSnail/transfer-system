package com.nordea.transfer_system.model;


import lombok.*;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "currency_amount")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class CurrencyAmountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_generator")
    @SequenceGenerator(name = "account_id_generator", sequenceName = "currency_amount_id_seq", allocationSize = 1)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    @Column(name = "currency")
    private String currency;

    @Column(name = "amount")
    private BigDecimal amount;



}

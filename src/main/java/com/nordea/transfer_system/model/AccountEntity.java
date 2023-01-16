package com.nordea.transfer_system.model;


import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_amount_id_generator")
    @SequenceGenerator(name = "currency_amount_id_generator", sequenceName = "account_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "account_number")
    private String accountNumber;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CurrencyAmountEntity> currencyAmounts;

}

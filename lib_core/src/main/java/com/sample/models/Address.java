package com.sample.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@Setter
@Entity(name = "addresses")
public final class Address extends DataModel {

    @Id
    private UUID id;

    private String street;

    private String city;

    private String zip;

    @ManyToOne
    private Account account;
}

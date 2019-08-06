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
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@Setter
@Entity(name = "accounts")
public final class Account extends DataModel {

    @Id
    private UUID id;

    private String firstName;

    private String lastName;

    private String password;
}

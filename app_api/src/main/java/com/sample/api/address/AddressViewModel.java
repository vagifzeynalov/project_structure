package com.sample.api.address;

import com.sample.api.validators.NotEmptyString;
import com.sample.api.validators.RequestValidationGroups.Create;
import com.sample.models.Account;
import com.sample.models.Address;
import com.sample.models.Model;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@Builder(access = AccessLevel.PACKAGE)
@Getter
@Setter
final class AddressViewModel extends Model {

    @Null
    private UUID addressId;

    @NotBlank(groups = {Create.class})
    @NotEmptyString
    private String firstName;

    @NotBlank(groups = {Create.class})
    @NotEmptyString
    private String lastName;

    @NotBlank(groups = {Create.class})
    @NotEmptyString
    private String street;

    @NotBlank(groups = {Create.class})
    @NotEmptyString
    private String city;

    @NotBlank(groups = {Create.class})
    @NotEmptyString
    private String zip;

    static AddressViewModel of(final Address address) {
        return AddressViewModel.builder()
                .addressId(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .zip(address.getZip())
                .build();
    }

    static AddressViewModel of(final Address address, final Account account) {
        final AddressViewModel model = AddressViewModel.builder()
                .addressId(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .zip(address.getZip())
                .build();
        model.setFirstName(account.getFirstName());
        model.setLastName(account.getLastName());
        return model;
    }

    Address map(final UUID id) {
        final Address address = map();
        address.setId(id);
        return address;
    }

    Address map() {
        return Address.builder()
                .street(street)
                .city(city)
                .zip(zip)
                .build();
    }
}

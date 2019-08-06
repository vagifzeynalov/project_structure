package com.sample.services;

import com.sample.exceptions.ServiceException;
import com.sample.models.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ApiService {

    private final AccountService accountService;

    private final AddressService addressService;

    @Autowired
    public ApiService(final AccountService accountService, final AddressService addressService) {
        this.accountService = accountService;
        this.addressService = addressService;
    }

    public Address getAddress(final UUID id) throws ServiceException {
        return addressService.get(id);
    }

    public List<Address> getAllAddresses() {
        return addressService.getAll();
    }

    public Address createAddress(final Address address, final String firstName, final String lastName) throws ServiceException {
        address.setAccount(accountService.get(firstName, lastName));
        return addressService.insert(address);
    }

    public Address updateAddress(final Address address) throws ServiceException {
        return addressService.update(address);
    }
}

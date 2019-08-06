package com.sample.api.address;

import com.sample.api.validators.RequestValidationGroups.Create;
import com.sample.api.validators.RequestValidationGroups.Update;
import com.sample.exceptions.ServiceException;
import com.sample.services.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/addresses")
class AddressController {

    private final ApiService service;

    @Autowired
    AddressController(final ApiService service) {
        this.service = service;
    }

    @GetMapping(params = "/{id}")
    public AddressViewModel get(final UUID id) throws ServiceException {
        return AddressViewModel.of(service.getAddress(id));
    }

    @GetMapping
    public List<AddressViewModel> getAll() {
        return service.getAllAddresses().stream().map(AddressViewModel::of).collect(Collectors.toList());
    }

    @PostMapping
    public AddressViewModel create(@Validated(Create.class) @RequestBody final AddressViewModel request) throws ServiceException {
        return AddressViewModel.of(service.createAddress(request.map(), request.getFirstName(), request.getLastName()));
    }

    @PutMapping(params = "/{id}")
    public AddressViewModel update(final UUID id, @Validated(Update.class) @RequestBody final AddressViewModel request) throws ServiceException {
        return AddressViewModel.of(service.updateAddress(request.map(id)));
    }
}

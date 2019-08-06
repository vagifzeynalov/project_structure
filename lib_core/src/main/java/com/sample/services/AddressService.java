package com.sample.services;

import com.sample.exceptions.AlreadyExistsException;
import com.sample.exceptions.BadRequestException;
import com.sample.exceptions.NotFoundException;
import com.sample.exceptions.ServiceException;
import com.sample.models.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
class AddressService {

    private final AddressRepository repository;

    @Autowired
    public AddressService(final AddressRepository repository) {
        this.repository = repository;
    }

    public Address get(final UUID id) throws ServiceException {
        if (id == null) {
            throw new BadRequestException("Address ID is NULL");
        }
        return repository.findAddressById(id).orElseThrow(new NotFoundException(String.format("Address for id:%s", id)));
    }

    public List<Address> getAll() {
        return repository.findAll();
    }

    public Address insert(final Address address) throws ServiceException {
        if (address == null || address.getId() != null) {
            throw new BadRequestException();
        }
        if (repository.existsById(address.getId())) {
            throw new AlreadyExistsException(String.format("Address for id:%s", address.getId()));
        }
        address.setId(UUID.randomUUID());
        return repository.save(address);
    }

    public Address update(final Address address) throws ServiceException {
        if (address == null || address.getId() == null) {
            throw new BadRequestException();
        }
        if (repository.existsById(address.getId())) {
            return repository.save(address);
        }
        throw new NotFoundException(String.format("Address for id:%s", address.getId()));
    }

    @Repository
    interface AddressRepository extends CrudRepository<Address, UUID> {

        Optional<Address> findAddressById(UUID id);

        List<Address> findAll();

        List<Address> findAllByAccountId(UUID id);
    }
}

package com.sample.services;

import com.sample.exceptions.BadRequestException;
import com.sample.exceptions.NotFoundException;
import com.sample.exceptions.ServiceException;
import com.sample.models.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
class AccountService {

    private final AccountRepository repository;

    @Autowired
    AccountService(final AccountRepository repository) {
        this.repository = repository;
    }

    public Account get(final UUID id) throws ServiceException {
        if (id == null) {
            throw new BadRequestException("Account Id is NULL");
        }
        return repository.findAccountById(id).orElseThrow(new NotFoundException(String.format("Account for id:%s", id)));
    }

    public Account get(final String firstName, final String lastName) throws ServiceException {
        if (firstName == null || lastName == null) {
            throw new BadRequestException("firstName or lastName is NULL");
        }
        return repository.findOneByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(new NotFoundException(String.format("Account for firstName:%s and lastName:%s", firstName, lastName)));
    }

    public List<Account> getAll(final List<UUID> ids) throws BadRequestException {
        if (ids == null) {
            throw new BadRequestException();
        }
        return repository.findAccountsByIdIn(ids);
    }

    public Account save(final Account account) throws BadRequestException {
        if (account == null || account.getId() == null) {
            throw new BadRequestException();
        }
        return repository.save(account);
    }

    @Repository
    interface AccountRepository extends CrudRepository<Account, UUID> {

        Optional<Account> findAccountById(UUID id);

        Optional<Account> findOneByFirstNameAndLastName(String firstName, String lastName);

        List<Account> findAccountsByIdIn(List<UUID> ids);
    }
}

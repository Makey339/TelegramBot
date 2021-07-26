package com.makey.telegram.services;

import com.makey.telegram.models.Domain;

import java.util.List;

public interface DomainService {
    void save(Domain domain);
    void delete(Domain domain);
    List<Domain> getAll();
    Domain getById(Long id);
    Long count();
    void saveAll(List<Domain> domains);
    void deleteAll();
}

package com.makey.telegram.services;

import com.makey.telegram.models.Domain;
import com.makey.telegram.repositories.DomainRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DomainServiceImpl implements DomainService {

    private final DomainRepository domainRepository;

    public DomainServiceImpl(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }


    @Override
    public void save(Domain domain) {
        domainRepository.save(domain);
    }

    @Override
    public void delete(Domain domain) {
        domainRepository.delete(domain);
    }

    @Override
    public List<Domain> getAll() {
        return domainRepository.findAll();
    }

    @Override
    public Domain getById(Long id) {
        return domainRepository.getById(id);
    }

    @Override
    public Long count() {
        return domainRepository.count();
    }

    @Override
    public void saveAll(List<Domain> domains) {
        domainRepository.saveAll(domains);
    }

    @Override
    public void deleteAll() {
        domainRepository.deleteAll();
    }
}

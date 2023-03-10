package com.proximus.cloudfusion.example.domain;

import com.proximus.cloudfusion.example.data.CustomerProfileEntity;
import com.proximus.cloudfusion.example.data.CustomerProfileRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class CustomerProfileService {

    private final CustomerProfileRepository repository;

    public CustomerProfileService(CustomerProfileRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public CustomerProfileResponse create(CustomerProfileCreateRequest dto) {
        var entity = new CustomerProfileEntity()
                .setId(UUID.randomUUID())
                .setFirstName(dto.firstName())
                .setLastName(dto.lastName())
                .setEmail(dto.email());

        var persistedEntity = repository.save(entity);
        return entityToDto(persistedEntity);
    }

    public Optional<CustomerProfileResponse> getById(String idRepresentation) {
        return safeConvertToUUID(idRepresentation)
                .flatMap(repository::findById)
                .map(this::entityToDto);
    }

    private Optional<UUID> safeConvertToUUID(String stringRepresentation) {
        try {
            return Optional.of(UUID.fromString(stringRepresentation));
        } catch (IllegalArgumentException ignorable) {
            return Optional.empty();
        }
    }

    private CustomerProfileResponse entityToDto(CustomerProfileEntity entity) {
        return new CustomerProfileResponse(
                entity.getId().toString(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail());
    }

    public List<CustomerProfileResponse> list() {
        return repository.findAll(PageRequest.of(0, 20))
                .get().map(entity -> new CustomerProfileResponse(
                        entity.getId().toString(),
                        entity.getFirstName(),
                        entity.getLastName(),
                        entity.getEmail()))
                .toList();
    }
}

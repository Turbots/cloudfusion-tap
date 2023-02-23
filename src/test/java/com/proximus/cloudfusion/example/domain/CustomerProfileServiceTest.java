package com.proximus.cloudfusion.example.domain;

import com.proximus.cloudfusion.example.data.CustomerProfileEntity;
import com.proximus.cloudfusion.example.data.CustomerProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerProfileServiceTest {

    @Mock
    private CustomerProfileRepository repository;

    @InjectMocks
    private CustomerProfileService subject;

    @Test
    void shouldDelegateToRepositoryToPersistProfile() {
        // given
        var customerProfile = new CustomerProfileCreateRequest("Joe", "Doe", "joe.doe@test.org");

        when(repository.save(any())).thenAnswer((invocation) -> invocation.getArgument(0));

        // when
        var result = subject.create(customerProfile);

        // then
        assertThat(result).isNotNull();
        assertThat(result.firstName()).isEqualTo(customerProfile.firstName());
        assertThat(result.lastName()).isEqualTo(customerProfile.lastName());
        assertThat(result.email()).isEqualTo(customerProfile.email());
        assertThat(result.id()).isNotBlank();

        var customerProfileEntityCaptor = ArgumentCaptor.forClass(CustomerProfileEntity.class);
        verify(repository).save(customerProfileEntityCaptor.capture());

        var customerProfileEntity = customerProfileEntityCaptor.getValue();
        assertThat(customerProfileEntity.getFirstName()).isEqualTo(customerProfile.firstName());
        assertThat(customerProfileEntity.getLastName()).isEqualTo(customerProfile.lastName());
        assertThat(customerProfileEntity.getEmail()).isEqualTo(customerProfile.email());
        assertThat(customerProfileEntity.getId()).isEqualTo(UUID.fromString(result.id()));
    }

    @Test
    void shouldDelegateToRepositoryToRetrieveProfile() {

        var id = UUID.randomUUID();
        var entity = new CustomerProfileEntity().setId(id).setFirstName("Joe").setLastName("Doe").setEmail("joe.doe@test.org");
        when(repository.findById(any())).thenReturn(Optional.of(entity));

        // when
        var resultOptional = subject.getById(id.toString());

        // then
        assertThat(resultOptional).isPresent();

        var result = resultOptional.get();
        assertThat(result.firstName()).isEqualTo(entity.getFirstName());
        assertThat(result.lastName()).isEqualTo(entity.getLastName());
        assertThat(result.email()).isEqualTo(entity.getEmail());
        assertThat(result.id()).isEqualTo(id.toString());

        verify(repository).findById(id);
    }

    @Test
    void shouldReturnEmptyIfIdIsInvalid() {

        // when
        var resultOptional = subject.getById("invalid-id");

        // then
        assertThat(resultOptional).isEmpty();

        verifyNoInteractions(repository);
    }
}

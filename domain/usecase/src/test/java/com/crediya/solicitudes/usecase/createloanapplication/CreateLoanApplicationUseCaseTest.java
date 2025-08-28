package com.crediya.solicitudes.usecase.createloanapplication;

import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loantype.gateways.LoanTypeRepository;
import com.crediya.solicitudes.model.user.gateways.UserRepository;
import com.crediya.solicitudes.usecase.exceptions.InvalidLoanTypeException;
import com.crediya.solicitudes.usecase.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateLoanApplicationUseCaseTest {

    @Mock
    private LoanApplicationRepository loanApplicationRepository;
    @Mock
    private LoanTypeRepository loanTypeRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateLoanApplicationUseCase createLoanApplicationUseCase;

    private LoanApplication testLoanApplication;

    @BeforeEach
    void setUp() {
        testLoanApplication = LoanApplication.builder()
                .userId("user-123")
                .loanTypeId(1L)
                .amount(new BigDecimal("10000"))
                .term(12)
                .build();
    }

    @Test
    @DisplayName("Should create a loan application successfully when all validations pass")
    void shouldCreateLoanApplicationSuccessfully() {
        when(userRepository.existsById(anyString())).thenReturn(Mono.just(true));
        when(loanTypeRepository.existsById(anyLong())).thenReturn(Mono.just(true));
        when(loanApplicationRepository.save(any(LoanApplication.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0, LoanApplication.class)));

        Mono<LoanApplication> result = createLoanApplicationUseCase.createNewLoanApplication(testLoanApplication);

        StepVerifier.create(result)
                .expectNextMatches(savedApp ->
                        savedApp.getStatusId().equals(1L) &&
                                savedApp.getApplicationDate() != null
                )
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return UserNotFoundException when user does not exist")
    void shouldReturnErrorWhenUserNotFound() {
        when(userRepository.existsById(anyString())).thenReturn(Mono.just(false));

        when(loanTypeRepository.existsById(anyLong())).thenReturn(Mono.just(true));

        Mono<LoanApplication> result = createLoanApplicationUseCase.createNewLoanApplication(testLoanApplication);

        StepVerifier.create(result)
                .expectError(UserNotFoundException.class)
                .verify();
    }

    @Test
    @DisplayName("Should return InvalidLoanTypeException when loan type does not exist")
    void shouldReturnErrorWhenLoanTypeIsInvalid() {
        when(userRepository.existsById(anyString())).thenReturn(Mono.just(true));
        when(loanTypeRepository.existsById(anyLong())).thenReturn(Mono.just(false));

        Mono<LoanApplication> result = createLoanApplicationUseCase.createNewLoanApplication(testLoanApplication);

        StepVerifier.create(result)
                .expectError(InvalidLoanTypeException.class)
                .verify();
    }
}

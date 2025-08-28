package com.crediya.solicitudes.usecase.createloanapplication;

import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loantype.gateways.LoanTypeRepository;
import com.crediya.solicitudes.model.user.gateways.UserRepository;
import com.crediya.solicitudes.usecase.exceptions.InvalidLoanTypeException;
import com.crediya.solicitudes.usecase.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class CreateLoanApplicationUseCase {

    private static final Long PENDING_STATUS_ID = 1L;

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final UserRepository userRepository;

    public Mono<LoanApplication> createNewLoanApplication(LoanApplication loanApplication) {
        return validateUser(loanApplication.getUserId())
                .then(validateLoanType(loanApplication.getLoanTypeId()))
                .flatMap(isValid -> {
                    LoanApplication applicationToSave = loanApplication.toBuilder()
                            .statusId(PENDING_STATUS_ID)
                            .applicationDate(LocalDateTime.now())
                            .build();
                    return loanApplicationRepository.save(applicationToSave);
                });
    }

    private Mono<Boolean> validateUser(String userId) {
        return userRepository.existsById(userId)
                .flatMap(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        return Mono.error(new UserNotFoundException("El usuario con ID " + userId + " no existe."));
                    }
                    return Mono.just(true);
                });
    }

    private Mono<Boolean> validateLoanType(Long loanTypeId) {
        return loanTypeRepository.existsById(loanTypeId)
                .flatMap(exists -> {
                    if (Boolean.FALSE.equals(exists)) {
                        return Mono.error(new InvalidLoanTypeException("El tipo de préstamo con ID " + loanTypeId + " no es válido."));
                    }
                    return Mono.just(true);
                });
    }
}

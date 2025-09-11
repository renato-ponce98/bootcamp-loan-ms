package com.crediya.solicitudes.model.autovalidationevent.gateways;

import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import reactor.core.publisher.Mono;

public interface AutoValidationEventGateway {
    Mono<Void> publishForAutoValidation(LoanApplication loanApplication);
}

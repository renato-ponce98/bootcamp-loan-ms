package com.crediya.solicitudes.config;

import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loantype.gateways.LoanTypeRepository;
import com.crediya.solicitudes.model.user.gateways.UserRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TestUseCaseConfig {

    @Bean
    @Primary
    public LoanApplicationRepository loanApplicationRepository() {
        return Mockito.mock(LoanApplicationRepository.class);
    }

    @Bean
    @Primary
    public LoanTypeRepository loanTypeRepository() {
        return Mockito.mock(LoanTypeRepository.class);
    }

    @Bean
    @Primary
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }
}

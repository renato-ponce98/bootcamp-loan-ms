package com.crediya.solicitudes.config;

import com.crediya.solicitudes.usecase.createloanapplication.CreateLoanApplicationUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UseCasesConfig.class, TestUseCaseConfig.class})
class UseCasesConfigTest {

    @Autowired
    private CreateLoanApplicationUseCase createLoanApplicationUseCase;

    @Test
    void testUseCaseBeansExist() {
        assertNotNull(createLoanApplicationUseCase);
    }
}
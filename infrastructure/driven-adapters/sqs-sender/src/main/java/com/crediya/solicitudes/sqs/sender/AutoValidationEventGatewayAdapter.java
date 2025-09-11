package com.crediya.solicitudes.sqs.sender;

import com.crediya.solicitudes.model.autovalidationevent.gateways.AutoValidationEventGateway;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Service
public class AutoValidationEventGatewayAdapter implements AutoValidationEventGateway {

    private final SqsAsyncClient sqsAsyncClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper;

    public AutoValidationEventGatewayAdapter(SqsAsyncClient sqsAsyncClient,
                                             @Value("${adapters.driven.sqs.queue-url-autovalidate}") String queueUrl) {
        this.sqsAsyncClient = sqsAsyncClient;
        this.queueUrl = queueUrl;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public Mono<Void> publishForAutoValidation(LoanApplication loanApplication) {
        return Mono.fromCallable(() -> {
                    try {
                        return objectMapper.writeValueAsString(loanApplication);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error serializing LoanApplication to JSON", e);
                    }
                })
                .flatMap(messageBody -> {
                    log.info("Publicando solicitud {} para validación automática en SQS.", loanApplication.getId());
                    SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(messageBody)
                            .build();
                    return Mono.fromFuture(sqsAsyncClient.sendMessage(sendMessageRequest));
                })
                .doOnSuccess(response -> log.info("Mensaje enviado a cola de validación automática. MessageId: {}", response.messageId()))
                .doOnError(error -> log.error("Error al publicar mensaje en cola de validación automática", error))
                .then();
    }
}

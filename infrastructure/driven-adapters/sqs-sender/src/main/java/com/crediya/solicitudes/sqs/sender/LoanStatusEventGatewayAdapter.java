package com.crediya.solicitudes.sqs.sender;

import com.crediya.solicitudes.model.loanapplication.LoanApplicationDetail;
import com.crediya.solicitudes.model.loanstatusevent.gateways.LoanStatusEventGateway;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Slf4j
@Service
public class LoanStatusEventGatewayAdapter implements LoanStatusEventGateway {

    private final SqsAsyncClient sqsAsyncClient;
    private final String queueUrl;
    private final ObjectMapper objectMapper;

    public LoanStatusEventGatewayAdapter(SqsAsyncClient sqsAsyncClient, @Value("${adapters.driven.sqs.queue-url}") String queueUrl) {
        this.sqsAsyncClient = sqsAsyncClient;
        this.queueUrl = queueUrl;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    public Mono<Void> publishLoanStatusUpdate(LoanApplicationDetail applicationDetail) {
        return Mono.fromCallable(() -> {
                    try {
                        return objectMapper.writeValueAsString(applicationDetail);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException("Error serializing LoanApplicationDetail to JSON", e);
                    }
                })
                .flatMap(messageBody -> {
                    log.info("Publicando evento de actualización de estado en SQS. Cola: {}", queueUrl);
                    SendMessageRequest sendMessageRequest = SendMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .messageBody(messageBody)
                            .build();
                    return Mono.fromFuture(sqsAsyncClient.sendMessage(sendMessageRequest));
                })
                .doOnSuccess(response -> log.info("Mensaje enviado a SQS con éxito. MessageId: {}", response.messageId()))
                .doOnError(error -> log.error("Error al publicar mensaje en SQS", error))
                .then();
    }
}

package com.crediya.solicitudes.usecase.getloanapplicationsforreview;

import com.crediya.solicitudes.model.common.Pagination;
import com.crediya.solicitudes.model.loanapplication.LoanApplication;
import com.crediya.solicitudes.model.loanapplication.LoanApplicationDetail;
import com.crediya.solicitudes.model.loanapplication.gateways.LoanApplicationRepository;
import com.crediya.solicitudes.model.loantype.LoanType;
import com.crediya.solicitudes.model.loantype.gateways.LoanTypeRepository;
import com.crediya.solicitudes.model.status.gateways.StatusRepository;
import com.crediya.solicitudes.model.userdetail.UserDetail;
import com.crediya.solicitudes.model.userdetail.gateways.UserDetailRepository;
import com.crediya.solicitudes.model.status.Status;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@RequiredArgsConstructor
public class GetLoanApplicationsForReviewUseCase {

    private final LoanApplicationRepository loanApplicationRepository;
    private final StatusRepository statusRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final UserDetailRepository userDetailRepository;

    private static final String APPROVED_STATUS_NAME = "APROBADO";

    public Flux<LoanApplicationDetail> getApplications(List<String> statusNames, Pagination pagination) {
        return statusRepository.findByNameIn(statusNames)
                .map(Status::getId)
                .collectList()
                .flatMapMany(statusIds -> {
                    if (statusIds.isEmpty()) {
                        return Flux.empty();
                    }
                    return loanApplicationRepository.findAllByStatusIdIn(statusIds, pagination);
                })
                .flatMap(this::enrichApplicationWithDetails);
    }

    public Mono<LoanApplicationDetail> enrichApplicationWithDetails(LoanApplication application) {
        Mono<UserDetail> userDetailMono = userDetailRepository.findByIdentityDocument(application.getUserId())
                .defaultIfEmpty(UserDetail.builder().build());
        Mono<BigDecimal> totalMonthlyDebtMono = calculateTotalMonthlyDebt(application.getUserId());
        Mono<LoanType> loanTypeMono = loanTypeRepository.findById(application.getLoanTypeId())
                .defaultIfEmpty(LoanType.builder().name("Desconocido").interestRate(BigDecimal.ZERO).build());
        Mono<Status> statusMono = statusRepository.findById(application.getStatusId())
                .defaultIfEmpty(Status.builder().name("Desconocido").build());

        return Mono.zip(userDetailMono, totalMonthlyDebtMono, loanTypeMono, statusMono)
                .map(tuple -> {
                    UserDetail userDetail = tuple.getT1();
                    BigDecimal totalMonthlyDebt = tuple.getT2();
                    LoanType loanType = tuple.getT3();
                    Status status = tuple.getT4();

                    return LoanApplicationDetail.builder()
                            .id(application.getId())
                            .amount(application.getAmount())
                            .term(application.getTerm())
                            .applicationDate(application.getApplicationDate())
                            .userFullName(userDetail.getFirstName() + " " + userDetail.getLastName())
                            .userEmail(userDetail.getEmail())
                            .userBaseSalary(userDetail.getBaseSalary())
                            .loanTypeName(loanType.getName())
                            .loanInterestRate(loanType.getInterestRate())
                            .statusName(status.getName())
                            .totalMonthlyDebt(totalMonthlyDebt)
                            .build();
                });
    }

    private Mono<BigDecimal> calculateTotalMonthlyDebt(String userId) {
        return loanApplicationRepository.findAllByUserIdAndStatusName(userId, APPROVED_STATUS_NAME)
                .flatMap(loan -> loanTypeRepository.findById(loan.getLoanTypeId())
                        .flatMap(loanType -> calculateMonthlyPayment(loan, loanType))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Mono<BigDecimal> calculateMonthlyPayment(LoanApplication loan, LoanType loanType) {
        return Mono.fromCallable(() -> {
            if (loanType.getInterestRate() == null || loanType.getInterestRate().compareTo(BigDecimal.ZERO) == 0) {
                return loan.getAmount().divide(BigDecimal.valueOf(loan.getTerm()), 2, RoundingMode.HALF_UP);
            }

            BigDecimal monthlyRate = loanType.getInterestRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                    .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

            BigDecimal ratePlusOne = monthlyRate.add(BigDecimal.ONE);
            BigDecimal ratePlusOneToN = ratePlusOne.pow(loan.getTerm());

            BigDecimal numerator = monthlyRate.multiply(ratePlusOneToN);
            BigDecimal denominator = ratePlusOneToN.subtract(BigDecimal.ONE);

            if (denominator.compareTo(BigDecimal.ZERO) == 0) {
                return BigDecimal.ZERO;
            }

            return loan.getAmount().multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);
        });
    }
}

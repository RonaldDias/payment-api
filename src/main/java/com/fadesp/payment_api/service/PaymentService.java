package com.fadesp.payment_api.service;

import com.fadesp.payment_api.dtos.PaymentRequest;
import com.fadesp.payment_api.dtos.StatusUpdateRequest;
import com.fadesp.payment_api.model.Payment;
import com.fadesp.payment_api.model.enums.PaymentMethod;
import com.fadesp.payment_api.model.enums.PaymentStatus;
import com.fadesp.payment_api.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public Payment create(PaymentRequest request) {
        if (isCardMethod(request.getMetodoPagamento()) &&
                (request.getNumeroCartao() == null || request.getNumeroCartao().isBlank())) {
                throw new IllegalArgumentException("Número do cartão é obrigatório para pagamentos com cartão.");
        }

        Payment payment = new Payment();
        payment.setCodigoDebito(request.getCodigoDebito());
        payment.setCpfCnpj(request.getCpfCnpj());
        payment.setMetodoPagamento(request.getMetodoPagamento());
        payment.setNumeroCartao(request.getNumeroCartao());
        payment.setValor(request.getValor());
        payment.setStatus(PaymentStatus.PENDENTE);

        return repository.save(payment);
    }

    public Payment updateStatus(Long id, StatusUpdateRequest request) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado"));

        validateStatusTransition(payment.getStatus(), request.getStatus());
        payment.setStatus(request.getStatus());

        return repository.save(payment);
    }

    public List<Payment> findAll(Integer codigoDebito, String cpfCmpj, PaymentStatus status) {
        return repository.findAll(PaymentSpecification.filter(codigoDebito, cpfCmpj, status));
    }

    public void delete(Long id) {
        Payment payment = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado"));

        if (payment.getStatus() != PaymentStatus.PENDENTE) {
            throw new IllegalStateException("Apenas pagamentos pendentes podem ser excluídos");
        }

        payment.setAtivo(false);
        repository.save(payment);
    }

    private void validateStatusTransition(PaymentStatus current, PaymentStatus next) {
        if (current == PaymentStatus.PROCESSADO_SUCESSO) {
            throw new IllegalStateException("Pagamento já processado com sucesso não pode ser alterado.");
        }
        if (current == PaymentStatus.PROCESSADO_FALHA && next != PaymentStatus.PENDENTE) {
            throw new IllegalStateException("Pagamento com falha só pode voltar para pendente");
        }
    }

    private boolean isCardMethod(PaymentMethod method) {
        return method == PaymentMethod.CARTAO_CREDITO || method == PaymentMethod.CARTAO_DEBITO;
    }
}

package com.fadesp.payment_api.service;

import com.fadesp.payment_api.model.Payment;
import com.fadesp.payment_api.model.enums.PaymentStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class PaymentSpecification {
    public static Specification<Payment> filter(Integer codigoDebito, String cpfCnpj, PaymentStatus status) {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (codigoDebito != null) {
                predicates.add(cb.equal(root.get("codigoDebito"), codigoDebito));
            }

            if (cpfCnpj != null && !cpfCnpj.isBlank()) {
                predicates.add(cb.equal(root.get("cpfCnpj"), cpfCnpj));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        });
    }
}

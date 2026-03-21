package com.fadesp.payment_api.controllers;

import com.fadesp.payment_api.dtos.PaymentRequest;
import com.fadesp.payment_api.dtos.StatusUpdateRequest;
import com.fadesp.payment_api.model.Payment;
import com.fadesp.payment_api.model.enums.PaymentStatus;
import com.fadesp.payment_api.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Payment> create(@RequestBody @Valid PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Payment> updateStatus(@PathVariable Long id,
                                                @RequestBody @Valid StatusUpdateRequest request) {
        return ResponseEntity.ok(service.updateStatus(id, request));
    }

    @GetMapping
    public ResponseEntity<List<Payment>> findAll(
            @RequestParam(required = false) Integer codigoDebito,
            @RequestParam(required = false) String cpfCnpj,
            @RequestParam(required = false) PaymentStatus status) {
        return ResponseEntity.ok(service.findAll(codigoDebito, cpfCnpj, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.dao.Payment;
import com.rofik.miniproject.domain.dto.request.PaymentRequest;
import com.rofik.miniproject.domain.dto.response.PaymentResponse;
import com.rofik.miniproject.repository.PaymentRepository;
import com.rofik.miniproject.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rofik.miniproject.constant.ResponseContant.*;

@Slf4j
@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    public ResponseEntity<Object> createOne(PaymentRequest request) {
        try {
            Optional<Payment> paymentOptional = paymentRepository.findByName(request.getName());
            if (paymentOptional.isPresent())
                return ResponseUtil.build(PAYMENT_EXIST, HttpStatus.BAD_REQUEST);

            Payment payment = new Payment();
            BeanUtils.copyProperties(request, payment);
            payment = paymentRepository.saveAndFlush(payment);

            PaymentResponse response = new PaymentResponse();
            BeanUtils.copyProperties(payment, response);
            return ResponseUtil.build(PAYMENT_CREATED, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error create new payment: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getAll() {
        try {
            log.info("Get all payment");
            List<Payment> paymentList = paymentRepository.findAll();

            List<PaymentResponse> result = new ArrayList<>();
            paymentList.forEach(payment -> result.add(
                    PaymentResponse.builder()
                            .id(payment.getId())
                            .name(payment.getName())
                            .status(payment.getStatus())
                            .build()
            ));

            return ResponseUtil.build(PAYMENT_GET_ALL, HttpStatus.OK, result);
        } catch (Exception e) {
            log.error("Error get all payment: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getOneById(Long id) {
        try {
            Optional<Payment> paymentOptional = paymentRepository.findById(id);

            if (paymentOptional.isEmpty()) return ResponseUtil.build(PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND);

            Payment payment = paymentOptional.get();
            PaymentResponse response = new PaymentResponse();
            BeanUtils.copyProperties(payment, response);

            return ResponseUtil.build(PAYMENT_GET_BY_ID, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error get payment by id: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> updateOne(Long id, PaymentRequest request) {
        try {
            log.info("Update payment {}", id);

            Optional<Payment> paymentOptional = paymentRepository.findById(id);
            if (paymentOptional.isEmpty()) return ResponseUtil.build(PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND);

            Optional<Payment> paymentOptionalByName = paymentRepository.findByNameAndIdNot(request.getName(), id);
            if (paymentOptionalByName.isPresent())
                return ResponseUtil.build(PAYMENT_EXIST, HttpStatus.BAD_REQUEST);

            Payment payment = paymentOptional.get();
            BeanUtils.copyProperties(request, payment);
            payment = paymentRepository.saveAndFlush(payment);

            PaymentResponse response = new PaymentResponse();
            BeanUtils.copyProperties(payment, response);
            return ResponseUtil.build(PAYMENT_UPDATED, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error update payment with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> deleteOne(Long id) {
        try {
            log.info("Delete payment with id {}", id);

            Optional<Payment> paymentOptional = paymentRepository.findById(id);
            if (paymentOptional.isEmpty()) return ResponseUtil.build(PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND);

            paymentRepository.deleteById(id);
            return ResponseUtil.build(PAYMENT_DELETED, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error delete payment with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }

    }
}

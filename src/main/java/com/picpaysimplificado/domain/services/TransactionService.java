package com.picpaysimplificado.domain.transaction;

import com.picpaysimplificado.domain.dtos.TransactionDTO;
import com.picpaysimplificado.domain.repositories.TransactionRepository;
import com.picpaysimplificado.domain.services.UserService;
import com.picpaysimplificado.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private RestTemplate restTemplate;

    public void createTransaction(TransactionDTO transactionDto) throws Exception {

        User sender = this.userService.findUserById(transactionDto.senderId());
        User receiver = this.userService.findUserById(transactionDto.receiverId());

        userService.validateTransaction(sender, transactionDto.value());

        boolean notAuthorized = this.authorizeTransaction(sender, transactionDto.value());
        if(notAuthorized){
            throw new Exception("Transação não autorizada");
        }

        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.value());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDto.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDto.value()));

        transactionRepository.save(transaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

    }


    public boolean authorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if (authorizationResponse.getStatusCode() == HttpStatus.OK ){
            Map<String, Object> responseBody = authorizationResponse.getBody();
            if (responseBody != null && responseBody.containsKey("data")){
                Map<String, Object> data = ( Map<String, Object>) responseBody.get("data");

                Boolean authorization = (Boolean) data.get("authorization");
                return authorization != null && authorization;
            }
        }
        return false;
    }
}

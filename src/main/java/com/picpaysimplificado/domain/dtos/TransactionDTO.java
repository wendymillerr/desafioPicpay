package com.picpaysimplificado.domain.dtos;

import com.picpaysimplificado.domain.user.User;

import java.math.BigDecimal;

public record TransactionDTO (BigDecimal value, Long senderId, Long receiverId){
}

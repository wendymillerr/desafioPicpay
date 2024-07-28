package com.picpaysimplificado.domain.services;


import com.picpaysimplificado.domain.dtos.UserDTO;
import com.picpaysimplificado.domain.repositories.UserRepository;
import com.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.domain.user.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {

        if (sender.getUserType().equals(UserType.MERCHANT)){
            throw new Exception("Usuário do tipo lojisto não está autorizado a realizar transação");
        }
        if (sender.getBalance().compareTo(amount) < 0){
            throw new Exception(("Saldo insuficiente"));
        }

    }

    public User createUser(UserDTO data){
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers(){
       return this.repository.findAll();
    }

    public User findUserById(Long id) throws Exception{
        return this.repository.findUserById(id).orElseThrow(()-> new Exception("Usuário não encontrado"));
    }

    public void saveUser(User user){
        this.repository.save(user);
    }
}

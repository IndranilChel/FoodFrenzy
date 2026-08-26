package com.foodfrenzy.foodfrenzyapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.foodfrenzy.foodfrenzyapp.entities.User;


public interface UserRepository extends CrudRepository<User,Integer> {

    public User findUserByUemail(String email);
}

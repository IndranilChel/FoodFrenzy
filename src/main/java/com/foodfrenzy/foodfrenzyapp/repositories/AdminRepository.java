package com.foodfrenzy.foodfrenzyapp.repositories;

import org.springframework.data.repository.CrudRepository;

import com.foodfrenzy.foodfrenzyapp.entities.Admin;


public interface AdminRepository extends CrudRepository<Admin, Integer>{

    public Admin findByAdminEmail(String email);
}

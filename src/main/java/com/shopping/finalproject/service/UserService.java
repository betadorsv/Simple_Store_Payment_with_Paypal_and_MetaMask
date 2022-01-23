package com.shopping.finalproject.service;

import com.shopping.finalproject.repository.RoleRepository;
import com.shopping.finalproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;



}

package com.example.secure.service;


import com.example.secure.entity.Role;
import com.example.secure.entity.User;
import com.example.secure.repository.RoleRepository;
import com.example.secure.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Stream;


@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found "+ username);
        }
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword()).authorities(user.getRoles()).build();
    }
    @Transactional
    @Override
    public void saveUser(User user,String[] role) {
        if( user.getPassword() == null || user.getPassword() == "" ) {
            user.setPassword(user.getUsername());
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setEnabled(true);
        List <Role> saveList = new ArrayList<>();
        for (String r : role) {
            saveList.add(roleRepository.findByRole(r.trim()));
        }
        user.setRoles(saveList);
        userRepository.save(user);
    }
    @Transactional
    @Override
    public void removeById(Long id) {
        userRepository.deleteById(id);
    }
    @Transactional
    @Override
    public void update(User user, Long id, String[] role) {
        User forUpdateUser = findById(id);
        forUpdateUser.setUsername(user.getUsername());
        forUpdateUser.setLastName(user.getLastName());
        if( user.getPassword() == null || user.getPassword() == "" ) {
            user.setPassword(user.getUsername());
        }
        forUpdateUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        forUpdateUser.setEnabled(true);
        List <Role> updateList = new ArrayList<>();
        for (String r : role) {
            updateList.add(roleRepository.findByRole(r.trim()));
        }
        user.setRoles(updateList);
        forUpdateUser.setRoles(user.getRoles());
    }
    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
package com.app.MediQuirk.services;

import com.app.MediQuirk.model.Role;
import com.app.MediQuirk.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Set<Role> getRolesByIds(List<Long> roleIds) {
        return roleIds.stream().map(id -> roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id))).collect(Collectors.toSet());
    }
}

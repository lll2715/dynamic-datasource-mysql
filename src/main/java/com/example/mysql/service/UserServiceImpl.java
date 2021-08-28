package com.example.mysql.service;

import com.example.mysql.mapper.UmsAdminMapper;
import com.example.mysql.model.UmsAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: liufeixiang
 * @date: 8/24/21 4:40 PM
 */
@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UmsAdminMapper umsAdminMapper;

    @Override
    public UmsAdmin insert(Long id) {
        return umsAdminMapper.selectByPrimaryKey(id);
    }
}

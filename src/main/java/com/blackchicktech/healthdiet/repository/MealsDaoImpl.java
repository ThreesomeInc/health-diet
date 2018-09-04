package com.blackchicktech.healthdiet.repository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MealsDaoImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(MealsDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;


}

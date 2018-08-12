package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.domain.UserDataInfo;
import com.blackchicktech.healthdiet.domain.UserInfo;
import com.blackchicktech.healthdiet.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.StringJoiner;

/**
 * Created by Eric Cen on 2018/8/12.
 */
@Repository
public class UserDaoImpl {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RowMapper rowMapper = new BeanPropertyRowMapper(User.class);

    public void createUser(UserInfo userInfo, UserDataInfo userDataInfo) {
        StringJoiner treatJoiner = new StringJoiner(",");
        userDataInfo.getTreatmentMethod().forEach(treatJoiner::add);

        StringJoiner diseaseJoiner = new StringJoiner(",");
        userDataInfo.getOtherDisease().forEach(diseaseJoiner::add);
        jdbcTemplate.update(
            "INSERT INTO user_tbl VALUES (?, ?, ?, ?, ?, ? ,?, ?, ?)",
                userInfo.getOpenId(),
                userDataInfo.getGender(),
                userDataInfo.getBirthDay(),
                userDataInfo.getHeight(),
                userDataInfo.getWeight(),
                userDataInfo.getSportRate(),
                userDataInfo.getNephroticPeriod(),
                treatJoiner.toString(),
                diseaseJoiner.toString()
        );
    }

    public User getUserByOpenId(String openId){
        List<User> users = jdbcTemplate.query("SELECT * from user_tbl where open_id = " + openId, rowMapper);
        if(users != null){
            return users.get(0);
        }
        return null;

    }
}

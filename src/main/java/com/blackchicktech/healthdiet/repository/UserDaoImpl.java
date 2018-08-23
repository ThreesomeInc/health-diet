package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.domain.UserDataInfo;
import com.blackchicktech.healthdiet.domain.UserInfo;
import com.blackchicktech.healthdiet.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Created by Eric Cen on 2018/8/12.
 */
@Repository
public class UserDaoImpl {

	private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private RowMapper rowMapper = new BeanPropertyRowMapper(User.class);

	public void createUser(UserInfo userInfo, UserDataInfo userDataInfo) {
		StringJoiner treatJoiner = new StringJoiner(",");
		userDataInfo.getTreatmentMethod().forEach(treatJoiner::add);

		StringJoiner diseaseJoiner = new StringJoiner(",");
		userDataInfo.getOtherDisease().forEach(diseaseJoiner::add);

		StringJoiner irritabilityJoiner = new StringJoiner(",");
		userDataInfo.getIrritability().forEach(irritabilityJoiner::add);
		logger.info("Going to insert user data openId={}, gender={}, birthday={}, height={}, weight={}, sportRate={}, nephroticPeriod={}",
				userInfo.getOpenId(),
				userDataInfo.getGender(),
				userDataInfo.getBirthDay(),
				userDataInfo.getHeight(),
				userDataInfo.getWeight(),
				userDataInfo.getSportRate(),
				userDataInfo.getNephroticPeriod());
		logger.info("Going to insert user data treat={},", treatJoiner.toString());
		logger.info("Going to insert user data disease={},", diseaseJoiner.toString());
		logger.info("Going to insert user data irritability={},", irritabilityJoiner.toString());
		jdbcTemplate.update(
				"REPLACE INTO user_tbl VALUES (?, ?, ?, ?, ?, ? ,?, ?, ?, ?)",
				userInfo.getOpenId(),
				userDataInfo.getGender(),
				userDataInfo.getBirthDay(),
				userDataInfo.getHeight(),
				userDataInfo.getWeight(),
				userDataInfo.getSportRate(),
				userDataInfo.getNephroticPeriod(),
				treatJoiner.toString(),
				diseaseJoiner.toString(),
				irritabilityJoiner.toString()
		);
	}

	public User getUserByOpenId(String openId) {
		List<User> users = jdbcTemplate.<User>query("SELECT * FROM user_tbl WHERE open_id = ?", rowMapper, openId);
		return users.stream().findFirst().orElse(null);
	}
}

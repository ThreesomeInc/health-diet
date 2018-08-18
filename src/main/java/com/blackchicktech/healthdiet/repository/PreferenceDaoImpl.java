package com.blackchicktech.healthdiet.repository;

import com.blackchicktech.healthdiet.entity.Preference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PreferenceDaoImpl extends AbstractDao<Preference> {

	@Override
	public Preference entitySupplier() {
		return new Preference();
	}

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Preference> listByUserId(String openId) {
		return jdbcTemplate.query("SELECT * FROM user_preference WHERE user_open_id = ?", rowMapper, openId);
	}

	public Preference getPreference(String openId, String foodId) {
		return jdbcTemplate.query("SELECT p.*,f.food_name FROM user_preference p " +
						"LEFT JOIN food_tbl f ON f.food_id=p.food_id " +
						"WHERE p.user_open_id = ? AND p.food_id = ?",
				rowMapper, openId, foodId).stream().findFirst().orElse(null);
	}

	public void savePreference(Preference preference) {
		jdbcTemplate.update("INSERT INTO user_preference(user_open_id, food_id, frequency) VALUES (?,?,?) " +
						"ON DUPLICATE KEY UPDATE frequency = VALUES(frequency)",
				preference.getUserOpenId(), preference.getFoodId(), preference.getFrequency());
	}
}

package com.blackchicktech.healthdiet.service;

import com.blackchicktech.healthdiet.domain.PreferenceResponse;
import com.blackchicktech.healthdiet.entity.Preference;
import com.blackchicktech.healthdiet.repository.PreferenceDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PreferenceService {
	@Autowired
	private PreferenceDaoImpl preferenceDao;

	public PreferenceResponse save(Preference preference) {
		preferenceDao.savePreference(preference);
		PreferenceResponse response = new PreferenceResponse();
		response.setMessage("更新成功！");
		return response;
	}

	public PreferenceResponse listPreference(String userId, String itemId, String type) {
		Preference preference = preferenceDao.getPreference(userId, itemId, type);
		PreferenceResponse response = new PreferenceResponse();
		Optional.ofNullable(preference).ifPresent(item -> response.setPreference(preference.getFrequency()));
		return response;
	}
}

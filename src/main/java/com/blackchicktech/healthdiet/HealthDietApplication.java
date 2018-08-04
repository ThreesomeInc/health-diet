package com.blackchicktech.healthdiet;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.sql.DataSource;

@SpringBootApplication
//@MapperScan("com.blackchicktech.healthdiet.repository.*")
public class HealthDietApplication {

//	@Bean
//	public SqlSessionFactoryBean getMybatisFactoryBean(@Qualifier("dataSource") DataSource dataSource,
//													   @Qualifier("${mybatis-org.mybatis.spring.boot.autoconfigure.MybatisProperties}") MybatisProperties mybatisProperties) {
//		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
//		factoryBean.setDataSource(dataSource);
//		factoryBean.setMapperLocations(ResouceLocation);
//
//	}

	

	@Bean
	public View jsonView() {
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		view.setPrettyPrint(true);
		return view;
	}

	public static void main(String[] args) {
		SpringApplication.run(HealthDietApplication.class, args);
	}
}

package com.blackchicktech.healthdiet.util.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket createRestApi() {
		List<Parameter> pars = new ArrayList<>();
//		ParameterBuilder appIdPt = new ParameterBuilder();
//		appIdPt.name("appId").description("appId").modelRef(new ModelRef("string")).parameterType("header").required(true).defaultValue("ops_k8s_orchestration_system");
//		pars.add(appIdPt.build());
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.blackchicktech.healthdiet.controller"))
				.paths(PathSelectors.any())
				.build()
				.globalOperationParameters(pars);
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Health diet backend")
				.description("Health diet backend")
				.contact(new Contact("SaraQian", null, "abigail830@163.com"))
				.version("1.0")// 版本号
				.build();
	}
}
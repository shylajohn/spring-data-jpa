/*
 * Copyright 2014-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.jpa.repository.support;

import static org.assertj.core.api.Assertions.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.persistence.EntityManager;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Integration tests for {@link EntityManagerBeanDefinitionRegistrarPostProcessor}.
 *
 * @author Oliver Gierke
 * @author Jens Schauder
 * @author Réda Housni Alaoui
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class EntityManagerBeanDefinitionRegistrarPostProcessorIntegrationTests {

	@Autowired EntityManagerInjectionTarget target;

	@Test // DATAJPA-445
	void injectsEntityManagerIntoConstructors() {

		assertThat(target).isNotNull();
		assertThat(target.firstEm).isNotNull();
		assertThat(target.primaryEm).isNotNull();
	}

	/**
	 * Annotation to demarcate test components.
	 *
	 * @author Oliver Gierke
	 */
	@Component
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	private static @interface TestComponent {

	}

	@Configuration
	@ImportResource("classpath:infrastructure.xml")
	@ComponentScan(includeFilters = @Filter(TestComponent.class), useDefaultFilters = false)
	static class Config {

		@Autowired DataSource dataSource;
		@Autowired JpaVendorAdapter vendorAdapter;

		@Bean
		public static EntityManagerBeanDefinitionRegistrarPostProcessor processor() {
			return new EntityManagerBeanDefinitionRegistrarPostProcessor();
		}

		private LocalContainerEntityManagerFactoryBean emf() {

			LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
			factoryBean.setPersistenceUnitName("spring-data-jpa");
			factoryBean.setDataSource(dataSource);
			factoryBean.setJpaVendorAdapter(vendorAdapter);

			return factoryBean;
		}

		@Bean
		LocalContainerEntityManagerFactoryBean firstEmf() {
			return emf();
		}

		@Bean
		LocalContainerEntityManagerFactoryBean secondEmf() {
			return emf();
		}

		@Primary
		@Bean
		LocalContainerEntityManagerFactoryBean thirdEmf() {
			return emf();
		}
	}

	@TestComponent
	static class EntityManagerInjectionTarget {

		private final EntityManager firstEm;
		private final EntityManager primaryEm;

		@Autowired
		public EntityManagerInjectionTarget(@Qualifier("firstEmf") EntityManager firstEm, EntityManager primaryEm) {

			this.firstEm = firstEm;
			this.primaryEm = primaryEm;
		}
	}
}

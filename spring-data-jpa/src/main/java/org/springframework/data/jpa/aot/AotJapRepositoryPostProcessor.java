/*
 * Copyright 2022 the original author or authors.
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
package org.springframework.data.jpa.aot;

import org.springframework.aot.generator.CodeContribution;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.data.aot.AotContributingRepositoryBeanPostProcessor;
import org.springframework.data.aot.AotRepositoryContext;
import org.springframework.data.aot.RepositoryBeanContribution;

/**
 * @author Christoph Strobl
 * @since 2022/04
 */
public class AotJapRepositoryPostProcessor extends AotContributingRepositoryBeanPostProcessor {

	@Override
	public RepositoryBeanContribution contribute(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
		return super.contribute(beanDefinition, beanType, beanName);
	}

	@Override
	protected void contribute(AotRepositoryContext ctx, CodeContribution contribution) {


	}


}

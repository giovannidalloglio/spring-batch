/*
 * Copyright 2006-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.batch.core.test.repository;

import static org.junit.Assert.assertEquals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Add Value S.p.A. by Giovanni Dall'Oglio Risso
 * 
 */
@ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class IdentityJobParametersIntegrationTests {
	/** Logger */
	private final Log logger = LogFactory.getLog(getClass());

	private ClassPathXmlApplicationContext context;
	private JobLauncher jobLauncher;
	private Job job;

	@Before
	public void before() {
		context = new ClassPathXmlApplicationContext(getClass().getSimpleName()
				+ "-context.xml", getClass());
		jobLauncher = (JobLauncher) context.getBean("jobLauncher",
				JobLauncher.class);
		job = (Job) context.getBean("job", Job.class);
	}

	@Test
	public void launchJob() throws Throwable {

		JobParameters jobParameters = new JobParametersBuilder()
				.addString("foo", "aaa")
				.addString("bar", "bbb")
				.addString("-foo", "ccc")
				.toJobParameters();

		JobExecution execution = launch(jobParameters);
		assertEquals(BatchStatus.COMPLETED, execution.getStatus());
		
	}

	@Test(expected=JobInstanceAlreadyCompleteException.class)
	public void reLaunchJob() throws Throwable {

		JobParameters jobParameters = new JobParametersBuilder()
				.addString("foo", "aaa")
				.addString("bar", "bbb")
				.addString("-foo", "ccc")
				.toJobParameters();

		launch(jobParameters);

	}

	private JobExecution launch(JobParameters jobParameters)
			throws JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		
		return jobLauncher.run(job, jobParameters);
	}
}

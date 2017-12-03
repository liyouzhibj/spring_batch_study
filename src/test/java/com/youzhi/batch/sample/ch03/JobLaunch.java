/**
 * 
 */
package com.youzhi.batch.sample.ch03;

import com.youzhi.batch.sample.ch03.config.CreditBatchConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@ComponentScan("com.youzhi.batch.sample.config")
public class JobLaunch {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		ApplicationContext context = new ClassPathXmlApplicationContext(
//				"/ch03/sample-context.xml");
//		JobLauncher launcher = (JobLauncher) context.getBean("jobLauncher");
//		Job job = (Job) context.getBean("billJob");
//		JobLaunch jobLaunch = new JobLaunch();
		AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext(CreditBatchConfiguration.class);
		JobLauncher launcher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) context.getBean("billJob");
		// ((GetApplicationContext)context.getBean("getApplicationContext")).aaa();
		//System.out.println( context.getBean("importUserJob"));

		try {
			JobExecution result = launcher.run(job, new JobParameters());
			System.out.println(result.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

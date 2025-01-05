package com.aws.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

/**
 * Configuration class for AWS S3 settings.
 */

@Configuration
public class AwsS3Config {

	@Value("${AWS_API_KEY}")
	private String accessKey;

	@Value("${AWS_SECRET_KEY}")
	private String secretKey;

	@Value("${AWS_REGION}")
	private String region;

	 /**
     * Creates and configures an Amazon S3 client bean.
     *
     * @return AmazonS3 instance configured with AWS credentials and region.
     */
	
	@Bean
	public AmazonS3 amazonS3() {
		BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

		return AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
				.withRegion(region).build();
	}
}

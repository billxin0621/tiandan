package com.sinosoft.fragins.framework.utils;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.apache.commons.lang3.StringUtils;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class S3FileStorage extends FileStorageSupport {

	private String endpoint;
	private String accessKeyId;
	private String accessKeySecret;
	private String bucketName;
	private String region;

	private AmazonS3 s3Client;

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKeyId() {
		return accessKeyId;
	}

	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}

	public String getAccessKeySecret() {
		return accessKeySecret;
	}

	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	@Override
	public void save(String fileId, InputStream in) {
		ObjectMetadata metaData = new ObjectMetadata();
		metaData.setLastModified(new Date());
		// 个性标签数据
		Map<String, String> userMetadata = new HashMap<>();
		userMetadata.put("From", "FRAGINS");
		metaData.setUserMetadata(userMetadata);
		PutObjectRequest objectRequest = new PutObjectRequest(bucketName, fileId, in, metaData);
		getClient().putObject(objectRequest);
	}

	@Override
	public InputStream loadAsStream(String fileId) {
		return getClient().getObject(new GetObjectRequest(bucketName, fileId)).getObjectContent();
	}

	public AmazonS3 getClient() {
		if (s3Client == null) {
			/** 区域 */
			if (StringUtils.isBlank(region)) {
				// 杉岩的S3存储没有region，用特殊的方法构建
				AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, accessKeySecret);
				ClientConfiguration clientConfig = new ClientConfiguration();
				clientConfig.setProtocol(Protocol.HTTP);
				clientConfig.setSignerOverride("S3SignerType");
				s3Client = new AmazonS3Client(credentials, clientConfig);
				s3Client.setEndpoint(endpoint);
				s3Client.setS3ClientOptions(
						S3ClientOptions.builder().setPathStyleAccess(true).disableChunkedEncoding().build());
			} else {
				// S3标准构建客户端
				ClientConfiguration config = new ClientConfiguration();
				config.setProtocol(Protocol.HTTP);
				AwsClientBuilder.EndpointConfiguration endpointConfig = new AwsClientBuilder.EndpointConfiguration(
						endpoint, region);
				AWSCredentials awsCredentials = new BasicAWSCredentials(accessKeyId, accessKeySecret);
				AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);

				s3Client = AmazonS3Client.builder().withEndpointConfiguration(endpointConfig)
						.withClientConfiguration(config).withCredentials(awsCredentialsProvider)
						.disableChunkedEncoding().withPathStyleAccessEnabled(true).build();
			}

		}
		return s3Client;
	}

	@Override
	public String saveByUrl(String fileId, InputStream in) {
		ObjectMetadata metaData = new ObjectMetadata();
		metaData.setLastModified(new Date());
		// 个性标签数据
		Map<String, String> userMetadata = new HashMap<>();
		userMetadata.put("From", "FRAGINS");
		metaData.setUserMetadata(userMetadata);
		PutObjectRequest objectRequest = new PutObjectRequest(bucketName, fileId, in, metaData);
		getClient().putObject(objectRequest);
		GeneratePresignedUrlRequest urlRequest = new GeneratePresignedUrlRequest(
				bucketName,  fileId);
		//生成公用的url
		java.util.Date expiration = new java.util.Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000L * 60 * 60 * 24 * 7;
		expiration.setTime(expTimeMillis);
		urlRequest.setExpiration(expiration);
		URL url = getClient().generatePresignedUrl(urlRequest);
		return url.toString();
	}
}

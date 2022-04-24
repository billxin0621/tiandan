package com.sinosoft.fragins.framework.utils;

import java.io.InputStream;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;

public class OssFileStorage extends FileStorageSupport {

	private String endpoint;
	private String accessKeyId;
	private String accessKeySecret;
	private String bucketName;

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

	@Override
	public void save(String fileId, InputStream in) {
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		String key = fileId;
		try {
			if (!ossClient.doesBucketExist(bucketName)) {
				ossClient.createBucket(bucketName);
			}
			ossClient.putObject(bucketName, key, in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public InputStream loadAsStream(String fileId) {
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		try {
			if (!ossClient.doesBucketExist(bucketName) || !ossClient.doesObjectExist(bucketName, fileId)) {
				throw new RuntimeException("无法加载数据: " + fileId);
			}
			OSSObject ossObject = ossClient.getObject(bucketName, fileId);
			return ossObject.getObjectContent();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String saveByUrl(String fileId, InputStream in) {
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		String key = fileId;
		try {
			if (!ossClient.doesBucketExist(bucketName)) {
				ossClient.createBucket(bucketName);
			}
			ossClient.putObject(bucketName, key, in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return "";
	}
}

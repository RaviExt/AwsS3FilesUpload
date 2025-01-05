package com.aws.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
	/**
	 * Uploads a single file to the storage system.
	 *
	 * @param file The MultipartFile representing the file to be uploaded.
	 * @return A public URL of the uploaded file.
	 */
	String uploadFile(MultipartFile file);
}
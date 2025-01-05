package com.aws.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.Instant;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

/**
 * Service class for handling file-related operations using AWS S3.
 */
@Service
public class AWSS3Service implements FileService {

	@Autowired
	private AmazonS3 amazonS3;

	@Value("${AWS_BUCKET}")
	private String bucketName;

	@Value("${minio.returnurl}")
	private String returnUrl;

	/**
	 * Uploads a single file to AWS S3.
	 *
	 * @param file The file to be uploaded
	 * @return The public URL of the uploaded file
	 */
	@Override
	public String uploadFile(MultipartFile file) {
		if (file != null) {
			String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
			String originalFileName = FilenameUtils.getBaseName(file.getOriginalFilename());
			String timestamp = String.valueOf(Instant.now().toEpochMilli());
			String shortTimestamp = timestamp.length() > 10 ? timestamp.substring(timestamp.length() - 8) : timestamp;
			String key = originalFileName + "_" + shortTimestamp + "." + filenameExtension;
			ObjectMetadata metaData = new ObjectMetadata();
			metaData.setContentLength(file.getSize());
			metaData.setContentType(file.getContentType());
			try {
				amazonS3.putObject(bucketName, key, file.getInputStream(), metaData);
			} catch (IOException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
						"An exception occurred while uploading the file");
			}
			// return amazonS3.getUrl(bucketName, key).toString();

			return returnUrl + key;

		} else {
			return null;
		}
	}

	/**
	 * Retrieves a file from the AWS S3 bucket based on the provided key.
	 *
	 * @param key The key of the file to retrieve.
	 * @return The InputStream containing the file data.
	 * @throws ResponseStatusException If an error occurs during file retrieval.
	 */
	public S3ObjectInputStream getFile(String key) {
		try {
			S3Object s3Object = amazonS3.getObject(bucketName, key);
			return s3Object.getObjectContent();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An exception occurred while fetching the file", e);
		}
	}

	/**
	 * Deletes an image from AWS S3 based on the image path.
	 *
	 * @param imagePath The path of the image to be deleted
	 */
	public void deleteImage(String imagePath) {
		try {
			String key = extractKeyFromImagePath(imagePath);
			amazonS3.deleteObject(bucketName, key);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error while deleting the image");
		}
	}

	/**
	 * Extracts the key (object name) from the image path.
	 * 
	 * @param imagePath The image path from which to extract the key
	 * @return The key (object name) extracted from the image path
	 */
	private String extractKeyFromImagePath(String imagePath) {
		return imagePath.substring(imagePath.lastIndexOf('/') + 1);
	}

	public InputStream getFileInputStream(String fileName) {
		try {
			S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucketName, fileName));
			return s3Object.getObjectContent();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"An error occurred while retrieving the file");
		}
	}

	/**
	 * Downloads a file from AWS S3 and returns it as an InputStream.
	 *
	 * @param fileName The name of the file in the S3 bucket
	 * @return The InputStream of the file
	 * @throws ResponseStatusException If an error occurs while downloading the file
	 */
	public InputStream downloadFile(String fileName) {
		try {
			S3Object s3Object = amazonS3.getObject(bucketName, fileName);
			return s3Object.getObjectContent();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Error while downloading the file from S3", e);
		}
	}

	public ResponseEntity<InputStreamResource> getFile(String filePath, String token) {
		try {

//				boolean status = jwtService.validateToken(token);
//				if (status) {
//
//					InputStream inputStream = null;
//					if (!apiResponseConfig.isAmazonS3Active()) {
//						inputStream = minioService.getFile(filePath);
//					} else {
//						inputStream = awss3Service.downloadFile(filePath);
//					}

			boolean status = true;
			if (status) {
				InputStream inputStream = downloadFile(filePath);

				MediaType contentType = FileUtil.determineContentType(filePath);
				if (contentType == null) {
					contentType = MediaType.APPLICATION_OCTET_STREAM;
				}

				// Add Content-Disposition header to suggest filename
				String fileName = Paths.get(filePath).getFileName().toString();
				return ResponseEntity.ok().contentType(contentType)
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
						.body(new InputStreamResource(inputStream));
			} else {
				return ResponseEntity.badRequest().build();
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve the file", e);
		}
	}
}

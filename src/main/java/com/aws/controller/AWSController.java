package com.aws.controller;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aws.service.AWSS3Service;

@RestController
@RequestMapping("/aws")
public class AWSController {

	@Autowired
	private AWSS3Service awss3Service;

	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			String fileUrl = awss3Service.uploadFile(file);
			return new ResponseEntity<>(fileUrl, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("File upload failed", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/file/{key}")
	public ResponseEntity<InputStream> getFile(@PathVariable String key) {
		try {
			InputStream fileStream = awss3Service.getFileInputStream(key);
			return new ResponseEntity<>(fileStream, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<String> deleteImage(@RequestParam("imagePath") String imagePath) {
		try {
			awss3Service.deleteImage(imagePath);
			return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error while deleting the image", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/download/{fileName}")
	public ResponseEntity<InputStream> downloadFile(@PathVariable String fileName) {
		try {
			InputStream fileStream = awss3Service.downloadFile(fileName);
			return new ResponseEntity<>(fileStream, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/{filePath}/{token}")
	public ResponseEntity<InputStreamResource> getFile(@PathVariable String filePath, @PathVariable String token) {

		return awss3Service.getFile(filePath, token);

	}
}

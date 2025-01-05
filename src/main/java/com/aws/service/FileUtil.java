package com.aws.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;

public class FileUtil {
	private static final Map<String, MediaType> MIME_TYPES;

	static {
		MIME_TYPES = new HashMap<>();
		// Images
		MIME_TYPES.put("jpg", MediaType.IMAGE_JPEG);
		MIME_TYPES.put("jpeg", MediaType.IMAGE_JPEG);
		MIME_TYPES.put("png", MediaType.IMAGE_PNG);
		MIME_TYPES.put("gif", MediaType.IMAGE_GIF);
		MIME_TYPES.put("bmp", MediaType.parseMediaType("image/bmp"));
		MIME_TYPES.put("webp", MediaType.parseMediaType("image/webp"));
		// Documents
		MIME_TYPES.put("pdf", MediaType.parseMediaType("application/pdf"));
		MIME_TYPES.put("doc", MediaType.parseMediaType("application/msword"));
		MIME_TYPES.put("docx",
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
		MIME_TYPES.put("xls", MediaType.parseMediaType("application/vnd.ms-excel"));
		MIME_TYPES.put("xlsx",
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
		MIME_TYPES.put("ppt", MediaType.parseMediaType("application/vnd.ms-powerpoint"));
		MIME_TYPES.put("pptx",
				MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.presentationml.presentation"));
		// Audio
		MIME_TYPES.put("mp3", MediaType.parseMediaType("audio/mpeg"));
		MIME_TYPES.put("wav", MediaType.parseMediaType("audio/wav"));
		MIME_TYPES.put("ogg", MediaType.parseMediaType("audio/ogg"));
		MIME_TYPES.put("flac", MediaType.parseMediaType("audio/flac"));
		// Video
		MIME_TYPES.put("mp4", MediaType.parseMediaType("video/mp4"));
		MIME_TYPES.put("mov", MediaType.parseMediaType("video/quicktime"));
		MIME_TYPES.put("wmv", MediaType.parseMediaType("video/x-ms-wmv"));
		// Text
		MIME_TYPES.put("txt", MediaType.TEXT_PLAIN);
		MIME_TYPES.put("csv", MediaType.parseMediaType("text/csv"));
		MIME_TYPES.put("xml", MediaType.APPLICATION_XML);
		// Compressed
		MIME_TYPES.put("zip", MediaType.parseMediaType("application/zip"));
		MIME_TYPES.put("rar", MediaType.parseMediaType("application/x-rar-compressed"));
		MIME_TYPES.put("7z", MediaType.parseMediaType("application/x-7z-compressed"));
	}

	/**
	 * Determines the MediaType based on the file extension of the given fileName.
	 *
	 * @param fileName The name of the file including extension.
	 * @return The MediaType corresponding to the file extension, or
	 *         MediaType.APPLICATION_OCTET_STREAM if not found.
	 */
	public static MediaType determineContentType(String fileName) {
		String extension = StringUtils.getFilenameExtension(fileName);
		if (extension != null && MIME_TYPES.containsKey(extension.toLowerCase())) {
			return MIME_TYPES.get(extension.toLowerCase());
		}
		return MediaType.APPLICATION_OCTET_STREAM;
	}
}

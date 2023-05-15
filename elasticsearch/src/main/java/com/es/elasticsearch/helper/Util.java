package com.es.elasticsearch.helper;

import java.io.File;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

public class Util {
	private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

	public static String loadAsString(final String path) {
		try {
			final File resource = new ClassPathResource(path).getFile();
			return new String(Files.readAllBytes(resource.toPath()));
		} catch (final Exception e) {
			LOGGER.error(e.getMessage(), e);
			return null;
		}
	}
}

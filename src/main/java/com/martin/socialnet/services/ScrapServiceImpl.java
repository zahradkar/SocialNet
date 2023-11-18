package com.martin.socialnet.services;

import com.martin.socialnet.dtos.ScraperResponseDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ScrapServiceImpl implements ScrapService {
	private static final Logger logger = LoggerFactory.getLogger(ScrapServiceImpl.class);

	@Override
	public ScraperResponseDTO getWebpageInfo(String url) throws IOException { // http://localhost:8080/scrap
		// TODO method in development
		logger.debug("Received url: " + url);

		Document document;
		try {
			document = Jsoup.connect(url).get();
		} catch (IOException exception) {
			throw new IOException("URL does not exist!");
		}

		String title = document.title();
		String description = document.select("meta[name=description]").attr("content");
		String image = document.select("meta[property=og:image]").attr("content");


		if (title.isEmpty()) {
			title = document.select("meta[property=og:title]").attr("content");
			if (title.isEmpty())
				title = document.select("meta[name=twitter:title]").attr("content");
		}

		if (description.isEmpty()) {
			description = document.select("meta[property=og:description]").attr("content");
			if (description.isEmpty())
				description = document.select("meta[name=twitter:description]").attr("content");
		}

		// TODO update search for an image
		if (image.isEmpty()) {
			image = document.select("meta[name=twitter:image]").attr("content");
			if (image.isEmpty())
				image = "Not detected!";
		}

		logger.debug("title: " + title);
		logger.debug("description: " + description);
		logger.debug("image: " + image);

		return new ScraperResponseDTO(title, description, image);
	}
}

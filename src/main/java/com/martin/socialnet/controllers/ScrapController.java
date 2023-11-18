package com.martin.socialnet.controllers;

import com.martin.socialnet.dtos.ScraperResponseDTO;
import com.martin.socialnet.dtos.UrlDTO;
import com.martin.socialnet.services.ScrapService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
/*
 * web scraping metadata from received url
 */

@RestController
public class ScrapController {
	ScrapService scrapService;

	public ScrapController(ScrapService scrapService) {
		this.scrapService = scrapService;
	}

	@PostMapping("/scrap")
	public ResponseEntity<ScraperResponseDTO> scrap(@RequestBody UrlDTO data) throws IOException {
		return ResponseEntity.ok(scrapService.getWebpageInfo(data.url()));
	}
}

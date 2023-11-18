package com.martin.socialnet.services;

import com.martin.socialnet.dtos.ScraperResponseDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ScrapService {
	ScraperResponseDTO getWebpageInfo(String URL) throws IOException;
}

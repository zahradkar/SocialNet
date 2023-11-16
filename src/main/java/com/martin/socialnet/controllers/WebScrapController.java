package com.martin.socialnet.controllers;

import com.martin.socialnet.opengraph.OpenGraph;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebScrapController {
	@GetMapping("/parse")
	public void parse() throws Exception {
		// todo METHOD UNDER DEVELOPMENT ;)
		// source: https://github.com/johndeverall/opengraph-java
//		OpenGraph testPage1 = new OpenGraph("https://stackoverflow.com/", true);
		OpenGraph testPage1 = new OpenGraph("https://ibm.com/", true);
//		OpenGraph testPage1 = new OpenGraph("https://www.shmu.sk/", true);
		System.out.println(testPage1.getContent("title"));
		System.out.println(testPage1.getContent("description"));
		System.out.println(testPage1.getContent("type"));
		System.out.println(testPage1.getContent("image"));
	}
}

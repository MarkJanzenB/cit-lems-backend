package com.capstone.LEMS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(LemsApplication.class, args);
		System.out.println("backend is up and running :)");
	}
}
/*
 *                  HOUSE RULES
 * -----------------------------------------------
 * 1. Name your entity attributes in simple terms
 * 		ex. item.getItemId() this is redundant and very long to read and type. We already know we are getting it's id no need to repeat it.
 * 			item.getId() here it is much simple
 * 
 * 2. Use cammel case
 * 		-To avoid database naming conflict
 * 
 * 3. Do not generate constructors with attributes unless necessary
 * 4. Do not use Query annotations unless JPA query could not handle it
 * 		-JPA query is more readable than SQL query
 * 		-unless you are very good at it
 * 
 * 5. Use enums on non manually changable attributes
 * 		ex. Item only has "Available", "Damage", "Missing", and "In-use" status.
 * 			With enums we can easily know what we need to put in item.status
 * 
 * 6. If your logic is being repeated 2 times, turn it to a different method to lessen lines and redundancy
 * 		-Notice the pattern here?
 * 		-Yes I hate redundancy, anyone who does it deserves to be in jail
 * 
 * 7. Delete logs before committing
 * 		-Logs can be annoying
 * 		-Imagine if we have hundreds of users, logs will pile up
 * 
 * 8. Describe your logic with comments for easy navigation and maintenance
 * 		-This has been proven and tested. Ask Marks
 * 
 * 9. Do not use multiple params, use DTO instead
 * 		ex. (@RequestParam name, @RequestParam itemName, @RequestParam borrowStatus, @RequestParam quantity, @RequestParam whateverdafaq) this is unreadable, nightmare to maintain and can cause migrane
 * 			(addingToHistoryDTO dto) readable and scalable
 * 
 * 10. Always use DTO instead of Map<>
 * 		-Map<> can cause unncessary errors
 * 
 * 11. Use your logic in service instead of controller if applicable
 * 12. Do not stack your changes then put them in 1 commit
 * 		-Reading a commit with thousands of lines and file changes is a nightmare
 * 		-Instead commit every working methods, files, or features.
 * 
 * 13. Push your commits at the end of the day or if your feature is working, never push with errors.
 * 14. Use descriptive commit header.
 * 		ex. May 9, 2025 update - hard to navigate, need to memorize what this update is.
 * 			Add /register end point in JWT config - easy to navigate and familiarize
 * 
 * 15. You can add random comments anywhere you like, express your self, treat it like a freedom wall.
 * 16. State your sources
 * 		-Copied your logic elsewhere?
 * 		-Provide the links
 * 		-ex. logic from stackoverflow
 * 
 * 17. Avoid soulless AI generated comments
 * 
 * you can add your own house rules here
 * previous update: May 9, 2025
 * latest update: May 9, 2025
 * */
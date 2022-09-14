package pt.upbank.upbank.controllers;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

public class UserController {
        
        //welcomePage() will handle all client requests which are using “/” URI.
        @RequestMapping(value = { "/"}, method = RequestMethod.GET)
        public ModelAndView welcomePage() {
                ModelAndView model = new ModelAndView();
                model.setViewName("welcomePage");
                return model;
        }

        //homePage() will handle all client requests which are using “/homePage” URI.
        @RequestMapping(value = { "/homePage"}, method = RequestMethod.GET)
	public ModelAndView homePage() {
		ModelAndView model = new ModelAndView();
		model.setViewName("homePage");
		return model;
	}
	
        //loginPage() will handle all client requests which are using “/loginPage” URI.
        //In loginPage(), we have take care of handling error and logout messages.
	@RequestMapping(value = "/loginPage", method = RequestMethod.GET)
	public ModelAndView loginPage(@RequestParam(value = "error",required = false) String error,
	@RequestParam(value = "logout",	required = false) String logout) {
		
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error", "Invalid Credentials provided.");
		}

		if (logout != null) {
			model.addObject("message", "Logged out from UpBank successfully.");
		}

		model.setViewName("loginPage");
		return model;
	}
    
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
        // @PostMapping("/signin")
        // public String (login
        // @RequestParam String username, @RequestParam String password) {
        //         return ;

            //Check user name  
            //Check password 

    // }

    

    // @PostMapping("/signup")

            //Check access 
            //Check user 

    // }
    
}

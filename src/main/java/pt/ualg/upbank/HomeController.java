package pt.ualg.upbank;

import org.springframework.stereotype.Controller;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
public class HomeController {
    @GetMapping("/")
    @ResponseBody
    public String index() {
			Resource resource = new ClassPathResource("/static/api-reference.json");
			try {
					return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
			} catch (IOException e) {
					e.printStackTrace();
			}
			return "Could not find API reference";
    }
}

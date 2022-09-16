package pt.ualg.upbank;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class HomeController {

    @GetMapping("/")
    @ResponseBody
    public String index() {
				Resource resource = new ClassPathResource("/static/api-reference.json");
				try {
						ObjectMapper mapper = new ObjectMapper();
						return mapper.readValue(resource.getInputStream(), Object.class);
				} catch (IOException e) {
						e.printStackTrace();
				}
				return null;
    }

}

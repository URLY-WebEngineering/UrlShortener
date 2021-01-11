package urlshortener.web;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping(value = "/user")
  public String customers(Principal principal) {

    return "user " + principal.getName();
  }
}

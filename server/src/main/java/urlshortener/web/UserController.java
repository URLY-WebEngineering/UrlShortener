package urlshortener.web;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  @GetMapping(value = "/user/links")
  public List<String> customers(Principal principal) {
    List<String> url = new ArrayList<>();
    url.add("user " + principal.getName());
    url.add("customurl1");
    url.add("customurl2");
    return url;
  }
}

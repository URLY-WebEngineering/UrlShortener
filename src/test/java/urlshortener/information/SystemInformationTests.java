package urlshortener.information;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;

public class SystemInformationTests {
  private MockMvc mockMvc;
  @Mock private ClickService clickService;

  @Mock private ShortURLService shortUrlService;

  @Mock private SystemInformation SystemInformation;

}

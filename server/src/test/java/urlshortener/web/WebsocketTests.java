package urlshortener.web;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import urlshortener.repository.ShortURLRepository;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;
import urlshortener.service.URLStatusService;

public class WebsocketTests {

  private MockMvc mockMvc;

  @Mock private ClickService clickService;

  @Mock private ShortURLService shortUrlService;

  @Mock private URLStatusService urlStatusService;

  @Mock private ShortURLRepository shortURLRepository;

  @InjectMocks private WebSocketController webcontroller;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(webcontroller).build();
  }
}

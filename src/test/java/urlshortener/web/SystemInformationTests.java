package urlshortener.web;

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

  @InjectMocks private SystemInformationController systemInfoController;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(systemInfoController).build();
  }

  @Test
  public void SystemInfoWorking() throws Exception {
    mockMvc.perform(get("/system_info")).andDo(print()).andExpect(status().isOk());
  }
}

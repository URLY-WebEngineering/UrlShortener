package urlshortener.web;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import urlshortener.service.SafeBrowsingService;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class QRCodeTests {

  private MockMvc mockMvc;

  @InjectMocks
  private QRController qrcode;

  @Mock private SafeBrowsingService safeBrowsingService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(qrcode).build();
  }

  public String local_url="http://localhost:8080/qr";

  public String TEST_URL = "http://www.esportmaniacos.com/";
  @Test
  public void checkQRIfIsWorking() throws  Exception{
    when(safeBrowsingService.isSafe(TEST_URL)).thenReturn(true);
    //when(QRService)
    mockMvc.perform(post(local_url).param("url", TEST_URL).accept(MediaType.IMAGE_PNG_VALUE)).andDo(print())
            .andExpect(status().isCreated()).andExpect(header().string("Location", is(TEST_URL)));

  }
}

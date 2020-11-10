package urlshortener.web;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class QRCodeTests {
  private MockMvc mockMvc;

  @InjectMocks private QRController qrcode;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(qrcode).build();
  }

  @Test
  public void checkQRIfIsWorking() throws Exception {
    String local_url = "http://localhost:8080/qr";
    String TEST_URL = "http://www.esportmaniacos.com/";
    mockMvc
        .perform(post(local_url).param("url", TEST_URL).accept(MediaType.IMAGE_PNG_VALUE))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", is(TEST_URL)));
  }
}

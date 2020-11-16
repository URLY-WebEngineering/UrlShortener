package urlshortener.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static urlshortener.fixtures.ShortURLFixture.someUrl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import urlshortener.service.ShortURLService;

public class QRCodeTests {
  private MockMvc mockMvc;

  @InjectMocks private QRController qrcode;

  @Mock private ShortURLService shortUrlService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    this.mockMvc = MockMvcBuilders.standaloneSetup(qrcode).build();
  }

  @Test
  public void checkQRIfIsWorking() throws Exception {
    when(shortUrlService.findByKey("someKey")).thenReturn(someUrl());

    mockMvc
        .perform(get("/qr/{id}", "someKey"))
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.IMAGE_PNG));
  }

  @Test
  public void checkNotFound() throws Exception {
    when(shortUrlService.findByKey("someKey")).thenReturn(someUrl());
    mockMvc.perform(get("/qr/{id}", "oneDay")).andDo(print()).andExpect(status().isNotFound());
  }
}

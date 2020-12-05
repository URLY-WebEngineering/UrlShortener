package urlshortener.information;

import org.springframework.stereotype.Component;
import urlshortener.service.ClickService;
import urlshortener.service.ShortURLService;

@Component
public class SystemCalculator {
    private final ClickService clickService;
    private final ShortURLService shortUrlService;


    public SystemCalculator(ClickService clickService, ShortURLService shortUrlService) {
        this.clickService = clickService;
        this.shortUrlService = shortUrlService;
    }

    public Long getTotalClick() {
        return clickService.getTotalClick();
    }

    public Long getTotalURL(){
        return shortUrlService.getTotalURL();
    }
}

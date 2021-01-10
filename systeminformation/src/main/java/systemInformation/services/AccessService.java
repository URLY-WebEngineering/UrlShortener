package systemInformation.services;

import org.springframework.stereotype.Service;
import systemInformation.repository.ClickRepository;
import systemInformation.repository.ShortURLRepository;

@Service
public class AccessService {

    private final ClickRepository clickRepository;
    private final ShortURLRepository shortURLRepository;

    public AccessService(ClickRepository clickRepository, ShortURLRepository shortURLRepository) {
        this.clickRepository = clickRepository;
        this.shortURLRepository = shortURLRepository;
    }

    public Long getTotalClick() {
        return clickRepository.count(); // NOSONAR
    }

    public Long getTotalURL() {
        return shortURLRepository.count(); // NOSONAR
    }

}

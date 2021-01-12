package systemInformation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import systemInformation.domain.Click;

public interface ClickRepository extends JpaRepository<Click, Long> {}

package apptive.team5.subscribe.repository;

import apptive.team5.subscribe.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {
}

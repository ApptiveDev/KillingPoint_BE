package apptive.team5.subscribe.repository;

import apptive.team5.subscribe.domain.Subscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Query("select s from Subscribe s where s.subscriber.id = :subscriberId")
    List<Subscribe> findBySubscriberId(Long subscriberId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Subscribe s where s.subscribedTo.id = :subscribedToId and s.subscriber.id = :subscriberId")
    void deleteBySubscriberIdAndSubscribedToId(Long subscribedToId, Long subscriberId);
}

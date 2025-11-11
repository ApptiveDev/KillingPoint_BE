package apptive.team5.subscribe.service;

import apptive.team5.subscribe.domain.Subscribe;
import apptive.team5.subscribe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class SubscribeLowService {

    private final SubscribeRepository subscribeRepository;

    public Subscribe save(Subscribe subscribe) {
        return subscribeRepository.save(subscribe);
    }

    public void deleteBySubscriberIdAndSubscribedToId(Long subscribedToId, Long subscriberId) {
        subscribeRepository.deleteBySubscriberIdAndSubscribedToId(subscribedToId, subscriberId);
    }

    public Page<Subscribe> findBySubscriberIdWithSubscribedToPage(Long subscriberId, Pageable pageable) {
        return subscribeRepository.findBySubscriberIdWithSubscribedToPage(subscriberId, pageable);
    }

    public Page<Subscribe> findBySubscribedToIdWithSubscriberToPage(Long subscribedToId, Pageable pageable) {
        return subscribeRepository.findBySubscribedToIdWithSubscriberPage(subscribedToId, pageable);
    }
}

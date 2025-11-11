package apptive.team5.subscribe.service;

import apptive.team5.subscribe.domain.Subscribe;
import apptive.team5.subscribe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
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
}

package apptive.team5.subscribe.service;


import apptive.team5.subscribe.domain.Subscribe;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.service.UserLowService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final SubscribeLowService subscribeLowService;
    private final UserLowService userLowService;

    public void save(Long subscribeToUserId, Long userId) {

        UserEntity subscribedTo = userLowService.findById(subscribeToUserId);
        UserEntity subscriber = userLowService.getReferenceById(userId);

        Subscribe subscribe = new Subscribe(subscriber, subscribedTo);

        subscribeLowService.save(subscribe);
    }
}


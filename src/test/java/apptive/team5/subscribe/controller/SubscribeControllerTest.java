package apptive.team5.subscribe.controller;


import apptive.team5.subscribe.domain.Subscribe;
import apptive.team5.subscribe.repository.SubscribeRepository;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.repository.UserRepository;
import apptive.team5.util.TestSecurityContextHolderInjection;
import apptive.team5.util.TestUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.SoftAssertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SubscribeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscribeRepository subscribeRepository;


    @DisplayName("구독 추가 성공")
    @Test
    void subscribeSuccess() throws Exception {

        // given
        UserEntity subscriber = TestUtil.makeUserEntity();
        userRepository.save(subscriber);
        UserEntity subscribedTo = TestUtil.makeDifferentUserEntity(subscriber);
        userRepository.save(subscribedTo);

        TestSecurityContextHolderInjection.inject(subscriber.getId(), subscriber.getRoleType());

        // when
        mockMvc.perform(post("/api/subscribes/{subscribeToId}", subscribedTo.getId())
                .with(securityContext(SecurityContextHolder.getContext()))
        ).andExpect(status().isCreated());


        // then
        List<Subscribe> subscribes = subscribeRepository.findBySubscriberId(subscriber.getId());

        assertSoftly(softly -> {
            softly.assertThat(subscribes).hasSize(1);
            softly.assertThat(subscribes.getFirst().getSubscribedTo().getId()).isEqualTo(subscribedTo.getId());
        });
    }

    @DisplayName("구독 추가 실패 - 존재하지 않는 회원")
    @Test
    void subscribeFail() throws Exception {

        // given
        UserEntity subscriber = TestUtil.makeUserEntity();
        userRepository.save(subscriber);

        TestSecurityContextHolderInjection.inject(subscriber.getId(), subscriber.getRoleType());

        // when & then
        mockMvc.perform(post("/api/subscribes/{subscribeToId}", 100L)
                .with(securityContext(SecurityContextHolder.getContext()))
        ).andExpect(status().isNotFound());

    }

    @DisplayName("구독 취소 성공")
    @Test
    void cancelSubscribeSuccess() throws Exception {

        // given
        UserEntity subscriber = TestUtil.makeUserEntity();
        userRepository.save(subscriber);
        UserEntity subscribedTo = TestUtil.makeDifferentUserEntity(subscriber);
        userRepository.save(subscribedTo);
        Subscribe subscribe = subscribeRepository.save(new Subscribe(subscriber, subscribedTo));

        TestSecurityContextHolderInjection.inject(subscriber.getId(), subscriber.getRoleType());

        // when
        mockMvc.perform(delete("/api/subscribes/{subscribeToId}", subscribedTo.getId())
                .with(securityContext(SecurityContextHolder.getContext()))
        ).andExpect(status().isNoContent());


        // then
        List<Subscribe> subscribes = subscribeRepository.findBySubscriberId(subscriber.getId());
        boolean isPresent = subscribeRepository.findById(subscribe.getId()).isPresent();

        assertSoftly(softly -> {
            softly.assertThat(subscribes).hasSize(0);
            softly.assertThat(isPresent).isFalse();
        });
    }


}

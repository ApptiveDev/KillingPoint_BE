package apptive.team5.user.service;

import apptive.team5.global.exception.ExceptionCode;
import apptive.team5.global.exception.NotFoundEntityException;
import apptive.team5.user.domain.UserEntity;
import apptive.team5.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserLowService {

    private final UserRepository userRepository;


    public UserEntity findByIdentifier(String identifier) {
        return userRepository.findByIdentifier(identifier)
                .orElseThrow(()-> new NotFoundEntityException(ExceptionCode.NOT_FOUND_USER));
    }

    public boolean existsByIdentifier(String identifier) {
        return userRepository.findByIdentifier(identifier).isPresent();
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }
}

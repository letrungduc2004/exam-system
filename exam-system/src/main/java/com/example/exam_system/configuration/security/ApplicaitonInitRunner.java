package com.example.exam_system.configuration.security;

import com.example.exam_system.features.account.entity.User;
import com.example.exam_system.features.account.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicaitonInitRunner implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(userRepository.findByUserName("admin").isEmpty()){
            User user = User.builder()
                    .userName("admin")
                    .email("admin@gmail.com")
                    .password(passwordEncoder.encode("admin"))
                    .build();
            userRepository.save(user);
            log.info("admin has bean created with password admin !");
        }
    }
}

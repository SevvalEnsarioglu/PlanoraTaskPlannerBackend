package com.sevval.PlanoraTaskPlannerBackend.security;

import com.sevval.PlanoraTaskPlannerBackend.model.entity.User;
import com.sevval.PlanoraTaskPlannerBackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanoraUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new PlanoraUserDetails(user.getId(), user.getEmail(), user.getPassword());
    }
}



    package com.example.onlybunsbe.service;


    import com.example.onlybunsbe.model.User;
    import com.example.onlybunsbe.repository.UserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.security.authentication.DisabledException;
    import org.springframework.security.core.userdetails.UserDetails;
    import org.springframework.security.core.userdetails.UserDetailsService;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.stereotype.Service;


    // Ovaj servis je namerno izdvojen kao poseban u ovom primeru.
    // U opstem slucaju UserServiceImpl klasa bi mogla da implementira UserDetailService interfejs.
    @Service
    public class CustomUserDetailsService implements UserDetailsService {

        @Autowired
        private UserRepository userRepository;

        @Override
        public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
            User user = userRepository.findByEmail(mail);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username: " + mail);
            }

            // Check if the user account is enabled (activated)
            if (!user.isEnabled()) {
                throw new DisabledException("User account is not activated. Please check your email for activation.");
            }

            return user;
        }

    }

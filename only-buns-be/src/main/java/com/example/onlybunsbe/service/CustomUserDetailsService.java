    package com.example.onlybunsbe.service;


    import com.example.onlybunsbe.model.User;
    import com.example.onlybunsbe.repository.UserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
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
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            User user =  userRepository.findByEmail(email);
            if(user == null){
                  throw   new UsernameNotFoundException(String.format("No user found with email '%s'.", email));
            }
            else
            {
                return  user;
            }
        }

    }

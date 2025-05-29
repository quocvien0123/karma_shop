package com.gv.shoe_shop.config;

import com.gv.shoe_shop.constants.StringConstant;
import com.gv.shoe_shop.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private com.gv.shoe_shop.repository.UserRepository userRepo;


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = userRepo.findByUsername(username).orElse(null) ;

		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		} else {
			if(user.getStatus().equals(StringConstant.ACTIVE)){
				return new CustomUser(user);
			}else {
				throw new DisabledException("User is disabled");
			}
		}

	}

}

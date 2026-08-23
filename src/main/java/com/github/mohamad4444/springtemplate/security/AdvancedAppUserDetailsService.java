package com.github.mohamad4444.springtemplate.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AdvancedAppUserDetailsService implements UserDetailsService, UserDetailsPasswordService {

	private final AdvancedUserService userService;

	public AdvancedAppUserDetailsService(AdvancedUserService userService) {
		this.userService = userService;
	}

	@Override
	public UserDetails updatePassword(UserDetails user, String newPassword) {
		return this.userService
				.findByEmail(user.getUsername())
				.map(u -> {
					u.setPassword(newPassword);
					
					final var updatedUser = this.userService.save(u);
					return new AdvancedAuthenticatedUser(updatedUser);
				})
				.orElseThrow(() -> new UsernameNotFoundException("The user with user name " + user.getUsername() + " could not be found"));
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.userService
				.findByEmail(username)
				.map(AdvancedAuthenticatedUser::new)
				.orElseThrow(() -> new UsernameNotFoundException("The user with user name " + username + " could not be found"));
	}

}

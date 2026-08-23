package com.github.mohamad4444.springtemplate.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import com.github.mohamad4444.springtemplate.model.AdvancedUserEntity;

public class AdvancedAuthenticatedUser extends AdvancedUserEntity implements UserDetails {

	public AdvancedAuthenticatedUser(AdvancedUserEntity userEntity) {
		super();

		this.setId(userEntity.getId());
		this.setFirstName(userEntity.getFirstName());
		this.setLastName(userEntity.getLastName());
		this.setEmail(userEntity.getEmail());
		this.setPassword(userEntity.getPassword());
		this.setRoles(userEntity.getRoles());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", getRoles()));
	}

	@Override
	public String getUsername() {
		// use the email as user name
		return this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "AdvancedAuthenticatedUser [username=" + getUsername() + ", id=" + getId() + "]";
	}
}

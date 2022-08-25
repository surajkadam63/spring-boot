package com.springboot.jwt.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User  implements UserDetails{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String username;
	private String password;
	
	
	private boolean exipired;
	private boolean locked;
	private boolean enabled;
	
	@OneToMany(fetch = FetchType.EAGER) 
	@JoinTable(name = "user_roles", 
	joinColumns = @JoinColumn(name="user",
	referencedColumnName = "id"),
	inverseJoinColumns =@JoinColumn(name="roles",
	referencedColumnName = "id") )
	private List<Roles> roles=new ArrayList<>();

	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities=roles.stream()
				.map(r->new SimpleGrantedAuthority(r.role)).collect(Collectors.toList());
		return authorities;
	}

	@Override
	public String getPassword() {
		
		return password;
	}

	@Override
	public String getUsername() {
		
		return username;
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
		
		return enabled;
	}
	
	
	
	
}

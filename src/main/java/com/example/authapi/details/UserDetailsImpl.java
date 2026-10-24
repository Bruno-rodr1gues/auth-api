package com.example.authapi.details;

import com.example.authapi.entity.User;
import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        /*
         Este método converte a lista de papéis (roles) associados ao usuário
         em uma coleção de GrantedAuthorities, que é a forma que o Spring Security
         usa para representar papéis. Isso é feito mapeando cada papel para um
         novo SimpleGrantedAuthority, que é uma implementação simples de
         GrantedAuthority
        */

        return user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword(); // Retorna a credencial do usuário
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Retorna o nome de usuário do usuário
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Retorna true se a conta não estiver expirada
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Retorna true se a conta não estiver bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Retorna true se as credenciais não estiverem expiradas
    }

    @Override
    public boolean isEnabled() {
        return true; // Retorna true se o usuário estiver habilitado
    }
}

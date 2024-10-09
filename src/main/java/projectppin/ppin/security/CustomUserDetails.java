package projectppin.ppin.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private String empID;
    private String empPw;
    private Long companyId; // companyId 필드 추가
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(String empID, String empPw, Long companyId, Collection<? extends GrantedAuthority> authorities) {
        this.empID = empID;
        this.empPw = empPw;
        this.companyId = companyId; // companyId 초기화
        this.authorities = authorities;
    }

    // Getter for companyId
    public Long getCompanyId() {
        return companyId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return empPw;
    }

    @Override
    public String getUsername() {
        return empID;
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
}
package sbangularjs.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
    USER,
    MANAGER,
    TEACHER,
    SECRETARY,
    DEANERY; // деканат

    @Override
    public String getAuthority() {
        return name();
    }
}

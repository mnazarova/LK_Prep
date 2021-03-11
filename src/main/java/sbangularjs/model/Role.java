package sbangularjs.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority{
    TEACHER,
    SECRETARY,
    DEANERY, // деканат
    DEPUTY_DEAN; // зам. декана

    @Override
    public String getAuthority() {
        return name();
    }
}

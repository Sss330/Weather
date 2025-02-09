package filter;

import jakarta.servlet.*;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.SessionRepository;

//have to check user by cookie

@Component
@RequiredArgsConstructor
public class SessionFilter implements Filter {

    private final SessionRepository sessionRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

    }
}


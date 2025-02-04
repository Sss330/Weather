package filter;

import jakarta.servlet.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repository.SessionRepository;

//проверяем по переданной куке зареган ли пользователь

@Component
public class SessionFilter implements Filter {

    @Autowired
    private SessionRepository sessionRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain){

    }
}


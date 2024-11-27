package ega.spring.fitnessClubJdbc.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

@Component
public class RedisLogoutHandler implements LogoutHandler {

    private final SessionRepository<? extends Session> sessionRepository;

    public RedisLogoutHandler(SessionRepository<? extends Session> sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String sessionId = request.getSession(false).getId();
        if (sessionId != null) {
            sessionRepository.deleteById(sessionId); // Удаление сессии из Redis
            System.out.println("Сессия удалена из Redis: " + sessionId);
        }
    }
}

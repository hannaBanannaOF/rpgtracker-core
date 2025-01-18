package com.hbsites.rpgtracker.infrastructure.interceptors;

import com.hbsites.commons.domain.params.GetOneParams;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.SessionRepository;
import io.quarkus.security.ForbiddenException;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.UUID;

@Interceptor
@PermittedSession
public class PermittedSessionInterceptor {

    @Inject
    SessionRepository sessionRepository;

    @Inject
    JsonWebToken token;

    @AroundInvoke
    public Object permittedSheets(InvocationContext
                                            context) throws Exception {
        UUID user = UUID.fromString(token.getSubject());
        Method method = context.getMethod();
        Parameter[] methodParameters = method.getParameters();
        Object[] parameters = context.getParameters();
        for (int i = 0; i < methodParameters.length; i++) {
            if ((methodParameters[i].getAnnotation(PermittedSession.class) != null) &&
                    parameters[i] instanceof GetOneParams params) {
                boolean result = sessionRepository.userCanSee(user, params.getSlug());
                if (!result) {
                    throw new ForbiddenException();
                }
                break;
            }
        }

        return context.proceed();
    }

}

package com.hbsites.rpgtracker.infrastructure.interceptors;

import com.hbsites.rpgtracker.domain.params.CharacterSheetParams;
import com.hbsites.rpgtracker.infrastructure.repository.interfaces.CharacterSheetRepository;
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
@PermittedSheet
public class PermittedSheetInterceptor {

    @Inject
    CharacterSheetRepository characterSheetRepository;

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
            if ((methodParameters[i].getAnnotation(PermittedSheet.class) != null) &&
                    parameters[i] instanceof CharacterSheetParams params) {
                characterSheetRepository.userCanSee(user, params.getSlug()).onItem().transform(result -> {
                    if (!result) {
                        throw new ForbiddenException();
                    }
                    return null;
                });
                break;
            }
        }

        return context.proceed();
    }

}

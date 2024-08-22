package com.hbsites.rpgtracker.infrastructure.interceptors;

import com.hbsites.rpgtracker.domain.params.CharacterSheetParams;
import com.hbsites.rpgtracker.infrastructure.provider.RpgTrackerUserInfoProvider;
import io.quarkus.security.ForbiddenException;
import jakarta.inject.Inject;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptor;
import jakarta.interceptor.InvocationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Interceptor
@PermittedSheet
public class PermittedSheetInterceptor {

    @Inject
    private RpgTrackerUserInfoProvider provider;

    @AroundInvoke
    public Object permittedSheets(InvocationContext
                                            context) throws Exception {
        Method method = context.getMethod();
        Parameter[] methodParameters = method.getParameters();
        Object[] parameters = context.getParameters();
        for (int i = 0; i < methodParameters.length; i++) {
            if ((methodParameters[i].getAnnotation(PermittedSheet.class) != null) &&
                    parameters[i] instanceof CharacterSheetParams params
                    && (!provider.get().sheetBelongsToUser(params.getSheetId()))) {
                throw new ForbiddenException();
            }
        }

        return context.proceed();
    }

}

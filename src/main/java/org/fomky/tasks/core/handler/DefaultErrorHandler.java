package org.fomky.tasks.core.handler;

import org.springframework.stereotype.Component;
import ratpack.error.internal.ErrorHandler;
import ratpack.handling.Context;
import ratpack.handling.Handler;

/**
 * @author Created by Fomky on 2017/3/315:27.
 */
@Component
public class DefaultErrorHandler implements ErrorHandler {


    @Override
    public void error(Context context, int statusCode) throws Exception {

    }

    @Override
    public void error(Context context, Throwable throwable) throws Exception {
        context.render("Caught by error Handler: " + throwable.getMessage());
    }
}

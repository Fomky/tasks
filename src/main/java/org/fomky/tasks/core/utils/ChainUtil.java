package org.fomky.tasks.core.utils;

import ratpack.error.ServerErrorHandler;
import ratpack.handling.Chain;

/**
 * @author Created by Fomky on 2017/3/409:50.
 */
public class ChainUtil {

    private static Chain addErrorSupport(Chain chain) throws Exception {
        return chain.register(registry -> registry.add(ServerErrorHandler.class,(context, throwable)->{
                    context.render("Caught by error Handler: " + throwable.getMessage());
                }));
    }

    public static Chain addAllSupport(Chain chain) throws Exception {
        return addErrorSupport(chain);
    }

}

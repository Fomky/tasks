package org.fomky.tasks.core.controller;

import org.fomky.tasks.core.utils.ChainUtil;
import ratpack.handling.Chain;

import javax.annotation.Resource;
import java.util.function.Consumer;

/**
 * @author Created by Fomky on 2017/3/1811:41.
 */
public abstract class Controllers implements BaseController {


    @Override
    public void execute(Chain chain) throws Exception {
        run(ChainUtil.addAllSupport(chain));
    }

    public abstract void run(Chain chain) throws Exception;
}

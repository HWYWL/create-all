package com.generate.hooks;

import com.blade.ioc.annotation.Bean;
import com.blade.mvc.RouteContext;
import com.blade.mvc.hook.WebHook;
import com.blade.mvc.http.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 钩子拦截
 * @author YI
 * @date 2018-11-15 15:28:45
 */
@Bean
public class MyHook implements WebHook {
    public static final Logger LOGGER = LoggerFactory.getLogger(MyHook.class);

    @Override
    public boolean before(RouteContext routeContext) {
        Request request = routeContext.request();
        String  uri     = request.uri();
        LOGGER.info("{}\t{}", request.method(), uri);

        return true;
    }
}

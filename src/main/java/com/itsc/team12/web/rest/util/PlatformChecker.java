package com.itsc.team12.web.rest.util;

import org.springframework.stereotype.Component;

/**
 * Created by baba on 5/31/2016.
 */
@Component
public class PlatformChecker {

    private static String osName = System.getProperty("os.name");
    public static char FILE_SEPARATOR = osName.startsWith("Windows") ? '\\' : '/';

}

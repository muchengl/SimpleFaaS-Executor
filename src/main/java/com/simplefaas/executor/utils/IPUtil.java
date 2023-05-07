package com.simplefaas.executor.utils;

import java.io.IOException;
import java.io.InputStream;

public class IPUtil {

    public static String getLocalIp(){

        String cli[] = new String[2];
        cli[0]="curl";
        cli[1]="ifconfig.me";

       return ShellUtil.runSh(cli);
    }

}

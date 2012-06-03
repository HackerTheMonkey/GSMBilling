package com.asiacell.billing.utils;

import java.io.*;

class CmdExecutor {

    public static String[] executeCmd(String command)
            throws Exception {
        String cmd = command;
        String returnedArray[] = new String[2];
        int cmdOutputMid1;
        char cmdOutputMid2;
        String cmdOutputFinal = "";
        Runtime rtime = Runtime.getRuntime();
        Process proc = rtime.exec(cmd);
        InputStream inp = proc.getInputStream();
        cmdOutputMid1 = inp.read();
        while (cmdOutputMid1 != -1) {
            cmdOutputMid2 = (char) cmdOutputMid1;
            cmdOutputFinal = cmdOutputFinal + String.valueOf(cmdOutputMid2);
            cmdOutputMid1 = inp.read();
        }
        returnedArray[0] = cmdOutputFinal.trim();
        returnedArray[1] = Integer.toString(proc.exitValue());
        return returnedArray;
    }
}
package com.erbre.appstatus;

import java.text.DecimalFormat;

public final class Utils {

    private static final String[] bytesUnite = new String[] { "B ", "KB", "MB", "GB", "TB" };
    private static final double HOUR_NANO = 3600000000d;
    private static final double MINUTE_NANO = 60000000d;
    private static final double SECOND_NANO = 1000000d;

    public static String formatBytes(long size) {
        if (size == -1) {
            return "undefined";
        }
        if (size == 0) {
            return "0";
        }
        int digitGroups = (int) (Math.log10(size < 0 ? -size : size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + bytesUnite[digitGroups];
    }

    public Utils() {
    }

    public static String formatNanoSeconds(long time) {
        double digit = Math.log10(time);
        if (digit > 9d) {
            double result = time / HOUR_NANO;
            return String.format("%,.3f h", result);
        } else if (digit > 8d) {
            double result = time / MINUTE_NANO;
            return String.format("%,.3f m", result);
        } else if (digit > 6d) {
            double result = time / SECOND_NANO;
            return String.format("%,.3f s", result);
        }
        return String.format("%,d ns", time);

    }
    public static String formatMilliSeconds(long time) {
        double digit = Math.log10(time);
        if (digit > 6d) {
            double result = time / HOUR_NANO;
            return String.format("%,.3f h", result);
        } else if (digit > 5d) {
            double result = time / MINUTE_NANO;
            return String.format("%,.3f m", result);
        } else if (digit > 3d) {
            double result = time / SECOND_NANO;
            return String.format("%,.3f s", result);
        }
        return String.format("%,d ms", time);

    }
    public static void main(String[] args) {
        System.out.println(formatNanoSeconds(456544256456l));
        System.out.println(formatNanoSeconds(96544256456l));
        System.out.println(formatNanoSeconds(9444256456l));
        System.out.println(formatNanoSeconds(544256456l));
        System.out.println(formatNanoSeconds(44256456l));
        System.out.println(formatNanoSeconds(4256456l));
        System.out.println(formatNanoSeconds(56456l));
    }
}

package com.sample.iscanreorder;

import java.util.Comparator;

public class FileNameComparator implements Comparator<String> {

    @Override
    public int compare(String a, String b) {

        a = a.substring(a.lastIndexOf("_")+1);
        a = a.substring(0, a.indexOf("."));
        b = b.substring(b.lastIndexOf("_")+1);
        b = b.substring(0, b.indexOf("."));

        int intA = Integer.valueOf(a);
        int intB = Integer.valueOf(b);

        return intA < intB ? -1 : intA == intB ? 0 : 1;
    }

}

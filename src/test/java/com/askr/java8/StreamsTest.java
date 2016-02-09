package com.askr.java8;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StreamsTest {

    private static final String SUBNET_PREFIX = "10.15.127.";
    private static class Interface {
        private boolean management;
        private String ipv4;

        public boolean isManagement() {
            return management;
        }

        public String getIpv4() {
            return ipv4;
        }

        public Interface(boolean management, String ipv4) {
            this.management = management;
            this.ipv4 = ipv4;
        }
    }


    private static Collection<Interface> interfaces;
    private static Map<String, Interface> interfacesMap;

    @BeforeClass
    public static void init() {
        interfaces = new ArrayList<>();
        for (int i=120; i<135; i++) {
            for (int j=0; j<=255; j++) {
                interfaces.add(new Interface(j%2 == 0, String.format("10.15.%s.%s", i, j)));
            }
        }

        interfacesMap = interfaces.stream().collect(Collectors.toMap(Interface::getIpv4, (r)->r));
    }

    private boolean checkOneAddress(String addr) {
        return addr.startsWith(SUBNET_PREFIX) && interfacesMap.get(addr).isManagement();
    }
    private boolean checkResult(List<String> addrs) {

        for (int i = 0; i< addrs.size() -1; i++) {
            String addr = addrs.get(i);
            if (!checkOneAddress(addr)) {
                return false;
            }
            String addrNext = addrs.get(i+1);
            if (-addrComparator.compare(addr, addrNext) < 0) {
                return false;
            }
        }

        return checkOneAddress(addrs.get(addrs.size()-1));
    }

    @Test
    public void forLoopStream() {
        final int n = 15;
        AtomicInteger counter = new AtomicInteger();
        IntStream.range(0,n).forEach((i) -> counter.incrementAndGet());
        assertEquals(n, counter.intValue());
    }

    @Test
    public void forLoopStreamPeek() {
        final int n = 15;
        int actualN = (int)IntStream.range(0,n).peek((i) -> {}).count();
        assertEquals(actualN, n);
    }

    @Test
    public void java7() {

    }


    private static Comparator<String> addrComparator = (i1, i2) -> -Integer.decode(i1.replace(SUBNET_PREFIX, "")).compareTo(Integer.decode(i2.replace(SUBNET_PREFIX, "")));

    @Test
    // get list of addresses of management interfaces which start from specified subnet prefix order by address desc
    public void java8Streams() {
        List<String> ipv4s = interfaces.stream().
                filter((i) -> i.isManagement() && i.getIpv4().startsWith(SUBNET_PREFIX)).
                map((i) -> i.getIpv4()).
                sorted((i1,i2) -> addrComparator.compare(i1,i2)).
                collect(Collectors.toList());

        assertTrue(checkResult(ipv4s));
    }
}

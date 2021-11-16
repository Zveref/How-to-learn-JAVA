package edu.rice.comp504.model.util;

import junit.framework.TestCase;

public class ItemFinderTest extends TestCase {

    public void testFindNum() {
        int num = ItemFinder.findNum("gh3j1k45d");
        assertEquals(num, 3145);
        num = ItemFinder.findNum("abcd");
        assertEquals(num, -99999);
    }

    public void testFindString() {
        String s = ItemFinder.findString("gh3j1k45d");
        assertEquals(s, "ghjkd");
        s = ItemFinder.findString("abcd");
        assertEquals(s, "abcd");
        s = ItemFinder.findString("");
        assertEquals(s, "");
    }



}
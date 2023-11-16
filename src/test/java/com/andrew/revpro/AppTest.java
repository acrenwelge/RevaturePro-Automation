package com.andrew.revpro;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.andrew.revpro.Config.APPLICATION_MODES;

public class AppTest {

    @Test
    public void testAppConfigModes() throws IOException {
        Config.loadConfiguration(new String[] {"search", "foo", "bar"});
        assertEquals(Config.MODE, APPLICATION_MODES.SEARCH);
        assertEquals(Config.SEARCH_TERM, "foo bar");
        Config.loadConfiguration(new String[] {"scrape"});
        assertEquals(Config.MODE, APPLICATION_MODES.SCRAPE);
    }
    
    @Test
    public void testUrlStringListDistributedEvenly() {
    	List<String> testVals = Arrays.asList("alpha","beta","charlie","delta","echo","foxtrot","gamma","hotel");
    	List<List<String>> dividedLists = App.groupQuizUrls(4, testVals);
    	assertEquals(dividedLists.size(), 4);
    	int firstSize = dividedLists.get(0).size();
    	assertEquals(firstSize, 2); // all lists should be 8/4=2 in length
    	assertEquals(dividedLists.get(0).get(0),"alpha"); // should retain order
    }
    
}

package com.andrew.revpro;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Test;
import com.andrew.revpro.Config.APPLICATION_MODES;

public class AppTest {

    @Test
    public void testAppConfigModes() throws IOException {
        App.loadConfiguration(new String[] {"search", "foo", "bar"});
        assertEquals(Config.MODE, APPLICATION_MODES.SEARCH);
        assertEquals(Config.SEARCH_TERM, "foo bar");
        App.loadConfiguration(new String[] {"scrape"});
        assertEquals(Config.MODE, APPLICATION_MODES.SCRAPE);
    }
    
}

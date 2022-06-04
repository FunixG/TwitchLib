package fr.funixgaming.twitch.api.reference;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class TmiTwitchApiTests {

    @Test
    public void testGetChatters() throws Exception {
        final List<String> users = new TmiTwitchApi().getUsernamesConnectedOnChat("zerator");
        assertFalse(users.isEmpty());
    }

}

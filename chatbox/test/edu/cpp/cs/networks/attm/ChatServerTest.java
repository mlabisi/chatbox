package edu.cpp.cs.networks.attm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ChatServerTest {
    @Test
    public void testServerInstantiation() {
        ChatServer testChatServer = new ChatServer(1234);
        assertNotNull(testChatServer);
    }

    @Test
    public void testClientThreadCreation() {
    }

    @Test
    public void testIOInitilization() {

    }

    @Test
    public void testRegistration() {

    }

    @Test
    public void testLogout() {

    }

    @Test
    public void testChatBroadcast() {

    }
}

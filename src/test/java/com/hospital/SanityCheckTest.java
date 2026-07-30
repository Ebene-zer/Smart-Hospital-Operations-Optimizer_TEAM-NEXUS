package com.hospital;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SanityCheckTest {
    @Test
    void buildIsWired() {
        assertTrue(true, "If you see this pass, JUnit + Maven are wired correctly.");
    }
}

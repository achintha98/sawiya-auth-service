package com.sawiya.authservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "jwt.secret=Y2hvb3NlLWEtc2VjdXJlLWJhc2U2NC1lbmNvZGVkLXNlY3JldC1rZXktdGhhdC1pcy1hdC1sZWFzdC0yNTYtYml0cw==")
class AuthserviceApplicationTests {

    @Test
    void contextLoads() {
    }

}

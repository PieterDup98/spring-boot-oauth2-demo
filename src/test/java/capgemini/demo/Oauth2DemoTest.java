package capgemini.demo;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class Oauth2DemoTest {

    @Autowired
    private ApplicationContext context;

    @Test
    void canLoadContext() {
        Assertions.assertThat(context).isNotNull();
    }
}

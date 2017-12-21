package xx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Nested
public class Demo {

    int x=0;

    @BeforeEach
    public void init(){
        x=100;
    }

    @Nested
    class CheckResource{

        @Test
        public void prepareResource(){
            assertEquals(100,x);
            x=200;
        }
        @Nested
        class DoStuff{

            @Test
            public void stuffTest(){
                assertEquals(100,x);
            }

        }
    }

}

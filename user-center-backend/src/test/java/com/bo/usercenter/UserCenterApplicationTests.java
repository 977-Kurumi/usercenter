package com.bo.usercenter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class UserCenterApplicationTests {

    @Test
    void contextLoads() {
        String[] tests = {" ", "a ", " a", "a b", "a!b", "a$b", "a@b" ,"ab@"};
        String validPattern = "\\p{P}|\\p{S}|\\s+";
        Pattern pattern = Pattern.compile(validPattern);
        for (String test : tests) {
            Matcher matcher = pattern.matcher(test);
            System.out.println(test + ": " + matcher.find());
        }
    }

}

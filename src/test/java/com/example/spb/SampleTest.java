package com.example.spb;
import com.example.spb.entity.User;
import com.example.spb.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.List;
import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleTest {
    @Autowired
    private UserMapper mapper;
    @Test
    public void testSelect() {
        List<User> list = mapper.selectList(null);
        assertEquals(1, list.size());
        list.forEach(System.out::println);
    }
}

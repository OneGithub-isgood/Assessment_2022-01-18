package sg.edu.nus.AssessmentSSF.repositories;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {
    
    @Autowired
    @Qualifier("Book_Cache")
    private RedisTemplate<String, Map<String, String>> template;

    public void store(String keyName, Map<String, String> mapKeyValue) {
        template.opsForHash().putAll(keyName, mapKeyValue);
    }

    public Object retrieve(String keyName, String mapKey) {
        return template.opsForHash().get(keyName, mapKey);
    }
    
}

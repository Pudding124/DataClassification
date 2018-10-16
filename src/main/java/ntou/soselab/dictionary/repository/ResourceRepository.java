package ntou.soselab.dictionary.repository;

import ntou.soselab.dictionary.bean.Resource;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResourceRepository extends GraphRepository<Resource> {
    @Query("MATCH (n:Resource {title:{title}}) RETURN n ")
    Resource findByTitle(@Param("title") String title);
}
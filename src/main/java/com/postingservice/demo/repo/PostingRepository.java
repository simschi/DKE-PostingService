package com.postingservice.demo.repo;

import com.postingservice.demo.model.Posting;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "postings", path = "postings")
public interface PostingRepository extends MongoRepository<Posting, String> {

    List<Posting> findByEmotion(String emotion);
}

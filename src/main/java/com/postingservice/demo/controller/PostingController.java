package com.postingservice.demo.controller;

import com.postingservice.demo.model.Posting;
import com.postingservice.demo.model.User;
import com.postingservice.demo.repo.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PostingController {

    @Autowired
    private PostingRepository postingRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private KafkaTemplate<String, Posting> kafkaTemplate;

    private static final String TOPIC = "Kafka_Posting_Topic";

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/postings")
    public ResponseEntity<List<Posting>> getAllPostings(@RequestParam(value = "loggedInUser") String loggedInUser,
                                                        @RequestBody List<User> followedUsers) {
        List<Posting> postings = postingRepository.findAll();
        postings.sort((a,b) -> a.getCreationDate().before(b.getCreationDate()) ? 1 : -1);

        List<String> followedUsersAsString = followedUsers.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());

        if (followedUsers.size() == 0) {
            postings = postings.stream()
                    .filter(posting -> posting.getUserId().equals(loggedInUser))
                    .collect(Collectors.toList());
        } else {
            postings = postings.stream()
                    .filter(posting -> {
                        boolean containsUser = false;
                        if (posting.getUserId().equals(loggedInUser)){
                            containsUser = true;
                        }
                        if(followedUsersAsString.contains(posting.getUserId())){
                            containsUser = true;
                        }

                        return containsUser;
                    })
                    .collect(Collectors.toList());
        }

        return new ResponseEntity<>(postings, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/postings/{searchValue}")
    public ResponseEntity<List<Posting>> getFilteredPostings(@PathVariable("searchValue") String searchValue) {
        List<Posting> postings = null;

        if(searchValue.isEmpty()) {
            postings = postingRepository.findAll();
        } else {
            Query query = new Query();
            List<Criteria> criteriaList = new ArrayList<>();
            criteriaList.add(Criteria.where("userId").regex(searchValue, "i"));
            criteriaList.add(Criteria.where("content").regex(searchValue, "i"));
            criteriaList.add(Criteria.where("emotion").regex(searchValue, "i"));
            Criteria[] criteriaArray = criteriaList.toArray(new Criteria[criteriaList.size()]);
            query.addCriteria(new Criteria().orOperator(criteriaArray));

            postings = mongoTemplate.find(query, Posting.class);
        }

        postings.sort((a,b) -> a.getCreationDate().before(b.getCreationDate()) ? 1 : -1);
        return new ResponseEntity<>(postings, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{addPosting}")
    public ResponseEntity<Posting> addPosting(@RequestBody Posting posting) {
        posting.setCreationDate(new Date());
        Posting newPosting = postingRepository.insert(posting);
        sendToKafkaTopic(newPosting);
        return new ResponseEntity<>(newPosting, HttpStatus.CREATED);
    }

    private boolean sendToKafkaTopic(Posting newPosting) {
        try {
            kafkaTemplate.send(TOPIC, newPosting);
        } catch(Exception e) {
            return false;
        }
        return true;
    }

}

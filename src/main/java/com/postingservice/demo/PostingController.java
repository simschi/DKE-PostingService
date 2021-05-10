package com.postingservice.demo;

import com.postingservice.demo.model.Posting;
import com.postingservice.demo.repo.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class PostingController {

    @Autowired
    private PostingRepository postingRepository;

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/postings")
    public ResponseEntity<List<Posting>> getAllPostings() {
        List<Posting> postings = postingRepository.findAll();
        return new ResponseEntity<>(postings, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/postings/{emotion}")
    public ResponseEntity<List<Posting>> getAllPostings(@PathVariable("emotion") String emotion) {
        List<Posting> postings = postingRepository.findByEmotion(emotion);
        return new ResponseEntity<>(postings, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{addPosting}")
    public ResponseEntity<Posting> addPosting(@RequestBody Posting posting) {
        posting.setCreationDate(new Date());
        Posting newPosting = postingRepository.save(posting);
        return new ResponseEntity<>(newPosting, HttpStatus.CREATED);
    }
}

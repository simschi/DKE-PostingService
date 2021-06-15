package com.postingservice.demo;

import com.postingservice.demo.model.Posting;
import com.postingservice.demo.model.User;
import com.postingservice.demo.repo.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PostingController {

    @Autowired
    private PostingRepository postingRepository;

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

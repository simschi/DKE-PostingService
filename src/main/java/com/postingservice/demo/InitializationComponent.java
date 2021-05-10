package com.postingservice.demo;

import com.postingservice.demo.model.Posting;
import com.postingservice.demo.repo.PostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class InitializationComponent {

    @Autowired
    private PostingRepository postingRepository;

    @PostConstruct
    private void init(){

        postingRepository.deleteAll();

        Posting posting =new Posting();
        posting.setContent("Das ist mein erster Post");
        posting.setEmotion("FRÖHLICH");
        posting.setCreationDate(new Date());
        postingRepository.save(posting);

        posting =new Posting();
        posting.setContent("Das ist mein zweiter Post");
        posting.setEmotion("TRAURIG");
        posting.setCreationDate(new Date());
        postingRepository.save(posting);
    }
}

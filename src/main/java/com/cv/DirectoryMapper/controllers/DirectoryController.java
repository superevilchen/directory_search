package com.cv.DirectoryMapper.controllers;

import com.cv.DirectoryMapper.validators.CandidateType;
import com.cv.DirectoryMapper.services.DirectorySearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/directory_search/")
public class DirectoryController {

    private final DirectorySearch directorySearch;


    @GetMapping("{directory}")
    public Map<String, CandidateType> getAllFiles(@RequestParam String directory){
        return directorySearch.search(directory);
    }
}

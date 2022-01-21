package com.cv.DirectoryMapper.services;

import com.cv.DirectoryMapper.validators.CandidateDetector;
import com.cv.DirectoryMapper.validators.CandidateType;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class DirectorySearch {

//    private String fileExt;
        private Map<String, CandidateType> candidates = new HashMap<>();
        private Map<String, String> allFiles = new HashMap<>();

//    public DirectorySearch(List<String> fileList) {
//        this.fileList = fileList;
//    }

    public Map<String, CandidateType> search(String directory) {
        File f = new File(directory);

        if (f.exists()) {

            Queue<File> queue = new PriorityQueue<>();
            String[] files = f.list();

//            queue = Stream.of(files)
//                    .map(fi -> new File(directory + "/" + fi))
//                    .collect(Collectors.toCollection(PriorityQueue::new));

            for (String file : files){
                allFiles.put(file, null);
                queue.add(new File(directory + "/" + file));
            }

            while (!queue.isEmpty()) {

                File temp = queue.remove();

                // put validation here

                CandidateType result =
                        CandidateDetector
                                .isFileNotOpenedRecently()
                                .and(CandidateDetector.isFileBig().and(CandidateDetector.isFileDuplicated(allFiles)))
                                .apply(temp);

                if (!result.equals(CandidateType.OK)){
                    candidates.put(temp.toString(), result);
                }

                if (temp.isDirectory()) {
                    search(temp.toString());
                }
            }
        }
        return candidates;
    }
}

package com.cv.DirectoryMapper.validators;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;
import java.util.function.Function;
import static com.cv.DirectoryMapper.validators.CandidateType.*;

public interface CandidateDetector extends Function<File, CandidateType> {

    static CandidateDetector isFileBig(){
        return file -> (file.length() < 1_000_000_000)
                && (Instant.ofEpochMilli
                (file.lastModified())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .isBefore(LocalDateTime.now().minusMonths(6))) ? OK : BIG_AND_OLD;
    }

    static CandidateDetector isFileNotOpenedRecently(){
        return file -> {
            try {
                return Instant.ofEpochMilli
                        (Files.readAttributes(file.toPath(), BasicFileAttributes.class).lastAccessTime().toMillis())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime()
                        .isBefore(LocalDateTime.now().minusMonths(6)) ? OK : NOT_OPENED_RECENTLY;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return INVALID;
        };
    }

    static CandidateDetector isFileDuplicated(Map<String, String> allFiles){
        return file -> !allFiles.containsKey(file) ? OK : DUPLICATED;
    }

    default CandidateDetector and(CandidateDetector other){
        return file -> {
            CandidateType result = this.apply(file);
            return result.equals(OK) ? other.apply(file) : result;
        };
    }
}

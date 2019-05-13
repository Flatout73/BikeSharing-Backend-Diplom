package ru.hse.BikeSharing.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hse.BikeSharing.domain.DBFile;
import ru.hse.BikeSharing.errors.FileStorageException;
import ru.hse.BikeSharing.repo.DBFileRepo;

import java.io.IOException;

@Service
public class DBFileStorageService {

    @Autowired
    private DBFileRepo dbFileRepository;

    public DBFile storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());

            return dbFileRepository.save(dbFile);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public DBFile storeImage(byte[] bytes, String fileName) {
        DBFile dbFile = new DBFile(fileName, "image/png", bytes);

        return dbFileRepository.save(dbFile);
    }

    public DBFile getFile(Long fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new FileStorageException("File not found with id " + fileId));
    }
}

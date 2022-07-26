package edu.poly.shop.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.poly.shop.config.StorageProperties;
import edu.poly.shop.exception.StorageException;
import edu.poly.shop.exception.StorageFileNotFoundException;
import edu.poly.shop.service.StorageService;

@Service
public class FileSystemStorageServiceImpl implements StorageService {
	private final Path rootLocation;//duong dan goc luu image
	
	@Override
	public String getStoredFilenname(MultipartFile file,String id ) {
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		return "p" + id + "." + ext;
	}
	
	public FileSystemStorageServiceImpl(StorageProperties properties) {//pt dung de luu nd cua file 
		this.rootLocation = Paths.get(properties.getLocation());
	}
	@Override
	public void store(MultipartFile file, String storedFilename) {
		try {
			if(file.isEmpty()) {
				throw new StorageException("Failed to store empty file");
			}
			
			Path destinationFile = this.rootLocation.resolve(Paths.get(storedFilename))
					.normalize().toAbsolutePath();
			if(!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				throw new StorageException("Cannot store file outside current directory");
			}
			try(InputStream inputStream = file.getInputStream()) {
				Files.copy(inputStream, destinationFile,StandardCopyOption.REPLACE_EXISTING);
			}
		}catch(Exception e) {
			throw new StorageException("Failed to store file",e);
		}
	}
	@Override
	public Resource loadAsResource(String filename) {//nap nd cua file
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if(resource.exists() || resource.isReadable()) {//neu ton tai tiep so sanh  
				return resource;//tra ve resource
			}
			
			throw new StorageFileNotFoundException("Could not read file: " + filename);//nem ra ngoai le 
		} catch (Exception e) {//ht tb loi 
			throw new StorageFileNotFoundException("Could not read file: " + filename);
		}
	}
	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}
	@Override
	public void delete(String storedFilename) throws IOException {//xoa file 
		Path destinationFile = rootLocation.resolve(Paths.get(storedFilename)).normalize().toAbsolutePath();
		
		Files.delete(destinationFile);
	}
	@Override
	public void init() {//pt dung de khoi tao
		try {
			Files.createDirectories(rootLocation);
			System.out.println(rootLocation.toString());
		} catch (Exception e) {
			throw new StorageException("Could not initialize storage",e);
		}
	}
}

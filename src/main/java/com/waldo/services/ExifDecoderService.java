package com.waldo.services;

import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.waldo.model.Contents;
import com.waldo.model.ExifData;

/**
 * @author Suresh
 * 
 * 	This is a Decoder Service that parses exif metadata for a given image url and persists data using a dao service
 *  It uses a open source exif metadata reader 
 *
 */
@Component
@Scope("prototype")
public class ExifDecoderService extends Thread {
	
	Logger logger = LoggerFactory.getLogger(ExifDecoderService.class);

	public List<Contents> contents;
	
	public String contentUri;
	
	@Autowired
	ExifDataService exifDataService;
	
	@Override
	public void run() {
		
		//Process each batch
		for (Contents content : contents) {
			String imageUrl = contentUri+""+content.getKey();
			try {
				//Using open source library to read exif metadata
				Metadata metadata = ImageMetadataReader.readMetadata(new URL(imageUrl).openStream());
				Iterable<Directory> dirs = metadata.getDirectories();
				for (Directory dir : dirs) {
					for (Tag tag : dir.getTags()) {
			        
						//Create a data model and persist in db
				        ExifData data = new ExifData();
				        String desc = tag.getDescription();
				        //TODO: Truncating since some description are too long. Move description to a clob.
				        if(desc.length()>249)
				        	desc = desc.substring(0,  250);
				        data.setDescription(desc);
				        data.setDirectoryName(tag.getDirectoryName());
				        data.setS3etag(content.getEtag());
				        data.setS3key(content.getKey());
				        data.setTagName(tag.getTagName());
				        data.setTagType(tag.getTagType());
				        exifDataService.save(data);
				    }
				}

			} catch (Exception e) {
				//TODO: collect failure cases and send to a backup job to retry
				//Also report to support team
				logger.debug("Error retrieving or processing metadata data for etag: {} | Error {}", content.getEtag(), e.getMessage());
			}
			
		}
	}

	public String getContentUri() {
		return contentUri;
	}

	public void setContentUri(String contentUri) {
		this.contentUri = contentUri;
	}

	public List<Contents> getContents() {
		return contents;
	}

	public void setContents(List<Contents> contents) {
		this.contents = contents;
	}
	
}

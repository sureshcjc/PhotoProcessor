package com.waldo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="metadata")
public class ExifData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    
    String s3etag;
    String s3key;
    String tagName;
    Integer tagType;
    String directoryName;
    String description;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getS3etag() {
		return s3etag;
	}
	public void setS3etag(String s3etag) {
		this.s3etag = s3etag;
	}
	public String getS3key() {
		return s3key;
	}
	public void setS3key(String s3key) {
		this.s3key = s3key;
	}
	public String getTagName() {
		return tagName;
	}
	public void setTagName(String tagName) {
		this.tagName = tagName;
	}
	public Integer getTagType() {
		return tagType;
	}
	public void setTagType(Integer tagType) {
		this.tagType = tagType;
	}
	public String getDirectoryName() {
		return directoryName;
	}
	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    
}

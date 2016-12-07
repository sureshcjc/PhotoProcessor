package com.waldo.model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ListBucketResult", namespace="http://s3.amazonaws.com/doc/2006-03-01/")
public class ListBucketResult {

	@XmlElement(name = "Contents", required = true)
	public ArrayList<Contents> contents = new ArrayList<Contents>();

}

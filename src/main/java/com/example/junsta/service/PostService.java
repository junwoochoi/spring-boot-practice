package com.example.junsta.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.junsta.model.PostVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface PostService {
	
	public List<PostVO> getAll();
	public byte[] getImage(String id) throws IOException;
	public void uploadPost(String postStr, MultipartFile fileContent) throws JsonParseException, JsonMappingException, IOException;

}

package com.example.junsta.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.example.junsta.model.PostVO;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface PostService {
	
	public List<PostVO> getAll(Map<String, Integer> pageMap);
	public byte[] getImage(String fileName) throws IOException;
	public void uploadPost(String postStr, MultipartFile fileContent) throws JsonParseException, JsonMappingException, IOException;
	public boolean getLike(Map<String,String> map);
	public int insertLike(Map<String,String> map);
	public int deleteLike(Map<String,String> map);
	public List<String> getFollowingUserList(String userId);
	public int toggleFollow(Map<String,String> map);
	public int deletePost(Map<String, String> map);
}

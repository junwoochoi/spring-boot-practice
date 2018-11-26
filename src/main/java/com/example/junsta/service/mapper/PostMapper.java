package com.example.junsta.service.mapper;

import java.util.List;
import java.util.Map;

import com.example.junsta.model.PostVO;

public interface PostMapper {

	public List<PostVO> getAll(Map<String, Integer> map);
	public String getImage(String id);
	public int uploadPost(PostVO post);
	public int getLike(Map<String,String> map);
	public int insertLike(Map<String,String> map);
	public int deleteLike(Map<String,String> map);
	public int getLikeCount(String postId);
}

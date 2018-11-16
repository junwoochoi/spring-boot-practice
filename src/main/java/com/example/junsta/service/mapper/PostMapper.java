package com.example.junsta.service.mapper;

import java.util.List;
import java.util.Map;

import com.example.junsta.model.PostVO;

public interface PostMapper {

	public List<PostVO> getAll();
	public String getImage(String id);
	public int uploadPost(PostVO post);
}

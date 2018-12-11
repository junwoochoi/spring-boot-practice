package com.example.junsta.service.mapper;

import java.util.List;
import java.util.Map;

public interface HashTagMapper {
	public List<Map<String,String>> searchByKeyword(String keyword);
}

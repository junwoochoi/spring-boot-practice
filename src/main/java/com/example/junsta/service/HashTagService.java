package com.example.junsta.service;

import java.util.List;
import java.util.Map;

public interface HashTagService {
		public List<Map<String,String>> findByKeyword(String keyword);
}

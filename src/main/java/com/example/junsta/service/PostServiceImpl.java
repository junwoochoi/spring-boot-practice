package com.example.junsta.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.junsta.model.PostVO;
import com.example.junsta.service.mapper.PostMapper;
import com.example.junsta.util.FilenameUtil;
import com.example.junsta.util.JsonUtil;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

@Service
public class PostServiceImpl implements PostService {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public static final String IMAGE_URL = "C:\\\\Users\\\\user\\\\eclipse-workspace\\\\spring-boot-practice\\\\spring-boot-junsta\\\\src\\\\main\\\\resources\\\\uploadedImages\\\\";

	@Autowired
	PostMapper postMapper;

	@Override
	public List<PostVO> getAll(Map<String, Integer> map) {
		List<PostVO> list = postMapper.getAll(map);
		for(PostVO postVo : list) {
			postVo.setLikeCount(postMapper.getLikeCount(String.valueOf(postVo.getPostId())));
		}
		return list;
	}

	@Override
	public byte[] getImage(String fileName) throws IOException {
		
		File fi = new File(IMAGE_URL+fileName);
		byte[] fileContent = Files.readAllBytes(fi.toPath());
		return fileContent;
	}
	
	@Override
	public void uploadPost(String postStr, MultipartFile fileContent)
			throws JsonParseException, JsonMappingException, IOException {
		PostVO postObj = null;
		postObj = (PostVO) JsonUtil.parseToObj(postStr, PostVO.class);

		if (postObj != null) {


			String sourceFileName = fileContent.getOriginalFilename();
			String sourceFileNameExtension = FilenameUtil.getExtension(sourceFileName).toLowerCase();
			File destinationFile;
			String destinationFileName;
			String fileUrl = IMAGE_URL;

			do {
				destinationFileName = FilenameUtil.createRandomFileName(32) + "." + sourceFileNameExtension;
				destinationFile = new File(fileUrl + destinationFileName);
			} while (destinationFile.exists());

			destinationFile.getParentFile().mkdirs();

			fileContent.transferTo(destinationFile);

			postObj.setContents_pic(destinationFileName);

			postMapper.uploadPost(postObj);

		}
	}

	@Override
	public boolean getLike(Map<String,String> map) {
		int count =  postMapper.getLike(map);
		if(count>0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int insertLike(Map<String, String> map) {
		
		return postMapper.insertLike(map);
	}

	@Override
	public int deleteLike(Map<String, String> map) {
		return postMapper.deleteLike(map);
	}


}

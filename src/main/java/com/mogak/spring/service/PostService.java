package com.mogak.spring.service;

import com.mogak.spring.domain.post.Post;
import com.mogak.spring.web.dto.PostImgRequestDto;
import com.mogak.spring.web.dto.PostRequestDto;
import org.springframework.data.domain.Slice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {

    Post create(PostRequestDto.CreatePostDto request, List<PostImgRequestDto.CreatePostImgDto> postImgDtoList,/*User user*/ Long mogakId, HttpServletRequest req);
    Slice<Post> getAllPosts(Long cursor, Long mogakId, int size);
    Post findById(Long postId);
    Post update(Long postId, PostRequestDto.UpdatePostDto request);
    void delete(Long postId);
    List<Post> getPacemakerPosts(HttpServletRequest req);
}

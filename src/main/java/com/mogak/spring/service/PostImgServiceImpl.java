package com.mogak.spring.service;

import com.mogak.spring.domain.post.Post;
import com.mogak.spring.domain.post.PostComment;
import com.mogak.spring.domain.post.PostImg;
import com.mogak.spring.repository.PostCommentRepository;
import com.mogak.spring.repository.PostImgRepository;
import com.mogak.spring.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostImgServiceImpl implements PostImgService{

    private final PostRepository postRepository;
    private final PostImgRepository postImgRepository;

    @Override
    public List<String> findUrlByPost(Long postId){
        Post post = postRepository.findById(postId).get();
        List<PostImg> postImgList = postImgRepository.findAllByPost(post);
        List<String> imgUrlList =new ArrayList<>();
        for(PostImg postImg :postImgList){
            imgUrlList.add(postImg.getImgUrl());
        }
        return imgUrlList;
    }

    //이미지 상세조회를 위한
    @Override
    public List<String> findNotThumbnailImg(Post post){
        String thumbnailUrl = post.getPostThumbnailUrl();
        List<PostImg> postImgList = post.getPostImgs();
        List<String> imgUrls = new ArrayList<>();
        for(PostImg postImg : postImgList){
            if(thumbnailUrl != postImg.getImgUrl()){
                imgUrls.add(postImg.getImgUrl());
            }
        }
        return imgUrls;
    }

    @Override
    public List<PostImg> findAllByPost(Post post){
        return postImgRepository.findAllByPost(post);
    }




}
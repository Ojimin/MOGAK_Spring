package com.mogak.spring.web.controller;


import com.mogak.spring.converter.PostConverter;
import com.mogak.spring.domain.post.Post;
import com.mogak.spring.exception.ErrorResponse;
import com.mogak.spring.service.PostLikeService;
import com.mogak.spring.service.PostService;
import com.mogak.spring.web.dto.PostLikeRequestDto;
import com.mogak.spring.web.dto.PostResponseDto;
import com.mogak.spring.web.dto.PostResponseDto.NetworkPostDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Tag(name = "네트워킹 API", description = "네트워킹 API 명세서")
@RestController
@Slf4j
@RequiredArgsConstructor
public class NetworkController {

    private final PostLikeService postLikeService;
    private final PostService postService;

    //좋아요 생성&삭제
    @Operation(summary = "좋아요 생성/삭제", description = "게시물에 좋아요를 생성/삭제합니다",
            parameters = @Parameter(name = "JWT 토큰", description = "jwt 토큰"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "좋아요 생성/삭제"),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 회고록, 존재하지 않는 유저",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "이미 좋아요를 누른 케이스 Or 서버 오류"),
            })
    @PostMapping("/api/posts/like")
    public ResponseEntity<String> updateLike(@RequestBody PostLikeRequestDto.LikeDto request, HttpServletRequest req){
        String message = postLikeService.updateLike(request, req);
        return ResponseEntity.ok(message);
    }

    @Operation(summary = "페이스 메이커 게시물 조회", description = "팔로우 중인 페이스 메이커의 게시물을 페이징 조회 합니다",
            parameters = {
                    @Parameter(name = "JWT 토큰", description = "jwt 토큰"),
                    @Parameter(name = "cursor", description = "페이징 커서(시작점)"),
                    @Parameter(name = "size", description = "페이징 게시물 개수")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 유저",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            })
    @GetMapping("/api/posts/pacemakers")
    public ResponseEntity<List<NetworkPostDto>> getPacemakerPosts(@RequestParam int cursor,
                                                                  @RequestParam int size,
                                                                  HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPacemakerPosts(cursor, size, req));
    }

    //네트워킹 전체조회
    @Operation(summary = "네트워킹 게시물 조회", description = "네트워킹 게시물을 조건에 맞게 페이징 조회 합니다",
            parameters = {
                    @Parameter(name = "JWT 토큰", description = "jwt 토큰"),
                    @Parameter(name = "page", description = "page 수"),
                    @Parameter(name = "size", description = "페이징 게시물 개수(한 페이지에 들어갈 게시물 개수)")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "게시물 조회 성공"),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 유저",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "500", description = "서버 오류"),
            })
    @GetMapping("/api/posts")
    public ResponseEntity<Slice<PostResponseDto.GetAllNetworkDto>> getALlPosts(@RequestParam(value="page", defaultValue = "0", required = false) int page, @RequestParam(value="size") int size,
                                                                         @RequestParam(value = "sort", defaultValue = "createdAt", required = false) String sort, @RequestParam(value = "address", required = false) String address,
                                                                         /*@RequestParam(value = "category", defaultValue="all", required = false) List<String> categoryList,*/ HttpServletRequest req){
        Slice<Post> posts = postService.getNetworkPosts(page, size, sort, address,req);
        return ResponseEntity.ok(PostConverter.toNetworkPagingDto(posts));
    }


}

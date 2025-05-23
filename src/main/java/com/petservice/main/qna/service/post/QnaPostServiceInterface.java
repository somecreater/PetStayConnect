package com.petservice.main.qna.service.post;

import com.petservice.main.qna.database.dto.QnaPostDTO;
import com.petservice.main.user.database.dto.CustomUserDetails;
import org.springframework.data.domain.Page;


import java.util.List;

public interface QnaPostServiceInterface {

    QnaPostDTO createPost(QnaPostDTO dto, CustomUserDetails principal);

    List<QnaPostDTO> getPostsByUserLoginId(String userLoginId);

    QnaPostDTO getPostById(Long postId);

    QnaPostDTO viewAndGet(Long postId);

    QnaPostDTO updatePost(Long postId, QnaPostDTO dto, String userLoginId);

    void deletePost(Long postId, String userLoginId);

    Page<QnaPostDTO> getPostsPage(int page, int size);
}

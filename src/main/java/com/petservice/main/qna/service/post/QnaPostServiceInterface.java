package com.petservice.main.qna.service.post;

import com.petservice.main.qna.database.dto.QnaPostDTO;


import java.util.List;

public interface QnaPostServiceInterface {

    void createPost(QnaPostDTO dto, String userLoginId);

    List<QnaPostDTO> getAllPosts();

    List<QnaPostDTO> getPostsByUserLoginId(String userLoginId);


    QnaPostDTO getPostById(Long postId);

    void updatePost(Long postId, QnaPostDTO dto, String userLoginId);

    void deletePost(Long postId, String userLoginId);


}

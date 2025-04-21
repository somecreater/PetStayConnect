package com.petservice.main.user.database.mapper;

import com.petservice.main.user.database.dto.BookmarkDTO;
import com.petservice.main.user.database.entity.Bookmark;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookmarkMapper {

  private final UserRepository userRepository;

  public Bookmark toEntity(BookmarkDTO bookmarkDTO){

    Bookmark bookmark=new Bookmark();
    bookmark.setId(bookmarkDTO.getId());
    bookmark.setBookmarkType(bookmarkDTO.getBookmarkType());
    bookmark.setTargetId(bookmarkDTO.getTargetId());
    bookmark.setCreatedAt(bookmarkDTO.getCreateAt());
    bookmark.setUpdatedAt(bookmarkDTO.getUpdateAt());
    if(bookmarkDTO.getUserId()!=null) {
      bookmark.setUser(userRepository.findById(bookmarkDTO.getUserId()).orElse(null));
    }

    return bookmark;
  }

  public BookmarkDTO toDTO(Bookmark bookmark){

    BookmarkDTO bookmarkDTO =new BookmarkDTO();
    bookmarkDTO.setId(bookmark.getId());
    bookmarkDTO.setBookmarkType(bookmark.getBookmarkType());
    bookmarkDTO.setTargetId(bookmark.getTargetId());
    bookmarkDTO.setCreateAt(bookmark.getCreatedAt());
    bookmarkDTO.setUpdateAt(bookmark.getUpdatedAt());
    if(bookmark.getUser()!=null) {
      bookmarkDTO.setUserId(bookmark.getUser().getId());
    }
    return bookmarkDTO;
  }

}

package org.fastcampus.community_feed.post.domain.comment;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.fastcampus.community_feed.common.domain.PositiveIntegerCounter;
import org.fastcampus.community_feed.post.domain.Post;
import org.fastcampus.community_feed.post.domain.content.CommentContent;
import org.fastcampus.community_feed.post.domain.content.Content;
import org.fastcampus.community_feed.user.domain.User;

@Getter
public class Comment {
  private final Long id;
  private final Post post;
  private final User author;
  private final Content content;
  private final PositiveIntegerCounter likeCounter;

  @Builder
  public Comment(Long id, Post post, User author, Content content,
          PositiveIntegerCounter likeCounter) {
      if (post == null) {
      throw new IllegalArgumentException("post should not be null");
    }
    if (author == null) {
      throw new IllegalArgumentException("author should not be null");
    }
    if (content == null) {
      throw new IllegalArgumentException("content should not be null or empty");
    }

    this.id = id;
    this.post = post;
    this.author = author;
    this.content = content;
    this.likeCounter = likeCounter;
  }

  public Comment(Long id, Post post, User author, Content content) {
    this(id, post, author, content, new PositiveIntegerCounter());
  }

  public Comment(Long id, Post post, User author, String content) {
   this(id, post, author, new CommentContent(content), new PositiveIntegerCounter());
  }

  public void updateContent(User user, String content) {
    if (!author.equals(user)) {
      throw new IllegalArgumentException("only author can update content");
    }
    this.content.updateContent(content);
  }

  public void like(User user) {
    if (author.equals(user)) {
      throw new IllegalArgumentException("author cannot like own comment");
    }
    likeCounter.increase();
  }

  public void unlike() {
    likeCounter.decrease();
  }

  public int getLikeCount() {
    return likeCounter.getCount();
  }

  public String getContentText() {
    return content.getContentText();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Comment comment = (Comment) o;
    return Objects.equals(id, comment.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}

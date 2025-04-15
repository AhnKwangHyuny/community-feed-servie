package org.fastcampus.community_feed.post.domain;

import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import org.fastcampus.community_feed.common.domain.PositiveIntegerCounter;
import org.fastcampus.community_feed.post.domain.content.Content;
import org.fastcampus.community_feed.post.domain.content.PostContent;
import org.fastcampus.community_feed.user.domain.User;

@Getter
public class Post {
  private final Long id;
  private final User author;
  private final Content content;
  private PostPublicationState state;
  private final PositiveIntegerCounter likeCounter;

  @Builder
  public Post(Long id, User author, Content content, PostPublicationState state, PositiveIntegerCounter positiveIntegerCounter) {
    if (author == null) {
      throw new IllegalArgumentException("author should not be null");
    }
    if (content == null) {
      throw new IllegalArgumentException("content should not be null or empty");
    }

    this.id = id;
    this.author = author;
    this.content = content;
    this.state = state;
    this.likeCounter = positiveIntegerCounter;
  }

  public Post(Long id, User author, Content content) {
    this(id, author, content, PostPublicationState.PUBLIC, new PositiveIntegerCounter());
  }

  public Post(Long id, User author, String content) {
    this(id, author, new PostContent(content), PostPublicationState.PUBLIC, new PositiveIntegerCounter());
  }

  public void updateContent(User user, String content, PostPublicationState state) {
    if (!author.equals(user)) {
      throw new IllegalArgumentException("only author can update content");
    }

    if (state == null) {
      state = PostPublicationState.PUBLIC;
    }

    this.content.updateContent(content);
    this.state = state;
  }

  public void like(User user) {
    if (author.equals(user)) {
      throw new IllegalArgumentException("author cannot like own post");
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
    Post post = (Post) o;
    return Objects.equals(id, post.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}

package org.fastcampus.community_feed.post.domain.content;

import lombok.Getter;

@Getter
public abstract class Content {
  String contentText;
  final DatetimeInfo datetimeInfo;

  Content(String content) {
    checkLength(content);
    this.contentText = content;
    this.datetimeInfo = new DatetimeInfo();
  }

  public void updateContent(String content) {
    checkLength(content);
    this.contentText = content;
    this.datetimeInfo.updateEditDatetime();
  }

  abstract void checkLength(String content);

    public boolean isEdited() {
    return datetimeInfo.isEdited();
  }
}

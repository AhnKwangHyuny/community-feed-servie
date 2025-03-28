package org.faddy.common.domain.content;

import org.faddy.common.domain.DateTimeInfo;

public abstract class Content {

    private String contentText;
    private final DateTimeInfo dateTimeInfo;

    protected Content(String contentTest) {
        checkText(contentTest);
        this.dateTimeInfo = new DateTimeInfo();
        this.contentText = contentTest;
    }

    public abstract void checkText(String contentText);

    public void updateContent(String contentText) {
        checkText(contentText);

        this.contentText = contentText;

        // date 업데이트
        this.dateTimeInfo.updateDateTime();
    }


    public String getContentText() {
        return contentText;
    }
}

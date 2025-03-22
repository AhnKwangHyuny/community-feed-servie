package org.faddy.post.domain.content;

public abstract class Content {

    private final String contentTest;

    protected Content(String contentTest) {
        checkText(contentTest);
        this.contentTest = contentTest;
    }

    public abstract void checkText(String contentTest);

    public String getContentTest() {
        return contentTest;
    }
}

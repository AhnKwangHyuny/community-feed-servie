package org.faddy.community_feed.infra.s3.exception;

public class S3Exception extends RuntimeException {
    
    public S3Exception(String message) {
        super(message);
    }
    
    public S3Exception(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static class FileUploadException extends S3Exception {
        public FileUploadException(String message, Throwable cause) {
            super("Failed to upload file to S3: " + message, cause);
        }
    }
    
    public static class FileDeleteException extends S3Exception {
        public FileDeleteException(String message, Throwable cause) {
            super("Failed to delete file from S3: " + message, cause);
        }
    }
    
    public static class FileDownloadException extends S3Exception {
        public FileDownloadException(String message, Throwable cause) {
            super("Failed to download file from S3: " + message, cause);
        }
    }
}

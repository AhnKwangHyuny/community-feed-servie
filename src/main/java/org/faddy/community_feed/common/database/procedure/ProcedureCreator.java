package org.faddy.community_feed.common.database.procedure;

public interface ProcedureCreator {
    /**
     * 데이터베이스에 프로시저가 존재하는지 확인하고 없으면 생성
     */
    void createProcedureIfNotExists();

    /**
     * 프로시저의 이름 반환
     */
    String getProcedureName();
}
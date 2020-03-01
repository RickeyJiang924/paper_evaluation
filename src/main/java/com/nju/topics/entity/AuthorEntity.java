package com.nju.topics.entity;

public class AuthorEntity {
    //    ZZMC
    private String authorName;

    //    JGMC
    private String authorInstitution;

    //    TXDZ
    private String authorDepartment;

    public String getAuthorDepartment() {
        return authorDepartment;
    }


    public String getAuthorInstitution() {
        return authorInstitution;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorDepartment(String authorDepartment) {
        this.authorDepartment = authorDepartment;
    }

    public void setAuthorInstitution(String authorInstitution) {
        this.authorInstitution = authorInstitution;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

}

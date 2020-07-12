package com.kpatil.jwdnd.cloudstorage.model;

public class Note {
    private Integer noteId;
    private String noteTitle;
    private String nodeDescription;
    private Integer userId;

    public Note(Integer noteId, String noteTitle, String nodeDescription, Integer userId) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.nodeDescription = nodeDescription;
        this.userId = userId;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNodeDescription() {
        return nodeDescription;
    }

    public void setNodeDescription(String nodeDescription) {
        this.nodeDescription = nodeDescription;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}

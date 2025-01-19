package com.example.studyconnect.data.response;

public class SubmissionResponse {
    private int id;
    private int assignment_id;
    private int student_id;
    private String submitted_at;
    private String attachment_path;
    private Integer score;
    private String status;
    private String answer;
    private String created_at;
    private String updated_at;

    public SubmissionResponse(int id, int assignment_id, int student_id, String submitted_at, String attachment_path, Integer score, String status, String answer, String created_at, String updated_at) {
        this.id = id;
        this.assignment_id = assignment_id;
        this.student_id = student_id;
        this.submitted_at = submitted_at;
        this.attachment_path = attachment_path;
        this.score = score;
        this.status = status;
        this.answer = answer;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAssignment_id() {
        return assignment_id;
    }

    public void setAssignment_id(int assignment_id) {
        this.assignment_id = assignment_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getSubmitted_at() {
        return submitted_at;
    }

    public void setSubmitted_at(String submitted_at) {
        this.submitted_at = submitted_at;
    }

    public String getAttachment_path() {
        return attachment_path;
    }

    public void setAttachment_path(String attachment_path) {
        this.attachment_path = attachment_path;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "SubmissionResponse{" +
                "id=" + id +
                ", assignment_id=" + assignment_id +
                ", student_id=" + student_id +
                ", submitted_at='" + submitted_at + '\'' +
                ", attachment_path='" + attachment_path + '\'' +
                ", score=" + score +
                ", status='" + status + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}

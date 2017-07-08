package ubublik.network.rest.entities;

/**
 * Created by Bublik on 18-Jun-17.
 */
public class Report {

    enum ReportType{ QUESTION, USER_REPORT, SERVICE_REPORT }

    private String report_type;
    private Long problem_user_id;
    private String text;

    public Report(String report_type, Long problem_user_id, String text) {
        this.report_type = report_type;
        this.problem_user_id = problem_user_id;
        this.text = text;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public Long getProblem_user_id() {
        return problem_user_id;
    }

    public void setProblem_user_id(Long problem_user_id) {
        this.problem_user_id = problem_user_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

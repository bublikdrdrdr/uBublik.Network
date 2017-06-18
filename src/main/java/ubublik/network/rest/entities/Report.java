package ubublik.network.rest.entities;

/**
 * Created by Bublik on 18-Jun-17.
 */
public class Report {

    enum ReportType{ QUESTION, USER_REPORT, SERVICE_REPORT }

    private String report_type;
    private Long problem_user_id;
    private String text;
}

package zhihu.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ZhihuAnswer {
    private Long id;

    private String editable_content;

    private String content;

    private String excerpt;

    private String collapsed_by;

    private Integer voteup_count;

    private Boolean is_collapsed;

    private String type;

    private Date created_time;

    private Date updated_time;
}

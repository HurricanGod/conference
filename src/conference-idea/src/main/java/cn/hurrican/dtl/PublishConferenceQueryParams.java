package cn.hurrican.dtl;


import lombok.Data;

@Data
public class PublishConferenceQueryParams {

    public Boolean passCheck;
    public Boolean isCrawled;
    public Integer statusCode;
    public Integer offset;
    public Integer number;
}

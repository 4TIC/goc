package es.uji.apps.goc.dto;

import java.util.List;

public class ResponseMessage<T>
{
    private boolean success;
    private String message;
    private Integer totalCount;
    private List<T> data;

    public ResponseMessage()
    {
    }

    public ResponseMessage(boolean success)
    {
        this.success = success;
    }

    public ResponseMessage(boolean success, String message)
    {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess()
    {
        return success;
    }

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Integer getTotalCount()
    {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount)
    {
        this.totalCount = totalCount;
    }

    public List<T> getData()
    {
        return data;
    }

    public void setData(List<T> data)
    {
        this.data = data;
    }
}
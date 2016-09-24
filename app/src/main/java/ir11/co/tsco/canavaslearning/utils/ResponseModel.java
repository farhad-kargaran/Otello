package ir11.co.tsco.canavaslearning.utils;

public class ResponseModel
{
    public  String    response;
    public  int       statusCode;
    public  Exception exception;
    private int       nativeStatusCode;

    public String getResponse()
    {
        return response;
    }

    public void setResponse(String response)
    {
        this.response = response;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public void setStatusCode(int statusCode)
    {
        this.statusCode = statusCode;
    }

    public Exception getException()
    {
        return exception;
    }

    public void setException(Exception exception)
    {
        this.exception = exception;
    }

    public Integer getNativeStatusCode()
    {
        return nativeStatusCode;
    }

    public void setNativeStatusCode(int nativeStatusCode)
    {
        this.nativeStatusCode = nativeStatusCode;
    }

    public ResponseModel()
    {
        response = "";
    }

}

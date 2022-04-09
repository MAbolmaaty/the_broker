package com.emupapps.the_broker.models.report.response;

public class Result {

    private String akar_id;

    private String updated_at;

    private String user_id;

    private String created_at;

    private String id;

    private String message;

    private String type;

    public String getAkar_id ()
    {
        return akar_id;
    }

    public void setAkar_id (String akar_id)
    {
        this.akar_id = akar_id;
    }

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
    }

    public String getUser_id ()
    {
        return user_id;
    }

    public void setUser_id (String user_id)
    {
        this.user_id = user_id;
    }

    public String getCreated_at ()
    {
        return created_at;
    }

    public void setCreated_at (String created_at)
    {
        this.created_at = created_at;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getType ()
    {
        return type;
    }

    public void setType (String type)
    {
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [akar_id = "+akar_id+", updated_at = "+updated_at+", user_id = "+user_id+", created_at = "+created_at+", id = "+id+", message = "+message+", type = "+type+"]";
    }
}

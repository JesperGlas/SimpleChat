package Shared;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Payload {

    private final String divider = "#";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int code;
    private Date timeStamp;
    private String sender;
    private String body;

    public Payload(int code, String username, String body) {
        this.code = code;
        this.timeStamp = new Date();
        this.sender = username;
        this.body = body;
    }

    public Payload(String payloadString) {
        String[] splitString = payloadString.split(divider);
        try {
            this.code = Integer.parseInt(splitString[0]);
            this.timeStamp = dateFormat.parse(splitString[1]);
            this.sender = splitString[2];
            this.body = splitString[3];
        } catch (ParseException e) {
            System.out.println("Fatal error: Could not read payload properly..");
            System.exit(1);
        }
    }

    public int getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public String getSender() {
        return sender;
    }

    public String getTimeStampString() {
        return dateFormat.format(this.timeStamp);
    }

    public String toString() {
        return this.code + divider + getTimeStampString() + divider + this.sender + divider + this.body;
    }
}

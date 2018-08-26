package model;

import java.io.Serializable;
import java.util.Date;

public class LogRecord implements Serializable {

    private Date datatime;
    private String level;
    private String threadName;
    private String className;
    private String loggerContent;

    public Date getDatatime() {
        return datatime;
    }

    public void setDatatime(Date datatime) {
        this.datatime = datatime;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLoggerContent() {
        return loggerContent;
    }

    public void setLoggerContent(String loggerContent) {
        this.loggerContent = loggerContent;
    }

    @Override
    public String toString() {
        return datatime.toString() + " " + level + " " + threadName + " " + className + " " + loggerContent;
    }
}

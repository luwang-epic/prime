package log;


import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 定义格式化接口 用是定义一个format方法，接收ILoggingEvent对象，返回字符串
 * @author wanglu
 * @date 2018/08/26
 */
public interface Formatter {

    String format(ILoggingEvent event);
}


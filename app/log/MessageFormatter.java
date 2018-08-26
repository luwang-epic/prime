package log;


import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 实现Formatter接口，接收ILoggingEvent对象返回一个字符串供消费者处理
 * @author liuyazhuang
 *
 */
public class MessageFormatter implements Formatter {

    @Override
    public String format(ILoggingEvent event) {
        return event.getFormattedMessage();
    }

}

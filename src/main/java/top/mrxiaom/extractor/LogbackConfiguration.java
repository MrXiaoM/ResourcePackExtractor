package top.mrxiaom.extractor;

import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.layout.TTLLLayout;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.ContextAwareBase;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class LogbackConfiguration extends ContextAwareBase implements Configurator {
    @Override
    public ExecutionStatus configure(LoggerContext loggerContext) {
        ConsoleAppender<ILoggingEvent> ca = new ConsoleAppender<>();
        ca.setContext(this.context);
        ca.setName("console");
        {
            PatternLayoutEncoder encoder = new PatternLayoutEncoder();
            encoder.setPattern("%d{MM-dd HH:mm:ss} [%level] %logger{36} -- %msg%n");
            encoder.setCharset(System.console().charset());
            encoder.setContext(this.context);
            encoder.start();
            ca.setEncoder(encoder);
        }
        ca.start();
        Logger rootLogger = loggerContext.getLogger("ROOT");
        rootLogger.addAppender(ca);
        return ExecutionStatus.NEUTRAL;
    }
}

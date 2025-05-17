package top.mrxiaom.extractor;

import ch.qos.logback.classic.LoggerContext;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.mrxiaom.extractor.minecraft.ZipResourcePack;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    static {
        LogbackConfiguration configuration = new LogbackConfiguration();
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        configuration.setContext(context);
        context.reset();
        configuration.configure(context);
    }
    private static Logger LOGGER = LoggerFactory.getLogger("Main");

    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("i", "input", true, "输入文件路径");
        options.addOption("o", "output", true, "输出文件夹路径");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine cmd = parser.parse(options, args);
            if (!cmd.hasOption("i")) {
                formatter.printHelp("extractor <参数>", options);
                return;
            }
            String optOutput = cmd.hasOption("o") ? cmd.getOptionValue("o") : "output";
            File inputFile = new File(cmd.getOptionValue("i"));
            File outputDir = new File(optOutput);

            int bufferSize = 1024 * 1024;
            AtomicInteger counter = new AtomicInteger(0);
            ZipResourcePack resourcePack = ZipResourcePack.read(inputFile);
            resourcePack.walk((path, zipFile, input) -> {
                File file = new File(outputDir, path);
                int current = counter.incrementAndGet();
                try {
                    FileUtils.createParentDirectories(file);
                    try (FileOutputStream out = new FileOutputStream(file);
                         InputStream in = input.get()) {
                        int len;
                        byte[] buffer = new byte[bufferSize];
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                        String max = String.valueOf(zipFile.size());
                        String progress = String.format("[ %" + max.length() + "d / %s ]", current, max);
                        LOGGER.info("{} 已导出 {}", progress, path);
                    }
                } catch (IOException e) {
                    LOGGER.warn("导出文件 {} 时出现异常", path, e);
                }
            });
            LOGGER.info("导出完成! 已导出到 {}", outputDir.getAbsoluteFile());
        } catch (ParseException e) {
            LOGGER.error("参数错误: {}", e.getMessage());
            formatter.printHelp("extractor <参数>", options);
            System.exit(1);
        }
    }
}

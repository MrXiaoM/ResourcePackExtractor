package top.mrxiaom.extractor;

import java.io.InputStream;
import java.util.zip.ZipFile;

public interface ResourceConsumer {
    void accept(String path, ZipFile file, InputSupplier<InputStream> input);
}

package top.mrxiaom.extractor.minecraft;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@FunctionalInterface
public interface InputSupplier<T> {
    static InputSupplier<InputStream> create(ZipFile zipFile, ZipEntry zipEntry) {
        return () -> {
            return zipFile.getInputStream(zipEntry);
        };
    }

    T get() throws IOException;
}

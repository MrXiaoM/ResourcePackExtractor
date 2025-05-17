package top.mrxiaom.extractor.minecraft;

import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipResourcePack {
    private static Logger LOGGER = LoggerFactory.getLogger(ZipResourcePack.class);
    private final ZipFileWrapper zipFile;

    ZipResourcePack(ZipFileWrapper zipFile) {
        this.zipFile = zipFile;
    }

    public void walk(ResourceConsumer consumer) {
        ZipFile zipFile = this.zipFile.open();
        if (zipFile != null) {
            Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
            while (enumeration.hasMoreElements()) {
                ZipEntry zipEntry = enumeration.nextElement();
                if (!zipEntry.isDirectory()) {
                    String path = zipEntry.getName();
                    InputSupplier<InputStream> input = InputSupplier.create(zipFile, zipEntry);
                    consumer.accept(path, zipFile, input);
                }
            }
        }
    }

    public static ZipResourcePack read(File file) {
        ZipFileWrapper zipFile = new ZipFileWrapper(file);
        return new ZipResourcePack(zipFile);
    }

    static class ZipFileWrapper implements AutoCloseable {
        final File file;
        @Nullable
        private ZipFile zip;
        private boolean closed;

        ZipFileWrapper(File file) {
            this.file = file;
        }

        @Nullable
        ZipFile open() {
            if (this.closed) {
                return null;
            } else {
                if (this.zip == null) {
                    try {
                        this.zip = new ZipFile(this.file);
                    } catch (IOException var2) {
                        IOException iOException = var2;
                        ZipResourcePack.LOGGER.error("Failed to open pack {}", this.file, iOException);
                        this.closed = true;
                        return null;
                    }
                }

                return this.zip;
            }
        }

        public void close() {
            if (this.zip != null) {
                IOUtils.closeQuietly(this.zip);
                this.zip = null;
            }
        }

        protected void finalize() throws Throwable {
            this.close();
            super.finalize();
        }
    }
}

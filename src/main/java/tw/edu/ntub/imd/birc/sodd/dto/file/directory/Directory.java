package tw.edu.ntub.imd.birc.sodd.dto.file.directory;

import tw.edu.ntub.imd.birc.sodd.dto.Copyable;
import tw.edu.ntub.imd.birc.sodd.dto.File;
import tw.edu.ntub.imd.birc.sodd.exception.file.FileUnknownException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public interface Directory extends Copyable {
    @Nonnull
    String getName();

    @Nullable
    Path getAbsolutePath();

    default long getSize() {
        try {
            Path absolutePath = getAbsolutePath();
            return absolutePath != null ? Files.size(absolutePath) : 0;
        } catch (IOException e) {
            throw new FileUnknownException(e);
        }
    }

    default void delete() {
        try {
            Path absolutePath = getAbsolutePath();
            if (absolutePath != null) {
                Files.deleteIfExists(absolutePath);
            }
        } catch (IOException e) {
            throw new FileUnknownException(e);
        }
    }

    default boolean isExist() {
        Path absolutePath = getAbsolutePath();
        return absolutePath != null && Files.exists(absolutePath);
    }

    default boolean isNotExist() {
        return !isExist();
    }

    default void create() {
        try {
            Path absolutePath = Objects.requireNonNull(getAbsolutePath(), "該資料夾沒有設定路徑");
            if (isNotExist()) {
                Files.createDirectories(absolutePath);
            }
        } catch (IOException e) {
            throw new FileUnknownException(e);
        }
    }

    void reloadContent();

    Directory addDirectory(String newDirectoryName);

    List<Directory> getSubDirectoryList();

    default Optional<Directory> getDirectory(@Nonnull String directoryName) {
        return getSubDirectoryList().parallelStream()
                .filter(subDirectory -> subDirectory.getName().equals(directoryName))
                .findFirst();
    }

    int getSubDirectoryCount();

    List<File> getSubFileList();

    default Optional<File> getFile(@Nonnull String fileName) {
        return getSubFileList().parallelStream()
                .filter(subFile -> Objects.nonNull(subFile.getName()))
                .filter(subFile -> subFile.getName().equals(fileName))
                .findFirst();
    }

    int getSubFileCount();

    default int getAllDirectoryAndFileCount() {
        return getSubDirectoryCount() + getSubFileCount();
    }

    @Override
    default void copyTo(Directory newDirectory, StandardCopyOption... options) {
        try {
            Path absolutePath = Objects.requireNonNull(getAbsolutePath(), "該資料夾沒有設定路徑");
            Path newDirectoryPath = Objects.requireNonNull(newDirectory.getAbsolutePath(), "該資料夾沒有設定路徑");
            Files.copy(absolutePath, newDirectoryPath, options);
        } catch (IOException e) {
            throw new FileUnknownException(e);
        }
    }
}

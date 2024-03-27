package tw.edu.ntub.imd.birc.sodd.dto.file.directory;


import tw.edu.ntub.imd.birc.sodd.dto.File;
import tw.edu.ntub.imd.birc.sodd.dto.FileFactory;
import tw.edu.ntub.imd.birc.sodd.exception.file.FileUnknownException;
import tw.edu.ntub.imd.birc.sodd.util.file.FileUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DirectoryImpl implements Directory {
    private final String name;
    private final Path path;
    private final List<Directory> directoryList = new ArrayList<>();
    private final List<File> fileList = new ArrayList<>();

    public DirectoryImpl(Path path) {
        Path fileName = path.getFileName();
        this.name = fileName.toString();
        this.path = path;
        if (isExist()) {
            reloadContent();
        }
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }

    @Nullable
    @Override
    public Path getAbsolutePath() {
        return path;
    }

    @Override
    public void reloadContent() {
        try {
            directoryList.clear();
            fileList.clear();
            Files.list(path).forEach(this::addDirectoryOfFile);
        } catch (IOException e) {
            throw new FileUnknownException(e);
        }
    }

    @Override
    public Directory addDirectory(String newDirectoryName) {
        Directory newDirectory = new DirectoryImpl(path.resolve(newDirectoryName));
        newDirectory.create();
        directoryList.add(newDirectory);
        return newDirectory;
    }

    @Override
    public List<Directory> getSubDirectoryList() {
        return Collections.unmodifiableList(directoryList);
    }

    private void addDirectoryOfFile(Path path) {
        if (FileUtils.isDirectory(path)) {
            directoryList.add(DirectoryFactory.create(path));
        } else {
            fileList.add(FileFactory.create(path));
        }
    }

    @Override
    public int getSubDirectoryCount() {
        return directoryList.size();
    }

    @Override
    public List<File> getSubFileList() {
        return Collections.unmodifiableList(fileList);
    }

    @Override
    public int getSubFileCount() {
        return fileList.size();
    }
}

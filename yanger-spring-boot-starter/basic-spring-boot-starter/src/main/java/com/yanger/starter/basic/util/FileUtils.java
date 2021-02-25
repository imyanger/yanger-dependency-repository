package com.yanger.starter.basic.util;

import com.yanger.starter.basic.constant.ConfigKey;
import com.yanger.tools.general.constant.CharPool;
import com.yanger.tools.general.constant.Charsets;
import com.yanger.tools.general.constant.StringPool;
import com.yanger.tools.general.tools.StringTools;
import com.yanger.tools.web.exception.Exceptions;
import com.yanger.tools.web.tools.IoUtils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.PatternMatchUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description 文件工具类
 * @Author yanger
 * @Date 2021/2/25 10:13
 */
@Slf4j
@UtilityClass
@SuppressWarnings("checkstyle:MethodLimit")
public class FileUtils extends org.springframework.util.FileCopyUtils {

    /**
     * 扫描目录下的文件
     *
     * @param path 路径
     * @return 文件集合 list
     */
    public static List<File> list(String path) {
        File file = new File(path);
        return list(file, TrueFilter.TRUE);
    }

    /**
     * 扫描目录下的文件
     *
     * @param path            路径
     * @param fileNamePattern 文件名 * 号
     * @return 文件集合 list
     */
    public static List<File> list(String path, String fileNamePattern) {
        File file = new File(path);
        return list(file, pathname -> {
            String fileName = pathname.getName();
            return PatternMatchUtils.simpleMatch(fileNamePattern, fileName);
        });
    }

    /**
     * 扫描目录下的文件
     *
     * @param path   路径
     * @param filter 文件过滤
     * @return 文件集合 list
     */
    public static List<File> list(String path, FileFilter filter) {
        File file = new File(path);
        return list(file, filter);
    }

    /**
     * 扫描目录下的文件
     *
     * @param file 文件
     * @return 文件集合 list
     */
    public static List<File> list(File file) {
        List<File> fileList = new ArrayList<>();
        return list(file, fileList, TrueFilter.TRUE);
    }

    /**
     * 扫描目录下的文件
     *
     * @param file            文件
     * @param fileNamePattern Spring AntPathMatcher 规则
     * @return 文件集合 list
     */
    public static List<File> list(File file, String fileNamePattern) {
        List<File> fileList = new ArrayList<>();
        return list(file, fileList, pathname -> {
            String fileName = pathname.getName();
            return PatternMatchUtils.simpleMatch(fileNamePattern, fileName);
        });
    }

    /**
     * 扫描目录下的文件
     *
     * @param file   文件
     * @param filter 文件过滤
     * @return 文件集合 list
     */
    public static List<File> list(File file, FileFilter filter) {
        List<File> fileList = new ArrayList<>();
        return list(file, fileList, filter);
    }

    /**
     * 扫描目录下的文件
     *
     * @param file     文件
     * @param fileList file list
     * @param filter   文件过滤
     * @return 文件集合 list
     */
    @Contract("_, _, _ -> param2")
    private static List<File> list(@NotNull File file, List<File> fileList, FileFilter filter) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    list(f, fileList, filter);
                }
            }
        } else {
            // 过滤文件
            boolean accept = filter.accept(file);
            if (file.exists() && accept) {
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * 获取文件后缀名
     *
     * @param fullName 文件全名
     * @return {String}
     */
    @NotNull
    public static String getFileExtension(String fullName) {
        Assert.notNull(fullName, "file fullName is null.");
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    /**
     * 获取文件名,去除后缀名
     *
     * @param file 文件
     * @return {String}
     */
    public static @NotNull String getNameWithoutExtension(String file) {
        Assert.notNull(file, "file is null.");
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf(CharPool.DOT);
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    /**
     * Returns a {@link File} representing the system temporary directory.
     *
     * @return the system temporary directory.
     */
    @NotNull
    @Contract(" -> new")
    public static File getTempDir() {
        return new File(getTempDirPath());
    }

    /**
     * Returns the path to the system temporary directory.
     *
     * @return the path to the system temporary directory.
     */
    public static String getTempDirPath() {
        return System.getProperty(ConfigKey.JvmConfigKey.TMP_DIR);
    }

    /**
     * 拼接临时文件目录.
     *
     * @param subDirFile sub dir file
     * @return 临时文件目录. string
     */
    @NotNull
    public static String toTempDirPath(String subDirFile) {
        return FileUtils.toTempDir(subDirFile).getAbsolutePath();
    }

    /**
     * 拼接临时文件目录.
     *
     * @param subDirFile sub dir file
     * @return the system temporary directory.
     */
    public static @NotNull File toTempDir(@NotNull String subDirFile) {
        String tempDirPath = FileUtils.getTempDirPath();
        if (subDirFile.startsWith(StringPool.SLASH)) {
            subDirFile = subDirFile.substring(1);
        }
        String fullPath = tempDirPath.concat(subDirFile);
        File fullFilePath = new File(fullPath);
        File dir = fullFilePath.getParentFile();
        if (!dir.exists() && dir.mkdirs()) {
            log.debug("{} 不存在, 创建成功", dir);
        }
        return fullFilePath;
    }

    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     */
    @NotNull
    public static String readToString(File file) {
        return readToString(file, Charsets.UTF_8);
    }

    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file     the file to read, must not be {@code null}
     * @param encoding the encoding to use, {@code null} means platform default
     * @return the file contents, never {@code null}
     */
    @NotNull
    public static String readToString(File file, Charset encoding) {
        try (InputStream in = Files.newInputStream(file.toPath())) {
            return IoUtils.toString(in, encoding);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Reads the contents of a file into a String.
     * The file is always closed.
     *
     * @param file the file to read, must not be {@code null}
     * @return the file contents, never {@code null}
     */
    @NotNull
    public static byte[] readToByteArray(File file) {
        try (InputStream in = Files.newInputStream(file.toPath())) {
            return IoUtils.toByteArray(in);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file the file to write
     * @param data the content to write to the file
     */
    public static void writeToFile(File file, String data) {
        writeToFile(file, data, Charsets.UTF_8, false);
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param encoding the encoding to use, {@code null} means platform default
     * @param append   if {@code true}, then the String will be added to the end of the file rather than overwriting
     */
    public static void writeToFile(File file, String data, Charset encoding, boolean append) {
        try (OutputStream out = new FileOutputStream(file, append)) {
            IoUtils.write(data, out, encoding);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file   the file to write
     * @param data   the content to write to the file
     * @param append if {@code true}, then the String will be added to the end of the file rather than overwriting
     */
    public static void writeToFile(File file, String data, boolean append) {
        writeToFile(file, data, Charsets.UTF_8, append);
    }

    /**
     * Writes a String to a file creating the file if it does not exist.
     *
     * @param file     the file to write
     * @param data     the content to write to the file
     * @param encoding the encoding to use, {@code null} means platform default
     */
    public static void writeToFile(File file, String data, Charset encoding) {
        writeToFile(file, data, encoding, false);
    }

    /**
     * 转成file
     *
     * @param in   InputStream
     * @param file File
     */
    public static void toFile(InputStream in, File file) {
        try (OutputStream out = new FileOutputStream(file)) {
            FileUtils.copy(in, out);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * Moves a file.
     * <p>
     * When the destination file is on another file system, do a "copy and delete".
     *
     * @param srcFile  the file to be moved
     * @param destFile the destination file
     * @throws IOException if source or destination is invalid
     */
    public static void moveFile(File srcFile, File destFile) throws IOException {
        Assert.notNull(srcFile, "Source must not be null");
        Assert.notNull(destFile, "Destination must not be null");
        if (!srcFile.exists()) {
            throw new FileNotFoundException("Source '" + srcFile + "' does not exist");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("Source '" + srcFile + "' is a directory");
        }
        if (destFile.exists()) {
            throw new IOException("Destination '" + destFile + "' already exists");
        }
        if (destFile.isDirectory()) {
            throw new IOException("Destination '" + destFile + "' is a directory");
        }
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            FileUtils.copy(srcFile, destFile);
            if (!srcFile.delete()) {
                FileUtils.deleteQuietly(destFile);
                throw new IOException("Failed to delete original file '" + srcFile + "' after copy to '" + destFile + "'");
            }
        }
    }

    /**
     * Deletes a file, never throwing an exception. If file is a directory, delete it and all sub-directories.
     * <p>
     * The difference between File.delete() and this method are:
     * <ul>
     * <li>A directory to be deleted does not have to be empty.</li>
     * <li>No exceptions are thrown when a file or directory cannot be deleted.</li>
     * </ul>
     *
     * @param file file or directory to delete, can be {@code null}
     * @return {@code true} if the file or directory was deleted, otherwise {@code false}
     */
    @Contract("null -> false")
    public static boolean deleteQuietly(@Nullable File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                FileSystemUtils.deleteRecursively(file);
            }
        } catch (Exception ignored) {
        }

        try {
            return file.delete();
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * NIO 按行读取文件
     *
     * @param path 文件路径
     * @return 行列表 list
     */
    public static List<String> readLines(String path) {
        return readLines(Paths.get(path));
    }

    /**
     * NIO 按行读取文件
     *
     * @param path 文件路径
     * @return 行列表 list
     */
    public static List<String> readLines(Path path) {
        return readLines(path, Charsets.UTF_8);
    }

    /**
     * NIO 按行读取文件
     *
     * @param path 文件路径
     * @param cs   字符集
     * @return 行列表 list
     */
    public static List<String> readLines(Path path, Charset cs) {
        try {
            return Files.readAllLines(path, cs);
        } catch (IOException e) {
            throw Exceptions.unchecked(e);
        }
    }

    /**
     * NIO 按行读取文件
     *
     * @param file 文件
     * @return 行列表 list
     */
    public static List<String> readLines(@NotNull File file) {
        return readLines(file.toPath());
    }

    /**
     * NIO 按行读取文件
     *
     * @param path 文件路径
     * @param cs   字符集
     * @return 行列表 list
     */
    public static List<String> readLines(String path, Charset cs) {
        return readLines(Paths.get(path), cs);
    }

    /**
     * NIO 按行读取文件
     *
     * @param file 文件
     * @param cs   字符集
     * @return 行列表 list
     */
    public static List<String> readLines(@NotNull File file, Charset cs) {
        return readLines(file.toPath(), cs);
    }

    /**
     * 转换成不同平台的下的路径
     *
     * @param path the path
     * @return the string
     */
    @NotNull
    public static String getRealFilePath(@NotNull String path) {
        return path.replace("/", File.separator).replace("\\", File.separator);
    }

    /**
     * 拼接不同平台下的文件路径, 末尾不含路径分隔符
     *
     * @param paths paths
     * @return the string
     */
    @NotNull
    public static String appendPath(@NotNull String... paths) {
        // 删除前缀后后缀
        for (int i = 0; i < paths.length; i++) {
            paths[i] = StringTools.removeSuffix(paths[i], File.separator);
            // 第一个路径不删除前缀
            if (i == 0) {
                continue;
            }
            paths[i] = StringTools.removePrefix(paths[i], File.separator);
        }

        return String.join(File.separator, paths);
    }

    /**
     * 递归删除指定目录及文件
     *
     * @param first first
     * @param more  more
     * @return the boolean
     */
    @SneakyThrows
    public static boolean deleteFiles(String first, String... more) {
        Path start = Paths.get(first, more);
        if (start.toFile().exists()) {
            Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    try {
                        Files.delete(file);
                    } catch (IOException ignored) {
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException e) {
                    if (e == null) {
                        try {
                            Files.delete(dir);
                        } catch (IOException ignored) {
                        }
                        return FileVisitResult.CONTINUE;
                    }
                    // 如果存在异常, 则说明文件不存在
                    return FileVisitResult.SKIP_SUBTREE;
                }
            });
        }

        return true;
    }

    /**
     * 创建文件路径且处理 path 最后一个路径分隔符
     *
     * @param path path
     * @return the string
     */
    public static @NotNull String toPath(String path) {
        File logFile = new File(path);
        if (!logFile.exists() && logFile.mkdirs()) {
            log.info(String.format("创建日志目录: %s", logFile.getPath()));
        }
        return logFile.getPath();
    }

    /**
     * 默认为true
     */
    public static class TrueFilter implements FileFilter, Serializable {

        private static final long serialVersionUID = -6420452043795072619L;

        public static final TrueFilter TRUE = new TrueFilter();

        @Override
        public boolean accept(File pathname) {
            return true;
        }

    }

    /**
     * Touch
     *
     * @param file file
     * @throws IOException io exception
     */
    public static void touch(@NotNull File file) throws IOException {
        if (!file.exists()) {
            OutputStream out = openOutputStream(file);
            IoUtils.closeQuietly(out);
        }

        boolean success = file.setLastModified(System.currentTimeMillis());
        if (!success) {
            throw new IOException("Unable to set the last modification time for " + file);
        }
    }

    /**
     * Open output stream
     *
     * @param file file
     * @return the file output stream
     * @throws IOException io exception
     */
    public static @NotNull FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    /**
     * Open output stream
     *
     * @param file   file
     * @param append append
     * @return the file output stream
     * @throws IOException io exception
     */
    @Contract("_, _ -> new")
    public static @NotNull FileOutputStream openOutputStream(@NotNull File file, boolean append) throws IOException {
        if (file.exists()) {
            if (file.isDirectory()) {
                throw new IOException("File '" + file + "' exists but is a directory");
            }

            if (!file.canWrite()) {
                throw new IOException("File '" + file + "' cannot be written to");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null && !parent.mkdirs() && !parent.isDirectory()) {
                throw new IOException("Directory '" + parent + "' could not be created");
            }
        }
        return new FileOutputStream(file, append);
    }

}

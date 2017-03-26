package com.lawrence.core.lib.utils.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


/**
 * 文件处理工具类
 * <p>
 * 包含文件的创建,复制,粘贴,剪切,读取,存储,获取大小,解压缩等等
 * Created by xu on 2015/8/14.
 */
public class FileUtil {


    private static final String TAG = "FileUtil";
    public static final int SIZE_TYPE_B = 0x0001;//获取文件大小单位为B的double值
    public static final int SIZE_TYPE_KB = 0x0002;//获取文件大小单位为KB的double值
    public static final int SIZE_TYPE_MB = 0x0003;//获取文件大小单位为MB的double值
    public static final int SIZE_TYPE_GB = 0x0004;//获取文件大小单位为GB的double值
    private static final int BUFF_SIZE = 8192;

    /**
     * 获取文件指定文件的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     * @throws IOException 获取文件出错,返回IOException异常
     */
    public static double getFileSize(String filePath, int sizeType) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException("文件未找到!");
        }
        long blockSize = 0;
        if (file.isDirectory()) {
            blockSize = getFileSizes(file);
        } else {
            blockSize = getFileSize(file);
        }
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getFileSize(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new FileNotFoundException(filePath + " 文件未找到!");
        }
        long blockSize = 0;
        if (file.isDirectory()) {
            blockSize = getFileSizes(file);
        } else {
            blockSize = getFileSize(file);
        }
        return formatFileSize(blockSize);
    }

    /**
     * 获取指定文件大小
     *
     * @param file 目标文件
     * @return 文件的大小
     * @throws Exception
     */
    private static long getFileSize(File file) throws IOException {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
        } else {
            Logger.error(FileUtil.class.getSimpleName(), "File not found");
        }
        return size;
    }

    /**
     * 获取指定文件夹
     *
     * @param file 文件
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File file) throws IOException {
        long size = 0;
        File[] fLists = file.listFiles();
        for (File childFile : fLists) {
            if (file.isDirectory()) {
                size = size + getFileSizes(childFile);
            } else {
                size = size + getFileSize(childFile);
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS 文件大小
     * @return 但前文件的大小 使用最简形式,
     */
    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";   // 小于1024Kb 使用 B 为单位
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB"; // 小于1024Kb 使用 Kb为单位
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB"; // 小于 1024Mb 使用 Mb 为单位
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB"; // 小于 1024 G 使用 Gb 为单位
        }
        return fileSizeString;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double formatFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZE_TYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZE_TYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZE_TYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZE_TYPE_GB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    /**
     * 剪切文件
     *
     * @param srcFile   源文件
     * @param targetDir 目标地址
     * @throws IOException
     */
    public static void cutFile(File srcFile, String targetDir) throws IOException {
        copyFile(srcFile, targetDir);
        deleteFile(srcFile);
    }

    public static void cutFile(String srcFile, String targetDir) throws IOException {
        copyFile(new File(srcFile), targetDir);
        deleteFile(srcFile);
    }

    /**
     * 解压文件
     *
     * @param zipPath    原始 zip 文件目录
     * @param folderPath 目标路径
     * @throws IOException
     */
    public static void unCompressZipFile(String zipPath, String folderPath) throws IOException {
        if (StringUtil.isEmpty(zipPath)) {
            throw new IllegalArgumentException("非法参数 zipPath = " + zipPath);
        }
        unCompressZipFile(new File(zipPath), folderPath);
    }

    /**
     * 解压缩一个文件
     *
     * @param zip        要解压的压缩文件
     * @param folderPath 解压缩的目标目录
     * @throws IOException 当解压缩过程出错时抛出
     */
    public static void unCompressZipFile(File zip, String folderPath) throws IOException {

        if (!zip.exists()) {
            throw new FileNotFoundException("未找到 zip 文件  zip = " + zip.getPath());
        }

        try {
            // 先指定压缩档的位置和档名，建立FileInputStream对象
            FileInputStream fins = new FileInputStream(zip);
            // 将fins传入ZipInputStream中
            ZipInputStream zins = new ZipInputStream(fins);
            ZipEntry ze = null;
            byte[] ch = new byte[256];
            while ((ze = zins.getNextEntry()) != null) {
                File zfile = new File(folderPath + ze.getName());
                File fpath = new File(zfile.getParentFile().getPath());
                if (ze.isDirectory()) {
                    if (!zfile.exists())
                        zfile.mkdirs();
                    zins.closeEntry();
                } else {
                    if (!fpath.exists())
                        fpath.mkdirs();
                    FileOutputStream fos = new FileOutputStream(zfile);
                    int i;
                    while ((i = zins.read(ch)) != -1)
                        fos.write(ch, 0, i);
                    zins.closeEntry();
                    fos.close();
                }
            }
            fins.close();
            zins.close();


        } catch (Exception e) {
            System.err.println("Extract error:" + e.getMessage());
        }
    }


    public static void unComp(String zipPath, String unZipPath) {
        try {
            ZipInputStream Zin = new ZipInputStream(new FileInputStream(zipPath));//输入源zip路径
            BufferedInputStream Bin = new BufferedInputStream(Zin);
            String Parent = unZipPath; //输出路径（文件夹目录）
            File Fout = null;
            ZipEntry entry;
            try {
                while ((entry = Zin.getNextEntry()) != null && !entry.isDirectory()) {
                    Fout = new File(Parent, entry.getName());
                    if (!Fout.exists()) {
                        (new File(Fout.getParent())).mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(Fout);
                    BufferedOutputStream Bout = new BufferedOutputStream(out);
                    int b;
                    while ((b = Bin.read()) != -1) {
                        Bout.write(b);
                    }
                    Bout.close();
                    out.close();
                    System.out.println(Fout + "解压成功");
                }
                Bin.close();
                Zin.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建文件
     * <p>
     * 如果文件不存在, 则创建相应的文件和文件夹
     * <p>
     * 如果文件存在,则返回这个文件的对象
     *
     * @param filePath 文件名 带路径
     * @return 当前文件对象
     */
    public static File createFile(String filePath) throws IOException {
        if (StringUtil.isEmpty(filePath)) {
            throw new IllegalArgumentException(filePath + " 非法路径!");
        }
        return createFile(new File(filePath));
    }

    private static File createFile(File file) throws IOException {
        Logger.debug(TAG, "createFile: file path = " + file.getPath());
        if (!file.exists()) {
            if (file.isDirectory()) {
                file.mkdirs();
            } else {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                file.createNewFile();
            }
        }

        return file;
    }

    /**
     * 复制文件到其他目录下
     *
     * @param srcPath   源文件,也可以是文件夹 路径
     * @param targetDir 目标文件夹路径
     * @throws FileNotFoundException 当被复制的文件未找到时, 返回 FileNotFoundException
     *                               创建目标目录失败,返回 IOException
     */
    public static void copyFile(String srcPath, String targetDir) throws IOException {
        if (TextUtils.isEmpty(srcPath)) {
            throw new IllegalArgumentException("srcPath : " + srcPath + " 非法路径!");
        }
        copyFile(new File(srcPath), targetDir);
    }

    /**
     * 复制文件到其他目录下
     *
     * @param srcFile   原始文件路径
     * @param targetDir 目标目录路径
     * @throws IOException
     */
    public static void copyFile(File srcFile, String targetDir) throws IOException {

        File targetFile = null;

        if (!srcFile.exists()) {
            throw new FileNotFoundException(srcFile.getAbsolutePath() + " 原始文件不存在!");
        }

        if (TextUtils.isEmpty(targetDir)) {
            throw new IllegalArgumentException("targetDir = " + targetDir + " 非法路径");
        } else {
            targetFile = createFile(targetDir);
        }

        if (srcFile.isDirectory()) { // 文件夹,循环复制子文件到目标目录中
            String parentPathName = srcFile.getName();
            File[] childFiles = srcFile.listFiles();
            for (File childFile : childFiles) {
                copyFile(childFile, targetFile.getAbsolutePath() + File.separator + parentPathName);
            }
        } else {
            // 复制文件到目标目录中
            FileInputStream fis = new FileInputStream(srcFile);
            File childFile = createFile(targetFile.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(childFile);
            FileChannel inChannel = fis.getChannel();// 得到对应的文件通道
            FileChannel outChannel = fos.getChannel();// 得到对应的文件通道
            inChannel.transferTo(0, inChannel.size(), outChannel); // 连接两个通道，并且从inChannel通道中读取，写入到outChannel中
            // 完成后关闭流操作
            fis.close();
            fos.close();
            inChannel.close();
            outChannel.close();
        }
    }

    /**
     * 压缩文件/文件夹
     *
     * @param resFile     源文件
     * @param zipFilePath 压缩文件存储路径
     * @throws Exception
     */
    public static void compressFile(File resFile, String zipFilePath) throws Exception {

        if (!resFile.exists()) {
            throw new FileNotFoundException(resFile.getAbsolutePath() + " 原始文件未找到!");
        }

        if (TextUtils.isEmpty(zipFilePath)) {
            throw new IllegalArgumentException(zipFilePath + " 非法路径!");
        }

        ZipOutputStream zip = new ZipOutputStream(
                new FileOutputStream(createFile(zipFilePath + ".zip")));
        compressFile(resFile, zip, "");
        zip.finish();
        zip.close();
    }

//    public static void archiveCompressFile(final String resFilePath, final String zipFilePath) throws Exception {
//
//        if (TextUtils.isEmpty(resFilePath)) {
//            throw new IllegalArgumentException(resFilePath + " 原始文件未找到");
//        }
//
//        if (TextUtils.isEmpty(zipFilePath) || !zipFilePath.endsWith(".zip")) {
//            throw new IllegalArgumentException(zipFilePath + " 非法路径");
//        }
//
//        archiveCompressFile(new File(resFilePath), zipFilePath);
//
//    }


//    public static void archiveCompressFile(final File resFile, final String zipFilePath) throws Exception {
//
//        if (!FileUtil.checkExist(resFile)) {
//            throw new FileNotFoundException(resFile.getAbsolutePath() + " 文件未找到");
//        }
//
//        ZipArchiveOutputStream zipArchiveOutputStream = new ZipArchiveOutputStream(new FileOutputStream(zipFilePath));
//        compressFolderChangeToZip(resFile, zipArchiveOutputStream, "");
//        zipArchiveOutputStream.closeArchiveEntry();
//        zipArchiveOutputStream.close();
//    }
//
//
//    private static void compressFolderChangeToZip(File compressedFile, ZipArchiveOutputStream zipArchiveOutputStream, String base) throws IOException {
//        FileInputStream fileInputStream = null;
//        if (compressedFile.isDirectory()) {
//            File[] childrenCompressedFileList = compressedFile.listFiles();
//            base = base.length() == 0 ? "" : base + File.separator;
//            for (File file : childrenCompressedFileList) {
//                compressFolderChangeToZip(file, zipArchiveOutputStream, base + file.getName());
//            }
//        } else {
//            if ("".equalsIgnoreCase(base)) {
//                base = compressedFile.getName();
//            }
//
//            Logger.debug(TAG, "compressFolderChangeToZip:  file name = " + compressedFile.getName() + "\n file size = " + FileUtil.getFileSize(compressedFile.getPath(), FileUtil.SIZE_TYPE_MB));
//            zipArchiveOutputStream.putArchiveEntry(new ZipArchiveEntry(base));
//            fileInputStream = new FileInputStream(compressedFile);
//            int b;
//
//            byte[] bytes = new byte[BUFF_SIZE];
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, BUFF_SIZE);
//            while ((b = bufferedInputStream.read(bytes)) != -1) {
//                zipArchiveOutputStream.write(bytes, 0, b);
//            }
//            bufferedInputStream.close();
//        }
//    }

    // 文件压缩
    private static void compressFile(File resFile, ZipOutputStream zipOutputStream, String dir) throws Exception {

        dir = dir + (dir.trim().length() == 0 ? "" : File.separator) + resFile.getName();

        if (resFile.isDirectory()) {
            File[] fileList = resFile.listFiles();

            zipOutputStream.putNextEntry(new ZipEntry(dir));

            for (File file : fileList) {
                compressFile(file, zipOutputStream, dir);
            }
        } else if (resFile.isFile()) {

            Logger.debug(TAG, "compressFolderChangeToZip:  file name = " + resFile.getName() + "\n file size = " + FileUtil.getFileSize(resFile.getPath(), FileUtil.SIZE_TYPE_MB));
            byte buffer[] = new byte[BUFF_SIZE];
            BufferedInputStream in = new BufferedInputStream(
                    new FileInputStream(resFile), BUFF_SIZE);
            zipOutputStream.putNextEntry(new ZipEntry(dir));
            int realLength;
            while ((realLength = in.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, realLength);
            }
            in.close();
            zipOutputStream.flush();
            zipOutputStream.closeEntry();
        }
    }

    /**
     * 删除目标文件或文件夹
     *
     * @param file Target file
     */
    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File childrenFile : files) {

                deleteFile(childrenFile);
            }
        }
        // 如果是文件夹，删除最外面一层文件夹
        File to = new File(file.getAbsolutePath() + System.currentTimeMillis());
        file.renameTo(to);
        to.delete();
    }

    /**
     * delete file by path
     *
     * @param targetFilePath target file path
     */
    public static void deleteFile(String targetFilePath) throws IOException {
        if (TextUtils.isEmpty(targetFilePath)) {
            throw new IllegalArgumentException(targetFilePath + " 非法路径!");
        }
        deleteFile(new File(targetFilePath));
    }

//    /**
//     * 解析XMl文件
//     *
//     * @return
//     * @throws XmlPullParserException
//     * @throws IOException
//     */
//    public static List<MallInfoVo> getMallInfo(Context context, String str_city_name) throws XmlPullParserException, IOException {
//
//        InputStream is = context.getAssets().open(str_city_name + "_mall.xml");
//        XmlPullParser parser = Xml.newPullParser();
//        parser.setInput(is, "UTF_8");
//
//        List<MallInfoVo> list = new ArrayList();
//        int eventType = parser.getEventType();
//
//        while (eventType != parser.END_DOCUMENT) {
//
//            switch (eventType) {
//                case XmlPullParser.START_DOCUMENT:
//                    break;
//                case XmlPullParser.START_TAG:
//                    if (parser.getName().equals("malls")) {
//                        break;
//                    } else if (parser.getName().equals("mall")) {
//                        MallInfoVo mallInfoVo = new MallInfoVo();
//                        for (int i = 0; i < parser.getAttributeCount(); i++) {
//                            if (parser.getAttributeName(i).equals("py")) {
//                                mallInfoVo.seteMallName(parser.getAttributeValue(i));
//                            }
//                            if (parser.getAttributeName(i).equals("name")) {
//                                mallInfoVo.setcMallName(parser.getAttributeValue(i));
//                            }
//                            if (parser.getAttributeName(i).equals("floor")) {
////                                mallInfo.setFloorNum(parser.getAttributeValue(i));
//                            }
//                        }
//                        list.add(mallInfoVo);
//                    }
//                    break;
//                case XmlPullParser.END_TAG:
//                    break;
//
//                default:
//                    break;
//            }
//            eventType = parser.next();
//        }
//        return list;
//    }


    /**
     * 获取缓存文件位置
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) throws IOException {
        if (context == null) {
            throw new IllegalArgumentException("context is null");
        }
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable())
            cachePath = context.getExternalCacheDir().getPath();
        else
            cachePath = Environment.getExternalStorageDirectory().getPath();
        return new File(cachePath + File.separator + uniqueName);
    }


    /**
     * read content from file by utf-8
     *
     * @param path the file`s path
     * @return the file`s content
     * @throws IOException
     */
    public static String readFile(String path) throws IOException {

        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException(path + " 非法路径!");
        }
        return readFile(new File(path));
    }

    public static String readFile(File file) throws IOException {
        if (!file.exists()) {
            throw new FileNotFoundException(file.getAbsolutePath() + " 文件未找到!");
        }

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String tempString;
        while ((tempString = reader.readLine()) != null) {
            stringBuilder.append(tempString).append("\n");
        }

        reader.close();
        return stringBuilder.toString();
    }

    private static String readFile(Reader reader) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();

        int len;
        while ((len = reader.read()) != -1) {
            stringBuilder.append(len);
        }
        reader.close();
        return stringBuilder.toString();
    }

    public static String readFile(InputStream inputStream) throws IOException {
        if (inputStream.available() == 0) {
            throw new IllegalArgumentException("this is a empty file!");
        }
        StringBuilder stringBuilder = new StringBuilder();

        int len;
        while ((len = inputStream.read()) != -1) {
            stringBuilder.append(len);
        }

        inputStream.close();
        return stringBuilder.toString();
    }

    /**
     * write string to file by utf-8
     *
     * @param path target file`s path
     * @param data the content
     * @throws IOException
     */
    public static void writeFile(String path, String data) throws IOException {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException(path + " 非法路径!");
        }

        File file = createFile(path);
        BufferedWriter bufferWriter = new BufferedWriter(new FileWriter(file, true));
        bufferWriter.write(data);
        bufferWriter.close();
    }


    public static void writeFile(String path, byte[] data) throws IOException {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException(path + " 非法路径!");
        }

        if (data == null) {
            throw new NullPointerException("写入非法数据！");
        }

        File file = createFile(path);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(data);
        fileOutputStream.close();
    }

    public static void writeFile(String path, InputStream inputStream) throws IOException {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException(path + "非法路径!");
        }

        if (inputStream == null || inputStream.available() == 0) {
            throw new NullPointerException("流数据为空！");
        }

        File file = createFile(path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        int len;
        while ((len = bufferedReader.read()) != -1) {
            bufferedWriter.write(len);
        }
        bufferedReader.close();
        bufferedReader.close();
    }

    public static boolean checkExist(String path) throws IOException {
        return checkExist(new File(path));
    }

    public static boolean checkExist(File file) {
        return file.exists();
    }
}

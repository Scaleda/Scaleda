// package top.criwits.scaleda.kernel.utils;
//
// import java.io.File;
// import java.io.IOException;
// import java.io.InputStream;
// import java.net.URI;
// import java.util.AbstractMap;
// import java.util.List;
// import java.util.Map;
// import java.util.jar.JarFile;
// import java.util.stream.Collectors;
// import java.util.stream.Stream;
// import java.util.zip.ZipEntry;
//
// public class KernelFileUtilJava {
//     /**
//      * 复制jar包中的资源文件到指定目录
//      *
//      * @param path       jar包所在路径
//      * @param tempPath   保存目录
//      * @param filePrefix 需要进行复制的资源文件目录：以BOOT-INF/classes/开头
//      */
//     public static void copyJarResourcesFileToTemp(URI path, String tempPath, String filePrefix) {
//         try {
//             List<Map.Entry<ZipEntry, InputStream>> collect =
//                     readJarFile(new JarFile(path.getPath()), filePrefix).collect(Collectors.toList());
//             for (Map.Entry<ZipEntry, InputStream> entry : collect) {
//                 // 文件相对路径
//                 String key = entry.getKey().getName();
//                 // 文件流
//                 InputStream stream = entry.getValue();
//                 File newFile = new File(tempPath + key.replaceAll("BOOT-INF/classes", ""));
//                 if (!newFile.getParentFile().exists()) {
//                     newFile.getParentFile().mkdirs();
//                 }
//                 org.apache.commons.io.FileUtils.copyInputStreamToFile(stream, newFile);
//             }
//         } catch (IOException e) {
//             // log.error("failed to copy jar source template", e);
//         }
//     }
//
//     public static Stream<Map.Entry<ZipEntry, InputStream>> readJarFile(JarFile jarFile, String prefix) {
//         Stream<Map.Entry<ZipEntry, InputStream>> readingStream =
//                 jarFile.stream().filter(entry -> !entry.isDirectory() && entry.getName().startsWith(prefix))
//                         .map(entry -> {
//                             try {
//                                 return new AbstractMap.SimpleEntry<>(entry, jarFile.getInputStream(entry));
//                             } catch (IOException e) {
//                                 return new AbstractMap.SimpleEntry<>(entry, null);
//                             }
//                         });
//         return readingStream.onClose(() -> {
//             try {
//                 jarFile.close();
//             } catch (IOException e) {
//                 // MainLogger.error("failed to close jarFile", e);
//             }
//         });
//     }
// }

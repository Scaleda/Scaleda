package top.criwits.scaleda.verilog.utils;

import com.intellij.openapi.project.Project;
import scala.jdk.javaapi.CollectionConverters;
import top.criwits.scaleda.verilog.VerilogPSIFileRoot;

import java.util.List;

public class FileUtilsJava {
    public static List<VerilogPSIFileRoot> getAllVerilogFilesList(Project project) {
        return CollectionConverters.asJava(FileUtils$.MODULE$.getAllVerilogFiles(project));
    }
}

package top.criwits.scaleda.verilog.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
//import top.criwits.scaleda.verilog.VerilogFileType
//import top.criwits.scaleda.verilog.`VerilogFileType$`
import java.util.*
import java.util.stream.Collectors

//fun getAllVerilogFiles(project: Project): List<VerilogFileType> {
//    return FileTypeIndex
//            .getFiles(
//                    `VerilogFileType$`,
//                    GlobalSearchScope.allScope(project)
//            ).stream()
//            .map { PsiManager
//                    .getInstance(project)
//                    .findFile(it) }
//            .filter { Objects.nonNull(it) }
////            .filter { it is VerilogFile }
////            .map { it as VerilogFile }
//            .collect(Collectors.toList())
//}
package top.criwits.scaleda
package idea.project

import idea.ScaledaBundle
import idea.project.ScaledaSDK.SCALEDA_HOME
import idea.utils.Icons
import kernel.utils.Paths

import com.intellij.openapi.projectRoots._
import com.intellij.openapi.vfs.LocalFileSystem
import org.jdom.Element

import java.io.File
import javax.swing.Icon

/** Scaleda SDK Type
  *
  * Obviously, Scaleda is not a software development tool, in which there's no such 'SDK' concept.
  * However, to fit the requirement of IJ platform, SDK here is used to point to Scaleda config files.
  * For example, `~/.scaleda` is treated as the SDK path.
  */
class ScaledaSDK extends SdkType("scaleda") {
  override def getIcon: Icon = Icons.folder

  override def suggestHomePath(): String = {
    val systemEnv = System.getenv()
    if (systemEnv.containsKey(SCALEDA_HOME)) {
      systemEnv.get(SCALEDA_HOME)
    } else {
      // suggest home/.scaleda
      Paths.getConfigDir.getAbsolutePath
    }
  }

  override def isValidSdkHome(path: String): Boolean = {
    val home = LocalFileSystem.getInstance().findFileByIoFile(new File(path))
    if (home != null && home.exists() && home.isDirectory) {
      val toolchain = home.findChild("toolchains")
      // TODO: Add more detect conditions

      if (toolchain != null && toolchain.isDirectory) {
        return true
      }
    }
    false
  }

  override def suggestSdkName(currentSdkName: String, sdkHome: String): String =
    "Scaleda"

  override def createAdditionalDataConfigurable(
      sdkModel: SdkModel,
      sdkModificator: SdkModificator
  ): AdditionalDataConfigurable = null

  override def getPresentableName: String =
    ScaledaBundle.message("sdk.scaleda_config_root")

  override def saveAdditionalData(
      additionalData: SdkAdditionalData,
      additional: Element
  ): Unit = {}
}

object ScaledaSDK {
  val SCALEDA_HOME: String = "SCALEDA_HOME"
}

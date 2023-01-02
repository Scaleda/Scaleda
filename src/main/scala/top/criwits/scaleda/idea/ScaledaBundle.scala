package top.criwits.scaleda
package idea

import com.intellij.DynamicBundle
import org.jetbrains.annotations.{Nls, NonNls, NotNull, PropertyKey}
import top.criwits.scaleda.idea.ScaledaBundle.bundle

final class ScaledaBundle extends DynamicBundle(bundle)

object ScaledaBundle {
  @NonNls
  private val bundle = "bundles.ScaledaBundle"
  private val instance = new ScaledaBundle

  @Nls
  def message(@NotNull @PropertyKey(resourceBundle = "bundles.ScaledaBundle") key: String, @NotNull params: Any*): String =
    instance.getMessage(key, params)
}
package top.scaleda.idea;

import com.intellij.DynamicBundle;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;
import top.scaleda.idea.i18n.DynamicScaledaBundle;

// don't extend DynamicScaledaBundle
// META-INF\plugin.xml: <message-bundle>top.scaleda.idea.ScaledaBundle</message-bundle>
public final class ScaledaBundle {
    @NonNls private static final String BUNDLE = "bundles.ScaledaBundle";

    private static final DynamicBundle INSTANCE = new DynamicBundle(ScaledaBundle.class, BUNDLE);

    private ScaledaBundle() {  }

    @NotNull
    public static @Nls String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object ...params) {
        return INSTANCE.getMessage(key, params);
    }

}


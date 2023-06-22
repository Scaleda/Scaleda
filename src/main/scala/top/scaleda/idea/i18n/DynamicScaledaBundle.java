package top.scaleda.idea.i18n;

import com.intellij.AbstractBundle;
import com.intellij.lang.LangBundle;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.ResourceBundle;

public abstract class DynamicScaledaBundle extends AbstractBundle {
    public DynamicScaledaBundle(@NonNls @NotNull String pathToBundle) {
        super(pathToBundle);
    }

    @NotNull
    @Override
    protected ResourceBundle findBundle(@NotNull @NonNls String pathToBundle, @NotNull ClassLoader loader, @NotNull ResourceBundle.Control control) {
        var base = super.findBundle(pathToBundle, loader, control);
        var ideLocale = LangBundle.getLocale();
        if (!ideLocale.equals(Locale.ENGLISH)) {
            // load your bundle from baseName_<language>.properties, e.g. "baseName_zh.properties"
            var localizedPath = pathToBundle + "_" + ideLocale.getLanguage();
            var localeBundle = super.findBundle(localizedPath, DynamicScaledaBundle.class.getClassLoader(), control);
            if (!base.equals(localeBundle)) {
                setParent(localeBundle, base);
                return localeBundle;
            }
        }
        return base;
    }

    /**
     * Borrows code from {@code com.intellij.DynamicBundle} to set the parent bundle using reflection.
     */
    private static void setParent(ResourceBundle localeBundle, ResourceBundle base) {
        try {
            Method method = ResourceBundle.class.getDeclaredMethod("setParent", ResourceBundle.class);
            method.setAccessible(true);
            MethodHandles.lookup().unreflect(method).bindTo(localeBundle).invoke(base);
        } catch (Throwable e) {
            // ignored, better handle this in production code
        }
    }
}

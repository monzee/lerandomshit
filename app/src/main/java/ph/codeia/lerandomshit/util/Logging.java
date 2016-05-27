package ph.codeia.lerandomshit.util;

/**
 * This file is a part of the Le Random Shit project.
 */
public interface Logging {
    void v(String text, Object... fmtArgs);
    void v(String text, Throwable t);
    void d(String text, Object... fmtArgs);
    void i(String text, Object... fmtArgs);
    void w(String text, Object... fmtArgs);
    void e(String text, Object... fmtArgs);
    void e(String text, Throwable t);
}

package com.example.filmBooking.util;

/**
 * Cung cấp template HTML thống nhất cho email (UTF-8, style gọn, dễ đọc trên mọi client).
 */
public final class EmailHtmlUtil {

    private static final String STYLE_CONTAINER = "max-width:560px;margin:0 auto;font-family:'Segoe UI',Arial,sans-serif;font-size:15px;line-height:1.5;color:#333;";
    /* Theme cam trắng: header gradient cam, body trắng, nút cam */
    private static final String STYLE_HEADER = "background:linear-gradient(135deg,#E65100 0%,#FF9800 50%,#FFB74D 100%);color:#fff;padding:20px 24px;border-radius:8px 8px 0 0;";
    private static final String STYLE_BODY = "background:#fff;padding:24px;border:1px solid #ffe0b2;border-top:none;border-radius:0 0 8px 8px;";
    private static final String STYLE_ROW = "padding:10px 0;border-bottom:1px solid #f5f5f5;";
    private static final String STYLE_LABEL = "font-weight:600;color:#555;min-width:140px;display:inline-block;";
    private static final String STYLE_FOOTER = "margin-top:24px;padding-top:16px;border-top:1px solid #ffe0b2;font-size:13px;color:#888;";
    private static final String STYLE_BTN = "display:inline-block;margin:16px 0;padding:12px 24px;background:#E65100;color:#fff !important;text-decoration:none;border-radius:6px;font-weight:600;";

    private EmailHtmlUtil() {}

    /**
     * Bọc nội dung body bằng layout chuẩn (header + body + footer).
     */
    public static String wrap(String title, String bodyHtml, String footerHtml) {
        return "<div style=\"" + STYLE_CONTAINER + "\">"
                + "<div style=\"" + STYLE_HEADER + "\"><h2 style=\"margin:0;font-size:20px;\">" + escape(title) + "</h2></div>"
                + "<div style=\"" + STYLE_BODY + "\">" + bodyHtml + (footerHtml != null && !footerHtml.isEmpty() ? "<div style=\"" + STYLE_FOOTER + "\">" + footerHtml + "</div>" : "") + "</div>"
                + "</div>";
    }

    /**
     * Một dòng: nhãn + giá trị (value được escape).
     */
    public static String row(String label, String value) {
        if (value == null) value = "—";
        return "<div style=\"" + STYLE_ROW + "\"><span style=\"" + STYLE_LABEL + "\">" + escape(label) + "</span> " + escape(value) + "</div>";
    }

    /**
     * Một dòng: nhãn + nội dung HTML (value không escape, dùng khi có &lt;br/&gt;).
     */
    public static String rowHtml(String label, String htmlContent) {
        if (htmlContent == null) htmlContent = "—";
        return "<div style=\"" + STYLE_ROW + "\"><span style=\"" + STYLE_LABEL + "\">" + escape(label) + "</span> " + htmlContent + "</div>";
    }

    /**
     * Nút link (dùng cho link đặt lại mật khẩu, link xác nhận đơn...).
     */
    public static String buttonLink(String url, String text) {
        return "<a href=\"" + escape(url) + "\" style=\"" + STYLE_BTN + "\">" + escape(text) + "</a>";
    }

    /**
     * Đoạn văn bản.
     */
    public static String paragraph(String text) {
        return "<p style=\"margin:0 0 12px 0;\">" + escape(text) + "</p>";
    }

    public static String escape(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}

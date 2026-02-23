package com.example.filmBooking.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DisplayFormatUtil {

    private DisplayFormatUtil() {}

    /**
     * Chỉ hiển thị loại ghế có số lượng > 0. Nếu chỉ 1 loại thì không cần ngoặc/dòng còn lại.
     */
    public static String formatSeatTypeForDisplay(String seatCountCount) {
        if (seatCountCount == null || seatCountCount.isBlank()) return "—";
        String s = seatCountCount.trim().replaceAll("^[\\(\\[]+|[\\]\\)]+$", "").trim();
        s = s.replaceAll("(\\d+)(ghế)", "$1 $2").replace(",", ", ");

        int don = 0, doi = 0;
        Pattern pDon = Pattern.compile("(\\d+)\\s*ghế đơn");
        Pattern pDoi = Pattern.compile("(\\d+)\\s*ghế đôi");
        Matcher mDon = pDon.matcher(s);
        Matcher mDoi = pDoi.matcher(s);
        if (mDon.find()) don = Integer.parseInt(mDon.group(1));
        if (mDoi.find()) doi = Integer.parseInt(mDoi.group(1));

        int total = don + doi * 2;
        if (total == 0) return "—";

        boolean onlyDon = don > 0 && doi == 0;
        boolean onlyDoi = don == 0 && doi > 0;

        if (onlyDon) return "Ghế đã chọn: " + don + " ghế đơn";
        if (onlyDoi) return "Ghế đã chọn: " + total + " chỗ (" + doi + " ghế đôi – 2 chỗ)";

        StringBuilder sb = new StringBuilder("Ghế đã chọn: ").append(total).append(" ghế (");
        List<String> parts = new ArrayList<>();
        parts.add(don + " ghế đơn");
        parts.add(doi + " ghế đôi – 2 chỗ");
        sb.append(String.join(", ", parts)).append(")");
        return sb.toString();
    }

    /**
     * Chỉ hiển thị: Ghế đơn: A7, A8 và Ghế đôi: B8, B9 (chỉ dòng có dữ liệu).
     * Trả về HTML với &lt;br/&gt; giữa hai dòng. Khi không có mã ghế thì fallback summary.
     */
    public static String formatSeatTypeWithCodes(String seatCountCount, String seatCodesDon, String seatCodesDoi) {
        String donCodes = normalizeSeatCodes(seatCodesDon);
        String doiCodes = normalizeSeatCodes(seatCodesDoi);
        boolean hasDon = donCodes != null && !donCodes.isEmpty();
        boolean hasDoi = doiCodes != null && !doiCodes.isEmpty();

        if (hasDon && hasDoi)
            return "Ghế đơn: " + donCodes + "<br/>Ghế đôi: " + doiCodes;
        if (hasDon) return "Ghế đơn: " + donCodes;
        if (hasDoi) return "Ghế đôi: " + doiCodes;

        return formatSeatTypeForDisplay(seatCountCount);
    }

    private static String normalizeSeatCodes(String raw) {
        if (raw == null || raw.isBlank()) return "";
        String[] codes = raw.trim().split("\\s*,\\s*");
        java.util.Arrays.sort(codes);
        return String.join(", ", codes);
    }

    public static String formatFoodsForDisplay(String foodsRaw) {
        if (foodsRaw == null || foodsRaw.isBlank()) return "Không có";
        String s = foodsRaw.trim();
        if (s.equalsIgnoreCase("không có")) return "Không có";

        List<String> result = new ArrayList<>();
        Pattern p = Pattern.compile("([^(]+)\\((\\d+)\\)");
        Matcher m = p.matcher(s);
        while (m.find()) {
            String name = m.group(1).trim();
            String qty = m.group(2);
            result.add(name + " × " + qty);
        }
        if (result.isEmpty()) return foodsRaw;
        return String.join(", ", result);
    }
}

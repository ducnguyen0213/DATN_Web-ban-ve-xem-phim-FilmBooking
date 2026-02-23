package com.example.filmBooking.config;

import com.example.filmBooking.util.DisplayFormatUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DisplayFormatConfig {

    @Bean(name = "displayFormat")
    public DisplayFormatHelper displayFormatHelper() {
        return new DisplayFormatHelper();
    }

    public static class DisplayFormatHelper {
        public String formatSeatTypeForDisplay(String seatCountCount) {
            return DisplayFormatUtil.formatSeatTypeForDisplay(seatCountCount);
        }
        public String formatFoodsForDisplay(String foodsRaw) {
            return DisplayFormatUtil.formatFoodsForDisplay(foodsRaw);
        }
        public String formatSeatTypeWithCodes(String seatCountCount, String seatCodesDon, String seatCodesDoi) {
            return DisplayFormatUtil.formatSeatTypeWithCodes(seatCountCount, seatCodesDon, seatCodesDoi);
        }
    }
}

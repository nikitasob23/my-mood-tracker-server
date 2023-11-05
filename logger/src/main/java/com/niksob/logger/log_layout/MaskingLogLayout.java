package com.niksob.logger.log_layout;

//import ch.qos.logback.classic.spi.ILoggingEvent;
//import ch.qos.logback.core.LayoutBase;
//import com.niksob.logger.service.masking.MaskingStringFieldService;
//import lombok.AllArgsConstructor;
//
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@AllArgsConstructor
//public class MaskingLogLayout extends LayoutBase<ILoggingEvent> {
//    private MaskingStringFieldService maskingStringFieldService;
//
//    @Override
//    public String doLayout(ILoggingEvent event) {
//        return Stream.of(event.getFormattedMessage())
//                .map(maskingStringFieldService::mask)
//                .collect(Collectors.joining()) + System.lineSeparator();
//    }
//}

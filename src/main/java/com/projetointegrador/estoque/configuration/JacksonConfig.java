//package com.projetointegrador.estoque.configuration;
//
//import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
//import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.format.DateTimeFormatter;
//
//@Configuration
//public class JacksonConfig {
//
//    private static final String FORMATO_BRASILEIRO = "dd/MM/yyyy";
//
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATO_BRASILEIRO);
//
//        return builder -> {
//            builder.simpleDateFormat(FORMATO_BRASILEIRO);
//            builder.serializers(new LocalDateSerializer(formatter));
//            builder.deserializers(new LocalDateDeserializer(formatter));
//        };
//    }
//}

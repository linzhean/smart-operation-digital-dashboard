package tw.edu.ntub.imd.birc.sodd.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@OpenAPIDefinition(
        info = @Info(
                title = "數位儀表板後台 - API",
                description = "此為所有系統API功能列表，如有疑問，請洽負責人員\n" +
                        "\n" +
                        "  以下為標準回傳格式，data請替換成API的RequestBody\n" +
                        "  ```json=\n" +
                        "  {\n" +
                        "      \"result\": boolean,\n" +
                        "      \"errorCode\": string,\n" +
                        "      \"message\": string,\n" +
                        "      \"data\": (參考RequestBody)\n" +
                        "  }\n" +
                        "  ```"
        )
)
public class SwaggerConfig {
}

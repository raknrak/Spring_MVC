package hello.springmvc.basic.request;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyStringController {
    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request,
                                  HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream,
                StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        response.getWriter().write("ok");
    }

    /**
     * InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
     * OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
     */
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter)
            throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream,
                StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
        //**스프링 MVC는 다음 파라미터를 지원한다.**
        //InputStream(Reader): HTTP 요청 메시지 바디의 내용을 직접 조회
        //OutputStream(Writer): HTTP 응답 메시지의 바디에 직접 결과 출력
    }

    /**
     * HttpEntity: HTTP header, body 정보를 편리하게 조회
     * - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     * <p>
     * 응답에서도 HttpEntity 사용 가능
     * - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {
        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);
        return new HttpEntity<>("ok");
        //**스프링 MVC는 다음 파라미터를 지원한다.**
        //**HttpEntity**: HTTP header, body 정보를 편리하게 조회
        //메시지 바디 정보를 직접 조회
        //요청 파라미터를 조회하는 기능과 관계 없음 `@RequestParam` X, `@ModelAttribute` X
        //**HttpEntity는 응답에도 사용 가능**
        //메시지 바디 정보 직접 반환
        //헤더 정보 포함 가능
        //view 조회X
        //`HttpEntity` 를 상속받은 다음 객체들도 같은 기능을 제공한다.
        //**RequestEntity**
        //HttpMethod, url 정보가 추가, 요청에서 사용
        //**ResponseEntity**
        //HTTP 상태 코드 설정 가능, 응답에서 사용
        //return new ResponseEntity<String>("Hello World", responseHeaders,HttpStatus.CREATED)`
        //**참고**
        //스프링MVC 내부에서 HTTP 메시지 바디를 읽어서 문자나 객체로 변환해서 전달해주는데, 이때 HTTP 메시지
        //컨버터( `HttpMessageConverter` )라는 기능을 사용한다
    }

    /**
     * @RequestBody - 메시지 바디 정보를 직접 조회(@RequestParam X, @ModelAttribute X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     * @ResponseBody - 메시지 바디 정보 직접 반환(view 조회X)
     * - HttpMessageConverter 사용 -> StringHttpMessageConverter 적용
     */
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {
        log.info("messageBody={}", messageBody);
        return "ok";
        //**@RequestBody**
        //`@RequestBody` 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다. 참고로 헤더 정보가 필요하다면
        //`HttpEntity` 를 사용하거나 `@RequestHeader` 를 사용하면 된다.
        //이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 `@RequestParam` , `@ModelAttribute` 와
        //는 전혀 관계가 없다.
        //**요청 파라미터 vs HTTP 메시지 바디**
        //요청 파라미터를 조회하는 기능: `@RequestParam` , `@ModelAttribute`
        //HTTP 메시지 바디를 직접 조회하는 기능: `@RequestBody`
        //**@ResponseBody**
        //`@ResponseBody` 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다.
        //물론 이 경우에도 view를 사용하지 않는다.
    }

}
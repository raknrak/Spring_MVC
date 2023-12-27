package hello.springmvc.basic.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {
    /**
     * 반환 타입이 없으면서 이렇게 응답에 값을 직접 집어넣으면, view 조회X
     */
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse
            response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);
        response.getWriter().write("ok");
    }

    /**
     * @RequestParam 사용
     * - 파라미터 이름으로 바인딩
     * @ResponseBody 추가
     * - View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
     */
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge) {
        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }

// `@RequestParam` : 파라미터 이름으로 바인딩
//`@ResponseBody` : View 조회를 무시하고, HTTP message body에 직접 해당 내용 입력
//**@RequestParam**의 `name(value)` 속성이 파라미터 이름으로 사용
//@RequestParam("**username**") String **memberName**
//request.getParameter("**username**")

    /**
     * @RequestParam 사용
     * HTTP 파라미터 이름이 변수 이름과 같으면 @RequestParam(name="xx") 생략 가능
     */
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            @RequestParam String username,
            @RequestParam int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }
    //HTTP 파라미터 이름이 변수 이름과 같으면 `@RequestParam(name="xx")` 생략 가능

    /**
     * @RequestParam 사용
     * String, int 등의 단순 타입이면 @RequestParam 도 생략 가능
     */
    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(String username, int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }
    //`String` , `int` , `Integer` 등의 단순 타입이면 `@RequestParam` 도 생략 가능
    //`@RequestParam` 이 있으면 명확하게 요청 파리미터에서 데이터를 읽는 다는 것을 알 수 있다

    /**
     * @RequestParam.required /request-param-required -> username이 없으므로 예외
     * <p>
     * 주의!
     * /request-param-required?username= -> 빈문자로 통과
     * <p>
     * 주의!
     * /request-param-required
     * int age -> null을 int에 입력하는 것은 불가능, 따라서 Integer 변경해야 함(또는 다음에 나오는
     * defaultValue 사용)
     */
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) Integer age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }
    //`@RequestParam.required`
    //파라미터 필수 여부
    //기본값이 파라미터 필수( `true` )이다.
    //`/request-param-required` 요청
    //`username` 이 없으므로 400 예외가 발생한다.
    //**주의! - 파라미터 이름만 사용**
    //`/request-param-required?username=`
    //파라미터 이름만 있고 값이 없는 경우 빈문자로 통과
    //**주의! - 기본형(primitive)에 null 입력**
    //`/request-param` 요청
    //`@RequestParam(required = false) int age`
    //`null` 을 `int` 에 입력하는 것은 불가능(500 예외 발생)
    //따라서 `null` 을 받을 수 있는 `Integer` 로 변경하거나, 또는 다음에 나오는 `defaultValue` 사용
    /**
     * @RequestParam
     * - defaultValue 사용
     *
     * 참고: defaultValue는 빈 문자의 경우에도 적용
     * /request-param-default?username=
     */
    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }
    //파라미터에 값이 없는 경우 `defaultValue` 를 사용하면 기본 값을 적용할 수 있다.
    //이미 기본 값이 있기 때문에 `required` 는 의미가 없다.
    //`defaultValue` 는 빈 문자의 경우에도 설정한 기본 값이 적용된다.
    //`/request-param-default?username=`

    /**
     * @RequestParam Map, MultiValueMap
     * Map(key=value)
     * MultiValueMap(key=[value1, value2, ...]) ex) (key=userIds, value=[id1, id2])
     */
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(@RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"),
                paramMap.get("age"));
        return "ok";
    }
    //파라미터를 Map, MultiValueMap으로 조회할 수 있다.
    //`@RequestParam Map` ,
    //`Map(key=value)`
    //`@RequestParam MultiValueMap`
    //`MultiValueMap(key=[value1, value2, ...] ex) (key=userIds, value=[id1,
    //id2])`
    //파라미터의 값이 1개가 확실하다면 `Map` 을 사용해도 되지만, 그렇지 않다면 `MultiValueMap` 을 사용하자.
}
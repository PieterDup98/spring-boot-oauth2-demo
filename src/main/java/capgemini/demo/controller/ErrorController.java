package capgemini.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    private final ErrorAttributes errorAttributes;

    public ErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        final ServletWebRequest webRequest = new ServletWebRequest(request);
        final Map<String, Object> attributes = errorAttributes.getErrorAttributes(
                webRequest, ErrorAttributeOptions.defaults());

        final Integer status = (Integer) attributes.get("status");

        if (status != null) {
            final String errorMessage = switch (status) {
                case 403 -> "Access Denied! You do not have permission.";
                case 401 -> "Unauthorized! Please log in.";
                case 404 -> "Page Not Found.";
                case 500 -> "Internal Server Error. Please try again later.";
                default -> "Something went wrong.";
            };

            model.addAttribute("error", errorMessage);
        }
        return "error";
    }
}

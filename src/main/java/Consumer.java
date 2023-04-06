import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class Consumer{
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String       url          = "https://reqres.in/api/users/2";
        String       response     = restTemplate.getForObject(url, String.class);

        System.out.println(formatJSONStr(response, 2));
        // if it's not already, convert to a JSON object

        Map<String, String> jsonToSend = new HashMap<>();

        jsonToSend.put("name", "Test user");
        jsonToSend.put("job", "Test job");

        HttpEntity<Map<String, String>> requestHttpEntity = new HttpEntity<>(jsonToSend);

        String urlRequest = "https://reqres.in/api/users";
        String request   = restTemplate.postForObject(urlRequest,requestHttpEntity, String.class);

        System.out.println("---->request");
        System.out.println(formatJSONStr(request, 2));
    }

    public static String formatJSONStr(final String json_str, final int indent_width) {
        final char[] chars   = json_str.toCharArray();
        final String newline = System.lineSeparator();

        String  ret          = "";
        boolean begin_quotes = false;

        for (int i = 0, indent = 0; i < chars.length; i++) {
            char c = chars[i];

            if (c == '\"') {
                ret += c;
                begin_quotes = !begin_quotes;
                continue;
            }

            if (!begin_quotes) {
                switch (c) {
                    case '{':
                    case '[':
                        ret += c + newline + String.format("%" + (indent += indent_width) + "s", "");
                        continue;
                    case '}':
                    case ']':
                        ret += newline + ((indent -= indent_width) > 0 ? String.format("%" + indent + "s",
                                "") : "") + c;
                        continue;
                    case ':':
                        ret += c + " ";
                        continue;
                    case ',':
                        ret += c + newline + (indent > 0 ? String.format("%" + indent + "s", "") : "");
                        continue;
                    default:
                        if (Character.isWhitespace(c)) continue;
                }
            }

            ret += c + (c == '\\' ? "" + chars[++i] : "");
        }

        return ret;
    }
}

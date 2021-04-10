/*
package sbangularjs.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;
import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
@Log
public class JwtProvider {

    public boolean validateToken(String token) {
        Base64 decoder = new Base64(true);
        byte[] decodesBytes = decoder.decode(token);
        String json = new String(decodesBytes);
        ObjectMapper oMapper = new ObjectMapper();
        try {

            // convert JSON string to Map
            String bodyJSON = json.split("}")[1]+'}';
            Map<String, String> map = oMapper.readValue(bodyJSON, Map.class);

            //Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});


            return json.contains("email");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getLoginFromToken(String token) {
        Base64 decoder = new Base64(true);
        byte[] decodesBytes = decoder.decode(token);
        String json = new String(decodesBytes);
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, String> map = null;
        try {
            // convert JSON string to Map
            // Map<String, String> map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
            String bodyJSON = json.split("}")[1]+'}';
            map = oMapper.readValue(bodyJSON, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(map == null || map.isEmpty()) return null;
        return map.get("email").split("@")[0];
    }
}
*/

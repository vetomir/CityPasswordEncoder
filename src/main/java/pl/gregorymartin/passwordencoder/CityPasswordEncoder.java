package pl.gregorymartin.passwordencoder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;


class CityPasswordEncoder implements PasswordEncoder {
    @Value("${api-key.openweather}")
    private String apiKey;
    @Override
    public String encode(final CharSequence charSequence) {


        final List<Integer> result = new ArrayList<>();
        charSequence.chars().forEach(x -> result.add(x));
        final String[] count = {""};
        result.stream().forEach( x -> {
                count[0] = count[0] + x;
                });

        System.out.println(count[0]);

        String lat = "5" + Integer.valueOf(count[0].substring(10,11)) + "," + count[0].substring(4,7);
        String lon = "2" + Integer.valueOf(count[0].substring(9,10)) + "," + count[0].substring(2,5);
        return getPassword(lat, lon);

    }
    private String getPassword( final String lat, final String lon) {
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+ lat +"&lon="+ lon +"&appid=" + apiKey;

        System.out.println(url);

        ResponseEntity<String> exchange = restTemplate.exchange(url,
                HttpMethod.GET,
                null,
                String.class);

        int startIndex = exchange.getBody().indexOf("name");
        int endIndex = exchange.getBody().indexOf(",", startIndex);

        return exchange.getBody().substring(startIndex + 7, endIndex -1);
    }

    @Override
    public boolean matches(final CharSequence charSequence, final String s) {
        String encoded = encode(charSequence);
        System.out.println(charSequence.toString());
        System.out.println(encoded);
        System.out.println(s);

        return encoded.equals(s);
    }
}

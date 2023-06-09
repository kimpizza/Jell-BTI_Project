package my.jelly.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import my.jelly.controller.JellyController;
import my.jelly.dto.JellyDTO;
import my.jelly.dto.RateDTO;
import my.jelly.entity.JInfo;
import my.jelly.entity.JRate;
import my.jelly.repository.RateRepositorySpringDataJpa;
import my.jelly.repository.JellyRepositorySpringDataJpa;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
class JellyInformationTest {

    @Autowired
    private JellyController controller;

    @Autowired
    private JellyInformationService service;

    @Autowired
    private JellyRepositorySpringDataJpa repository;

    @Autowired
    private RateService rateService;

    @Autowired
    private RateRepositorySpringDataJpa rateRepository;

    @Test
    void getJellyList() throws IOException, ParseException{
        StringBuilder urlBuilder = new StringBuilder("https://openapi.foodsafetykorea.go.kr/api/c7e42419587e4873ae88/I2790/json/1/200"); /*URL*/
        urlBuilder.append("/" + URLEncoder.encode("DESC_KOR","UTF-8") + "=" + URLEncoder.encode("하리보 골드베렌", "UTF-8")); /*식품이름*/
//        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("2", "UTF-8")); /*페이지번호*/
//        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("99", "UTF-8")); /*한 페이지 결과 수*/
//        urlBuilder.append("&" + URLEncoder.encode("bgn_year","UTF-8") + "=" + URLEncoder.encode("2021", "UTF-8")); /*구축년도*/
//        urlBuilder.append("&" + URLEncoder.encode("animal_plant","UTF-8") + "=" + URLEncoder.encode("(유)돌코리아", "UTF-8")); /*가공업체*/
//        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*응답데이터 형식(xml/json) Default: xml*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
//        System.out.println(sb.toString());

        // 1. 문자열 형태의 JSON을 파싱하기 위한 JSONParser 객체 생성.
        JsonParser parser = new JsonParser();
        // 2. 문자열을 JSON 형태로 JSONObject 객체에 저장.
        JsonObject obj = (JsonObject)parser.parse(sb.toString());
        // 3. 필요한 리스트 데이터 부분만 가져와 JSONArray로 저장.
        JsonObject dataArr = (JsonObject) obj.get("I2790");
//        JSONObject rows = (JSONObject) dataArr.get("row");
        JsonArray row = (JsonArray) dataArr.get("row");
//        System.out.println(row.toString());

        List<JInfo> jellies = new ArrayList<>();
        int result = 0;
        for(int i = 0; i < row.size(); i++){
            JsonObject tmp = (JsonObject) row.get(i);
            JellyDTO jelly = new JellyDTO();

            // 제품 이름에 용량이 들어가 있는 제품이 있는경우
            String tmpName = String.valueOf(tmp.get("DESC_KOR"));
            String name;
            String gram;
            if (tmpName.contains("\\(")) {
                name = String.valueOf(tmp.get("DESC_KOR")).split("\\(")[0];
                gram = String.valueOf(tmp.get("DESC_KOR")).split("\\(")[1].replace("G)","");
            }else {
                name = String.valueOf(tmp.get("DESC_KOR"));
                gram = "";
            }
            System.out.println("이름 : " + String.valueOf(tmp.get("DESC_KOR")).replace("\"","")
                    + ", 열량 : " + String.valueOf(tmp.get("NUTR_CONT1")).replace("\"","")
                    + ", 탄수화물 : " + tmp.get("NUTR_CONT2")
                    + ", 단백질 : " + tmp.get("NUTR_CONT3")
                    + ", 지방 : " + tmp.get("NUTR_CONT4")
                    + ", 당류 : " + tmp.get("NUTR_CONT5")
                    + ", 나트륨 : " + tmp.get("NUTR_CONT6")
                    + ", 콜레스테롤 : " + tmp.get("NUTR_CONT7")
                    + ", 포화지방산 : " + tmp.get("NUTR_CONT8")
                    + ", 트랜스지방 : " + tmp.get("NUTR_CONT9")
            );

        }
    }
    // 깃허브 테스트
    @Test
    void saveData() throws IOException, ParseException {
        int result = service.createJellyInformation();
        List<JInfo> jellies = repository.findAll();

        assertThat(result).isEqualTo(jellies.size());
    }

    @Test
    void 컨트롤러부터젤리정보저장() throws IOException {
        controller.createJellyInformation();
    }

    @Test
    void 모든젤리영양성분정보가져오기(){
        List<JInfo> list = controller.readJellyInformation("맛없는거");
        assertThat(list.size()).isEqualTo(0);

    }

    @Test
    void 젤리정보수정하기(){
        JellyDTO jellyDTO = new JellyDTO();
        jellyDTO.setJIdx(3671L);
        jellyDTO.setJCarbohydrate("0");
        jellyDTO.setJProtein("0");

        controller.updateJellyInformation(3671L, jellyDTO);

        JInfo jInfo = repository.findById(3671L).orElseThrow();

        assertThat(jellyDTO.getJCarbohydrate()).isEqualTo(jInfo.getJCarbohydrate());
    }

    @Test
    void 젤리정보하나만가져오기() {
        Map<String, Object> result = controller.readJellyInformationById(4739L);

        JellyDTO jelly = (JellyDTO) result.get("jelly");
        List<JRate> rates = (List<JRate>) result.get("rates");

        assertThat(jelly.getJIdx()).isEqualTo(4739L);

        for (JRate rate : rates) {
            assertThat(rate.getJInfoVO().getJIdx()).isEqualTo(4739L);
        }


    }

    @Test
    void 젤리평가저장하기() {
        RateDTO rateDTO = new RateDTO();
        rateDTO.setJIdx(3671L);
        rateDTO.setMEmail("magicofclown");
        rateDTO.setMJelly("testJelly");
        rateDTO.setJStar(4);
        rateDTO.setRContent("개마싯다");
        JRate result = rateService.createJellyRate(rateDTO);

        assertThat(rateDTO.getJStar()).isEqualTo(result.getJStar());
    }

    @Test
    void 이메일로평가정보가져오기() {
        List<JRate> results = controller.findRatesByEmail("magicofclown", "naver.com");
        assertThat(results.size()).isEqualTo(3);
    }

    @Test
    void 젤리후기수정하기() {
        RateDTO dto = new RateDTO();
        dto.setJStar(3);
        dto.setRContent("안맛있다");
        JRate result = controller.updateRate(3802L, dto);
        assertThat(result.getJStar()).isEqualTo(dto.getJStar());
        assertThat(result.getRContent()).isEqualTo(dto.getRContent());
    }

    @Test
    void 젤리id로후기가져오기() {
        JRate jRate = controller.findRateById(3802L);
        assertThat(jRate.getRIdx()).isEqualTo(3802L);
    }

    @Test
    void 젤리id로후기정보가져오기(){
    }
}

package service;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MapApi {
	private static String apiUrl;
	private static String auth = "KakaoAK " + "3133a1cc5809812626bdd5e34f1410e5";

	public static String[] getLocation(String keyword) throws Exception {
		String loc[][] = new String[15][2];
		Scanner scan = new Scanner(System.in);
		int command = 0;

// 키워드 검색시 필요
		String encodeName = "";  // 한글 encoding 해서 날려야 함
		try { encodeName = URLEncoder.encode(keyword, "UTF-8" ); }
		catch ( UnsupportedEncodingException e ) { e.printStackTrace(); }

		apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json" + "?query=" + encodeName;

		URL url = new URL( apiUrl );
		HttpsURLConnection conn = ( HttpsURLConnection ) url.openConnection();
		conn.setRequestMethod( "GET" );
		conn.setRequestProperty( "Authorization", auth );

		BufferedReader br;

		int responseCode = conn.getResponseCode();
		if( responseCode == 200 ) {  // 호출 OK
			br = new BufferedReader( new InputStreamReader(conn.getInputStream(), "UTF-8") );
		} else {  // 에러
			br = new BufferedReader( new InputStreamReader(conn.getErrorStream(), "UTF-8") );
		}
		System.out.printf("     %-20s%-20s%-30s%-30s", "장소명", "분류", "도로명주소", "지번주소");
		System.out.println("\n------------------------------------------------------------------------------------------------------------------");

		String jsonString = new String();
		String stringLine;
		while ( ( stringLine= br.readLine()) != null ) {
			jsonString += stringLine;
		}

		// JSON Parsing
		try {
			JSONParser jsonParser = new JSONParser();

			//JSON데이터를 넣어 JSON Object 로 만들어 준다.
			JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);

			//배열 추출
			JSONArray placeInfoArray = (JSONArray) jsonObject.get("documents");

			for (int i = 0; i < placeInfoArray.size(); i++) {
				//배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
				JSONObject placeObject = (JSONObject) placeInfoArray.get(i);
				//JSON name으로 추출
				System.out.printf("%-5s%-20s%-20s%-30s%-30s\n", i+1, placeObject.get("place_name"), placeObject.get("category_group_name"), placeObject.get("road_address_name"), placeObject.get("address_name"));
				loc[i][0] = String.valueOf(placeObject.get("x"));
				loc[i][1] = String.valueOf(placeObject.get("y"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		while(true) {
			System.out.println("현재 위치로 선택할 장소 번호를 입력하세요 : ");
			command = Integer.parseInt(scan.nextLine());
			if (command >=1 && command <= 15) {
				System.out.printf("x = %s\n", loc[command-1][0]);
				System.out.printf("y = %s\n", loc[command-1][1]);
				return loc[command-1];
			}
			else
				System.out.println("1~15 사이의 장소를 선택해주세요");
		}
	}

	public static void getCafeInfo(String x, String y, String cafeName) throws Exception {

// 키워드 검색시 필요
		String encodeName = "";  // 한글 encoding 해서 날려야 함
		try { encodeName = URLEncoder.encode(cafeName, "UTF-8" ); }
		catch ( UnsupportedEncodingException e ) { e.printStackTrace(); }
		

		apiUrl = "https://dapi.kakao.com/v2/local/search/keyword.json" + "?query=" + encodeName + "&x=" + x + "&y=" + y + "&radius=2000" + "&sort=distance";
		auth = "KakaoAK " + "3133a1cc5809812626bdd5e34f1410e5";
		
		URL url = new URL( apiUrl );
	    HttpsURLConnection conn = ( HttpsURLConnection ) url.openConnection();
		conn.setRequestMethod( "GET" );
	    conn.setRequestProperty( "Authorization", auth );
	    
	    BufferedReader br;

	    int responseCode = conn.getResponseCode();
	    if( responseCode == 200 ) {  // 호출 OK
	    	br = new BufferedReader( new InputStreamReader(conn.getInputStream(), "UTF-8") );
	    } else {  // 에러
	    	br = new BufferedReader( new InputStreamReader(conn.getErrorStream(), "UTF-8") );
	    }
		System.out.printf("%-20s%-20s%-30s%-30s%-10s", "카페명", "연락처", "도로명주소", "지번주소", "거리(m)");
		System.out.println("\n------------------------------------------------------------------------------------------------------------------");

	    String jsonString = new String();
	    String stringLine;
	    while ( ( stringLine= br.readLine()) != null ) {
	    	jsonString += stringLine;
	    }

	    // JSON Parsing
		try {
			JSONParser jsonParser = new JSONParser();

			//JSON데이터를 넣어 JSON Object 로 만들어 준다.
			JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);

			//배열 추출
			JSONArray cafeInfoArray = (JSONArray) jsonObject.get("documents");

			for (int i = 0; i < cafeInfoArray.size(); i++) {

				//배열 안에 있는것도 JSON형식 이기 때문에 JSON Object 로 추출
				JSONObject cafeObject = (JSONObject) cafeInfoArray.get(i);
				//JSON name으로 추출
				System.out.printf("%-20s%-20s%-30s%-30s%-10s\n", cafeObject.get("place_name"), cafeObject.get("phone"), cafeObject.get("road_address_name"), cafeObject.get("address_name"), cafeObject.get("distance"));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) throws Exception {
		getCafeInfo("127.05902969025047", "37.51207412593136", "스타벅스");
		getCafeInfo("127.05902969025047", "37.51207412593136", "커피빈");
		getCafeInfo("127.05902969025047", "37.51207412593136", "이디야");
		getLocation("아주대학교");
	}
}

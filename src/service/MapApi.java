package service;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import repository.UserRepository;
import repository.CafeRepository;


public class MapApi {
	public UserRepository userRepository;
	public CafeRepository cafeRepository;

	public MapApi(Statement st) {
		userRepository = new UserRepository(st);
		cafeRepository = new CafeRepository(st);
	}

	Scanner scan = new Scanner(System.in);
	private static String apiUrl;
	private static String auth = "KakaoAK " + "3133a1cc5809812626bdd5e34f1410e5";

	public void setUserLocation(int uID) throws Exception {
		String loc[][] = new String[15][2];
		Scanner scan = new Scanner(System.in);

		System.out.print("현재 위치로 지정할 장소를 검색하세요 : ");
		String keyword = scan.nextLine();

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
				userRepository.queryLocation(uID, Double.parseDouble(loc[command-1][0]), Double.parseDouble(loc[command-1][1]));
				break;
			}
			else
				System.out.println("1~15 사이의 장소를 선택해주세요");
		}
	}

	public void nearCafe(int uID) throws SQLException {
		System.out.print("찾고자 하는 카페 브랜드를 입력하세요 : ");
		String cafeName = scan.nextLine();
		cafeRepository.getCafeInfo(uID, cafeName);
	}
//	public static void main(String[] args) throws Exception {
//		getCafeInfo("127.05902969025047", "37.51207412593136", "스타벅스");
//		getCafeInfo("127.05902969025047", "37.51207412593136", "커피빈");
//		getCafeInfo("127.05902969025047", "37.51207412593136", "이디야");
//		getLocation("아주대학교");
//	}
}

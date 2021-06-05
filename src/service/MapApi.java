package service;

import java.io.*;
import java.net.*;
import javax.net.ssl.HttpsURLConnection;

public class map_api {
	public static String getCafeInfo(String x, String y) throws Exception {

// 키워드 검색시 필요
//		String encodeName = "";  // 한글 encoding 해서 날려야 함
//		try { encodeName = URLEncoder.encode(name, "UTF-8" ); } 
//		catch ( UnsupportedEncodingException e ) { e.printStackTrace(); }
		
		String apiUrl = "https://dapi.kakao.com/v2/local/search/category.json" + "?category_group_code=CE7" + "&x=" + x + "&y=" + y + "&radius=2000";
		System.out.println(apiUrl);
		String auth = "KakaoAK " + "3133a1cc5809812626bdd5e34f1410e5";
		
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
	    
	    String jsonString = new String();
	    String stringLine;
	    while ( ( stringLine= br.readLine()) != null ) {
	        jsonString += stringLine;
	    }
	    
	    return jsonString;
	}
	public static void main(String[] args) throws Exception {
		
		System.out.print(getCafeInfo("127.05902969025047", "37.51207412593136"));

	}
}

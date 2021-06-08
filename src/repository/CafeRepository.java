package repository;

import config.Env;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



public class CafeRepository {
    private final Statement st;
    private String query;
    private final String folderLocation = Env.getFolderLocation();

    ArrayList<CafeRepository.distInfo> distInfos = new ArrayList<>();

    public CafeRepository(Statement st) {
        this.st = st;
    }

    class distInfo {
        private int cafeID;
        private String cafeName;
        private String branchName;
        private String roadNameAddress;
        private String numberAddress;
        private double distance;

        public distInfo(int cafeID, String cafeName, String branchName, String roadNameAddress, String numberAddress, double distance) {
            this.cafeID = cafeID;
            this.cafeName = cafeName;
            this.branchName = branchName;
            this.roadNameAddress = roadNameAddress;
            this.numberAddress = numberAddress;
            this.distance = distance;
        }

        public int getCafeID() {
            return this.cafeID;
        }

        public double getDistance() {
            return this.distance;
        }

    }

    public class DistComparator implements Comparator {
        @Override
        public int compare(Object o1, Object o2) {
            double d1 = ((distInfo)o1).getDistance();
            double d2 = ((distInfo)o2).getDistance();

            if(d1 > d2){
                return 1;
            }else if(d1 < d2){
                return -1;
            }else{
                return 0;
            }
        }
    }

    // 두 경도(x) 위도(y) 거리 계산 x1, y1, x2, y2
    private static double distance(double lon1, double lat1, double lon2, double lat2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1609.344; // meter 변환
        return dist;
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public void createTable() throws SQLException {
        query = "DROP TABLE IF EXISTS Cafe;\n" +
                "CREATE TABLE Cafe (\n" +
                "  cafeID integer,\n" +
                "  cafeName VARCHAR(50),\n" +
                "  branchName VARCHAR(50),\n" +
                "  numberAddress VARCHAR(90),\n" +
                "  roadNameAddress VARCHAR(90),\n" +
                "  longitude float,\n" +
                "  latitude float,\n" +
                "  Primary KEY(cafeID)\n" +
                ");";

        st.executeUpdate(query);
    }

    public void importCSV() throws SQLException {
//        절대경로로 개인마다 바꿔야함.
//        경로에 한글이 껴있으면 안됨.
//        query = "\\copy Cafe FROM '" + folderLocation + "/cafeData.csv' DELIMITER ',' CSV HEADER;";
        query = "copy Cafe FROM '/private/tmp/cafeData.csv' DELIMITER ',' CSV HEADER;";
        st.executeUpdate(query);

    }

    public void showTable() throws SQLException {
        query = "select * from cafe";
        ResultSet rs = st.executeQuery(query);
        System.out.printf("%-8s%-20s%-20s%-20s%-20s", "cafeId", "cafeName", "branchName", "longitude", "latitude");
        System.out.println("\n-------------------------------------------------------------------------------------------");

        while (rs.next()) {
            System.out.printf("%-8s%-20s%-20s%-20s%-20s", rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(6), rs.getString(7));
            System.out.println();
        }
    }

    public double[] getUserLocation(int uID) throws SQLException {
        double loc[] = new double[2];
        query = "select longitude, latitude from Users where uID = " + uID;
        ResultSet rs = st.executeQuery(query);
        while (rs.next()) {
            loc[0] = rs.getDouble(1);
            loc[1] = rs.getDouble(2);
        }
        return loc;
    }

    public void getCafeInfo(int uID, String brandName) throws SQLException {
        double loc[] = getUserLocation(uID);
        double dist;

        query = "SELECT cafeID, cafeName, branchName, roadNameAddress, numberAddress, longitude, latitude FROM cafe WHERE cafeName LIKE '%' || '" + brandName + "' || '%';";
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            dist = distance(loc[0], loc[1], rs.getDouble(6), rs.getDouble(7));
            distInfos.add(new CafeRepository.distInfo(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), dist));
        }
        Collections.sort(distInfos, new DistComparator());

        System.out.printf("%-5s%-20s%-20s%-30s%-30s%-10s", "번호", "카페명", "지점명", "도로명주소", "지번주소", "거리(m)");
        System.out.println("\n------------------------------------------------------------------------------------------------------------------------------");
        for (int i = 0; i < 15; i++) {
            System.out.printf("%-5d%-20s%-20s%-30s%-30s%-10.3f\n", distInfos.get(i).cafeID, distInfos.get(i).cafeName, distInfos.get(i).branchName, distInfos.get(i).roadNameAddress, distInfos.get(i).numberAddress, distInfos.get(i).distance);
        }
    }
}

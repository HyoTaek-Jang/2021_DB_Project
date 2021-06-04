package service;

import java.sql.Statement;

public class AdministratorService {
    private Statement st = null;

    public AdministratorService(Statement st){
        this.st = st;
    }
    public void setTables(){
        System.out.println("setTables");
    }

    public void showTables() {
        System.out.println("showTables");

    }
}

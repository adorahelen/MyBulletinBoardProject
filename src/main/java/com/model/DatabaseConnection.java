package com.model;

import java.sql.*;

public class DatabaseConnection {
    private static Connection con; // 필드 선언
    private DatabaseConnection() { } // 생성자

    public static Connection getConnection(){
        if( con == null ) {
            String url = "jdbc:mysql://localhost:3306/modeldb";
            String username = "root";
            String password = "12345678";

            try {
                con = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return con;
    }

    //** 4 ~ 7은 모두 공유 메서드로 작성
    //4. ResultSet 객체와 PreparedStatement 객체를 매개변수로 받아서 닫는 close 메서드 작성
    public static void close(ResultSet rs, PreparedStatement pstmt){
        try {   //객체 생성 역순으로 닫음
            if( rs != null )        rs.close();
            if( pstmt != null )     pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //5. ResultSet 객체와 Statement 객체를 매개변수로 받아서 닫는 close 메서드 오버로딩
    public static void close(ResultSet rs, Statement stmt){
        try {
            if( rs != null )        rs.close();
            if( stmt != null )      stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //6. PreparedStatement 객체를 매개변수로 받아서 닫는 close 메서드 오버로딩
    public static void close(PreparedStatement pstmt){
        try {
            if( pstmt != null )     pstmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //7. 필드로 선언된 객체를 닫는 close 메서드 오버로딩
    public static void close(){
        try {
            if( con != null )     con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}



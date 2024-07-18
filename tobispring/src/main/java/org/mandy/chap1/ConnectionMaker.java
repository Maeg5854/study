package org.mandy.chap1;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {
    public Connection getConnection() throws ClassNotFoundException, SQLException;
}

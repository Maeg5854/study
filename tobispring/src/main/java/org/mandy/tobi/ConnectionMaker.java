package org.mandy.tobi;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionMaker {
    public Connection getConnection() throws ClassNotFoundException, SQLException;
}
